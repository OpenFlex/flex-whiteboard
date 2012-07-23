/*

   Copyright 2001-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.flex.forks.batik.test.xml;

import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.flex.forks.batik.test.DefaultTestSuite;
import org.apache.flex.forks.batik.test.DefaultTestReport;
import org.apache.flex.forks.batik.test.TestReport;
import org.apache.flex.forks.batik.test.TestSuite;
import org.apache.flex.forks.batik.test.Test;
import org.apache.flex.forks.batik.test.TestFilter;
import org.apache.flex.forks.batik.test.TestException;
import org.apache.flex.forks.batik.test.TestReportProcessor;


import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class can be used to build and run a <tt>TestSuite</tt> from
 * an XML description following the "XML Test Run" and "XML Test Suite"
 * formats, whose constants are defined in the <tt>XTRunConstants</tt>
 * and <tt>XTSConstants</tt> interfaces.
 *
 * This class takes a "Test Run" XML description as an input. That
 * description contains: <br />
 * + pointers to a number of "Test Suite" XML descriptions, 
 * which contain the definition of the set of <tt>Tests</tt> to be
 * run and their configuration.<br />
 * + a description of the set of <tt>TestReportProcessor</tt> and
 * their configuration that should be used to process the reports
 * generated by the various <tt>TestSuites</tt>.<br />
 * 
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id: XMLTestSuiteRunner.java,v 1.13 2004/08/18 07:17:08 vhardy Exp $
 */
public class XMLTestSuiteRunner implements XTRunConstants, XTSConstants{
    /**
     * Displayed when no test or testSuite matching the input id was
     * found.
     * {0} : unmatched id set
     */
    public static final String MESSAGE_UNMATCHED_TEST_IDS 
        = "XMLTestSuiteRunner.messages.unmatched.test.ids";

    /**
     * An error happened while processing a <tt>TestreportProcessor</tt>
     * description.
     * {0} : the <testReportProcessor> "className" attribute value
     * {1} : exception's class name
     * {2} : exception's message
     * {3} : exception's stack trace
     */
    public static final String CANNOT_CREATE_TEST_REPORT_PROCESSOR
        = "xml.XMLTestSuiteRunner.error.cannot.create.test.report.processor";

    /**
     * An error happened while running the <tt>TestSuite</tt>
     * {0} : <tt>TestSuite</tt> name
     * {1} : <tt>TestSuite</tt> class name.
     * {1} : exception's class name.
     * {2} : exception's message
     * {3} : exception's stack trace.
     */
    public static final String TEST_SUITE_EXCEPTION
        = "xml.XMLTestSuiteRunner.test.suite.exception";

    /**
     * An error happened while processing the <tt>TestReport</tt>
     * generated by the <tt>TestSuite</tt>
     * {0} : <tt>TestReportProcessor</tt> class name.
     * {1} : exception's class name.
     * {2} : exception's message
     * {3} : exception's stack trace.
     */
    public static final String TEST_REPORT_PROCESSING_EXCEPTION
        = "xml.XMLTestSuiteRunner.error.test.report.processing.exception";

    /**
     * Test filter which accepts all tests
     */
    public static class AcceptAllTestsFilter implements TestFilter{
        public Test filter(Test t){
            return t;
        }
    }

    /**
     * Test filter which only accepts tests with ids matching 
     * the ones passed to its constructor.
     */
    public static class IdBasedTestFilter implements TestFilter {
        protected String[] ids;
        protected Set unmatchedIds = new HashSet();

        public IdBasedTestFilter(String[] ids){
            this.ids = ids;
            for(int i=0; i<ids.length; i++){
                unmatchedIds.add(ids[i]);
            }
        }

        public String traceUnusedIds(){
            Object[] ui = unmatchedIds.toArray();
            StringBuffer sb = null; 
            if(ui != null && ui.length > 0){
                sb = new StringBuffer();
                sb.append(ui[0].toString());
                for(int i=1; i<ui.length; i++){
                    sb.append(", ");
                    sb.append(ui[i].toString());
                }
            }
            return sb != null ? sb.toString() : null;
        }

        /**
         * Remove children <tt>Test</tt> instances from the <tt>TestSuite</tt>
         * if they are filtered out.
         */
        public void filterTestSuite(TestSuite ts){
            Test[] t = ts.getChildrenTests();
            int nTests = t != null ? t.length : 0;
            for(int i=0; i<nTests; i++){
                if(filter(t[i]) == null){
                    ts.removeTest(t[i]);
                }
            }
        }

        /**
         * Accept a test if one of the ids is found (i.e., an
         * exact match or a substring) in the <tt>Test</tt>'s
         * qualified id.
         * <tt>TestSuite</tt>s are accepted if they have children and
         * rejected if they have none.
         */
        public Test filter(Test t){
            String id = t.getQualifiedId();
            boolean isRequested = isRequestedId(id);

            //
            // First, handle TestSuite children
            //
            if(t instanceof TestSuite){
                TestSuite ts = (TestSuite)t;
                filterTestSuite(ts);
                if(ts.getChildrenCount() > 0){
                    return t;
                }
                return null;
            }

            //
            // Now, handle leaf Tests
            //
            if(isRequested){
                return t;
            }

            return null;
        }

        protected boolean isRequestedId(String id){
            for(int i=0; i<ids.length; i++){
                //
                // id substring of ids[i]
                // ======================
                // if the test identifier (id) is a substring of one of the requested 
                // then the test is one of the requested test parents and should be accepted.
                // Example: id = "all.B" with requested ids = "all.A all.B.B3"
                //          "all.B" (id) is a substring of "all.B.B3" (ids[1])
                // Conclusion: id is accepted because it is a parent test of ids[1]
                // 
                // ids[i] substring of id
                // ======================
                // if one of the requested test identifiers (id[i]) is a substring of the
                // test id, then one of the test children is requested, so the test should
                // be accepted.
                // Example: id = "all.B.B3" with requested ids = "all.B"
                //          "all.B" (ids[0]) is a substring of "all.B.B3" (id)
                // Conclusion: id is accepted because it is a child test of ids[0]
                //
                if(ids[i].lastIndexOf(id) == 0){
                    // System.out.println("accepting " + id + ". It is (or is the a parent of) " + ids[i]);
                    unmatchedIds.remove(ids[i]);
                    return true;
                }
                
                if(id.lastIndexOf(ids[i]) != -1){
                    // System.out.println("accepting " + id + " it is (or is a child of) the requested " + ids[i]);
                    unmatchedIds.remove(ids[i]);
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * Builds an array of <tt>TestReportProcessor</tt> from the input
     * element, assuming the input element is a <testSuite> instance,
     */
    protected TestReportProcessor[] extractTestReportProcessor(Element element)
        throws TestException
    {
        Vector processors = new Vector();
        
        NodeList children = element.getChildNodes();
        if(children != null && children.getLength() > 0){
            int n = children.getLength();
            for(int i=0; i<n; i++){
                Node child = children.item(i);
                if(child.getNodeType() == Node.ELEMENT_NODE){
                    Element childElement = (Element)child;
                    String tagName = childElement.getTagName().intern();
                    if(tagName == XTRun_TEST_REPORT_PROCESSOR_TAG){
                        processors.addElement(buildProcessor(childElement));
                    }
                }
            }
        }
        
        TestReportProcessor[] p = null;
        if(processors.size() > 0){
            p = new TestReportProcessor[processors.size()];
            processors.copyInto(p);
        }

        return p;
    }

    /**
     * Builds a <tt>TestResultProcessor</tt> from an element.
     */
    protected TestReportProcessor buildProcessor(Element element)
        throws TestException {
        try{
            return (TestReportProcessor)XMLReflect.buildObject(element);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new TestException(CANNOT_CREATE_TEST_REPORT_PROCESSOR,
                                    new Object[] { element.getAttribute(XR_CLASS_ATTRIBUTE),
                                                   e.getClass().getName(),
                                                   e.getMessage(),
                                                   sw.toString() },
                                    e);
        }
    }

    /**
     * Builds a <tt>TestSuite</tt> from an input element.
     * This method assumes that element is a &lt;testRun&gt;
     * instance. The element is scanned for children
     * &lt;testSuite&gt; elements which is loaded into
     * a <tt>Test</tt> and composited into a <tt>TestSuite</tt>
     */
    protected DefaultTestSuite buildTestRunTestSuite(Element element)
        throws TestException {
        DefaultTestSuite testSuite = new DefaultTestSuite();

        //
        // Set the testRun name and id on the top level testSuite
        //
        String name = element.getAttribute(XTRun_NAME_ATTRIBUTE);
        testSuite.setName(name);
        
        String id = element.getAttribute(XTRun_ID_ATTRIBUTE);
        testSuite.setId(id);

        Element[] testSuites 
            = getChildrenByTagName(element, XTRun_TEST_SUITE_TAG);

        int n = testSuites != null ? testSuites.length : 0;
        for(int i=0; i<n; i++){
            String suiteHref = 
                testSuites[i].getAttribute(XTRun_HREF_ATTRIBUTE);

            Test test = XMLTestSuiteLoader.loadTestSuite(suiteHref, testSuite);
            if(test != null){
                testSuite.addTest(test);
            }
        }

        return testSuite;
    }

    /**
     * Gets all the children of a given type.
     */
    protected Element[] getChildrenByTagName(Element element,
                                             String tagName)
    {
        tagName = tagName.intern();
        Vector childrenWithTagName = new Vector();
        
        NodeList children = element.getChildNodes();
        if(children != null && children.getLength() > 0){
            int n = children.getLength();
            for(int i=0; i<n; i++){
                Node child = children.item(i);
                if(child.getNodeType() == Node.ELEMENT_NODE){
                    Element childElement = (Element)child;
                    String childTagName = childElement.getTagName().intern();
                    if(childTagName == tagName){
                        childrenWithTagName.addElement(childElement);
                    }
                }
            }
        }
        
        Element[] a = null;
        if(childrenWithTagName.size() > 0){
            a = new Element[childrenWithTagName.size()];
            childrenWithTagName.copyInto(a);
        }

        return a;        
    }


    /**
     * Runs the test suite described by the input
     * Document object. If the input ids array
     * is null or of zero length, then all the tests will be run.
     * Otherwise, only the tests identified by 
     * the array will be run.
     */
    public TestReport run(Document doc, String[] ids) 
        throws TestException {
        Element root = doc.getDocumentElement();

        return run(root, ids);
    }

    protected TestReport runTest(Test test)
        throws TestException {
        try{
            return test.run();
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new TestException(TEST_SUITE_EXCEPTION,
                                    new Object[] { test.getName(),
                                                   test.getClass().getName(),
                                                   e.getClass().getName(),
                                                   e.getMessage(),
                                                   sw.toString() },
                                    e);            
        }
    }

    protected void processReport(TestReport report,
                                 TestReportProcessor[] processors)
        throws TestException {
        int n = processors.length;
        int i=0;
        try{
            for(; i<n; i++){
                processors[i].processReport(report);
            }
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new TestException(TEST_REPORT_PROCESSING_EXCEPTION,
                                    new Object[] { processors[i].getClass().getName(),
                                                   e.getClass().getName(),
                                                   e.getMessage(),
                                                   sw.toString() },
                                    e);            
        }
    }

    protected TestReport run(Element testRunElement, String[] ids)
        throws TestException{
        //
        // First, build entire suite of tests
        //
        Test testRun
            = buildTestRunTestSuite(testRunElement);

        //
        // Filter testSuite if necessary
        //
        Test filteredTestRun = testRun;
        if(ids != null && ids.length > 0){
            IdBasedTestFilter filter = new IdBasedTestFilter(ids);
            filteredTestRun = filter.filter(testRun);
            String unusedIds = filter.traceUnusedIds();
            if(unusedIds != null){
                System.err.println(Messages.formatMessage(MESSAGE_UNMATCHED_TEST_IDS,
                                                          new Object[]{unusedIds}));
            }
        }

        if(filteredTestRun == null){
            DefaultTestReport report 
                = new DefaultTestReport(testRun);
            report.setPassed(true);
            return report;
        }

        //
        // Now, get the set of TestReportProcessors
        // that can use the data
        //
        TestReportProcessor[] processors 
            = extractTestReportProcessor(testRunElement);

        //
        // Run the test
        //
        TestReport report = runTest(testRun);
        
        //
        // Process the report
        //
        if(processors != null){
            processReport(report, processors);
        }

        return report;
    }

    /**
     * Displayed when the user passes no arguments to the command line.
     */
    public static final String USAGE 
        = "XMLTestSuiteRunner.messages.error.usage";

    /**
     * Displayed when the input argument does not represent an existing
     * file to notify the user that the argument is going to be 
     * interpreted as a URI.
     */
    public static final String NOT_A_FILE_TRY_URI
        = "XMLTestSuiteRunner.messages.error.not.a.file.try.uri";

    /**
     * Displayed when the input file name cannot be turned into a URL
     */
    public static final String COULD_NOT_CONVERT_FILE_NAME_TO_URI
        = "XMLTestSuiteRunner.messages.error.could.not.convert.file.name.to.uri";

    /**
     * Displayed when the input argument does not represent a valid
     * URI
     */
    public static final String INVALID_URI
        = "XMLTestSuiteRunner.messages.error.invalid.uri";

    /**
     * Displayed when the input document cannot be parsed.
     * {0} : uri of the invalid document.
     * {1} : exception generated while parsing
     * {2} : exception message
     */
    public static final String INVALID_DOCUMENT 
        = "XMLTestSuiteRunner.messages.error.invalid.document";

    /**
     * Error displayed when an error occurs while running the
     * test suite
     */
    public static final String ERROR_RUNNING_TEST_SUITE 
        = "XMLTestSuiteRunner.messages.error.running.test.suite";

    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println(Messages.formatMessage(USAGE, null));
            System.exit(0);
        }
        
        String uriStr = args[0];
        String[] ids = new String[args.length - 1];
        System.arraycopy(args, 1, ids, 0, args.length-1);

        File file = new File(uriStr);
        URL url = null;
        if(file.exists()){
            try {
                url = file.toURL();
            }catch(MalformedURLException e){
                System.err.println(Messages.formatMessage(COULD_NOT_CONVERT_FILE_NAME_TO_URI,
                                                            new Object[]{uriStr}));
                System.exit(0);
            }
        }
        
        else {
            System.err.println(Messages.formatMessage(NOT_A_FILE_TRY_URI,
                                                        new Object[]{uriStr}));
            try{
                url = new URL(uriStr);
            }catch(MalformedURLException e){
                System.err.println(Messages.formatMessage(INVALID_URI,
                                                          new Object[]{uriStr}));
                System.exit(0);
            }
        }

        Document doc = null;

        try{
            System.err.println("Loading document ...");

            DocumentBuilder docBuilder
                = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            doc = docBuilder.parse(url.toString());
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(Messages.formatMessage(INVALID_DOCUMENT,
                                                      new Object[] { uriStr,
                                                                     e.getClass().getName(),
                                                                     e.getMessage() }));
            System.exit(0);
        }
        
        try{
            System.err.println("Running test run...");
            XMLTestSuiteRunner r = new XMLTestSuiteRunner();
            r.run(doc, ids);
        }catch(TestException e){
            System.err.println(Messages.formatMessage(ERROR_RUNNING_TEST_SUITE,
                                                      new Object[] { e.getMessage() }));
            System.exit(0);
        }

        System.exit(1);
        
    }
}
