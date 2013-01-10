/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.flex.compiler.internal.js.codegen.goog;

import org.apache.flex.compiler.clients.IBackend;
import org.apache.flex.compiler.internal.as.codegen.TestStatements;
import org.apache.flex.compiler.internal.js.driver.goog.GoogBackend;
import org.apache.flex.compiler.internal.tree.as.LabeledStatementNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.ITryNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Michael Schmalle
 * @author Erik de Bruin
 */
public class TestGoogStatements extends TestStatements
{
    //----------------------------------
    // var declaration
    //----------------------------------

    @Override
    @Test
    public void testVarDeclaration()
    {
        IVariableNode node = (IVariableNode) getNode("var a;", IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("var /** @type {*} */ a");
    }

    @Override
    @Test
    public void testVarDeclaration_withType()
    {
        IVariableNode node = (IVariableNode) getNode("var a:int;", IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("var /** @type {number} */ a");
    }

    @Override
    @Test
    public void testVarDeclaration_withTypeAssignedValue()
    {
        IVariableNode node = (IVariableNode) getNode("var a:int = 42;",
                IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("var /** @type {number} */ a = 42");
    }

    @Override
    @Test
    public void testVarDeclaration_withTypeAssignedValueComplex()
    {
        IVariableNode node = (IVariableNode) getNode(
                "var a:Foo = new Foo(42, 'goo');", IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("var /** @type {Foo} */ a = new Foo(42, 'goo')");
    }

    @Override
    @Test
    public void testVarDeclaration_withList()
    {
        IVariableNode node = (IVariableNode) getNode(
                "var a:int = 4, b:int = 11, c:int = 42;", IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("var /** @type {number} */ a = 4, /** @type {number} */ b = 11, /** @type {number} */ c = 42");
    }

    //----------------------------------
    // const declaration
    //----------------------------------

    @Override
    @Test
    public void testConstDeclaration()
    {
        IVariableNode node = (IVariableNode) getNode("const a = 42;",
                IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("\n/**\n * @const\n * @type {*}\n */\na = 42");
    }

    @Override
    @Test
    public void testConstDeclaration_withType()
    {
        IVariableNode node = (IVariableNode) getNode("const a:int = 42;",
                IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("\n/**\n * @const\n * @type {number}\n */\na = 42");
    }

    @Override
    @Test
    public void testConstDeclaration_withList()
    {
        IVariableNode node = (IVariableNode) getNode(
                "const a:int = 4, b:int = 11, c:int = 42;", IVariableNode.class);
        visitor.visitVariable(node);
        assertOut("\n/**\n * @const\n * @type {number}\n */\na = 4, \n/**\n * @const\n * @type {number}\n */\nb = 11, \n/**\n * @const\n * @type {number}\n */\nc = 42");
    }

    //----------------------------------
    // for () { }
    //----------------------------------

    @Override
    @Test
    public void testVisitFor_1a()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int = 0; i < len; i++) { break; }",
                IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for (var /** @type {number} */ i = 0; i < len; i++) {\n\tbreak;\n}");
    }

    @Override
    @Test
    public void testVisitFor_1b()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int = 0; i < len; i++) break;", IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for (var /** @type {number} */ i = 0; i < len; i++)\n\tbreak;");
    }

    @Override
    @Test
    public void testVisitForIn_1()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int in obj) { break; }", IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for (var /** @type {number} */ i in obj) {\n\tbreak;\n}");
    }

    @Override
    @Test
    public void testVisitForIn_1a()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int in obj)  break; ", IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for (var /** @type {number} */ i in obj)\n\tbreak;");
    }

    @Ignore
    @Override
    @Test
    public void testVisitForEach_1()
    {
    	// TODO (erikdebruin) handle workaround for "for-each" loop
        IForLoopNode node = (IForLoopNode) getNode(
                "for each(var i:int in obj) { break; }", IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for each (var i:int in obj) {\n\tbreak;\n}");
    }

    @Ignore
    @Override
    @Test
    public void testVisitForEach_1a()
    {
    	// TODO (erikdebruin) handle workaround for "for-each" loop
        IForLoopNode node = (IForLoopNode) getNode(
                "for each(var i:int in obj)  break; ", IForLoopNode.class);
        visitor.visitForLoop(node);
        assertOut("for each (var i:int in obj)\n\tbreak;");
    }

    //----------------------------------
    // try {} catch () {} finally {}
    //----------------------------------

    @Override
    @Test
    public void testVisitTry_Catch()
    {
        ITryNode node = (ITryNode) getNode("try { a; } catch (e:Error) { b; }",
                ITryNode.class);
        visitor.visitTry(node);
        assertOut("try {\n\ta;\n} catch (e) {\n\tb;\n}");
    }

    @Override
    @Test
    public void testVisitTry_Catch_Finally()
    {
        ITryNode node = (ITryNode) getNode(
                "try { a; } catch (e:Error) { b; } finally { c; }",
                ITryNode.class);
        visitor.visitTry(node);
        assertOut("try {\n\ta;\n} catch (e) {\n\tb;\n} finally {\n\tc;\n}");
    }

    @Override
    @Test
    public void testVisitTry_Catch_Catch_Finally()
    {
        ITryNode node = (ITryNode) getNode(
                "try { a; } catch (e:Error) { b; } catch (f:Error) { c; } finally { d; }",
                ITryNode.class);
        visitor.visitTry(node);
        assertOut("try {\n\ta;\n} catch (e) {\n\tb;\n} catch (f) {\n\tc;\n} finally {\n\td;\n}");
    }

    @Override
    @Test
    public void testVisitTry_CatchEmpty_FinallyEmpty_()
    {
        ITryNode node = (ITryNode) getNode(
                "try { a; } catch (e:Error) {  } finally {  }", ITryNode.class);
        visitor.visitTry(node);
        assertOut("try {\n\ta;\n} catch (e) {\n} finally {\n}");
    }

    //----------------------------------
    // label : for () {}
    //----------------------------------

    @Override
    @Test
    public void testVisitLabel_1()
    {
        LabeledStatementNode node = (LabeledStatementNode) getNode(
                "foo: for each(var i:int in obj) { break foo; }",
                LabeledStatementNode.class);
        visitor.visitLabeledStatement(node);
        assertOutDebug("foo : for each (var /** @type {number} */ i in obj) {\n\tbreak foo;\n}");
    }

    @Override
    @Test
    public void testVisitLabel_1a()
    {
        // TODO LabelStatement messes up in finally{} block, something is wrong there
        LabeledStatementNode node = (LabeledStatementNode) getNode(
                "foo: for each(var i:int in obj) break foo;",
                LabeledStatementNode.class);
        visitor.visitLabeledStatement(node);
        assertOutDebug("foo : for each (var /** @type {number} */ i in obj)\n\tbreak foo;");
    }

    //----------------------------------
    // all together now!
    //----------------------------------

    @Ignore
    @Override
    @Test
    public void testVisit()
    {
        IFileNode node = (IFileNode) getNode(
                "try { a; } catch (e:Error) { if (a) { if (b) { if (c) b; else if (f) a; else e; }} } finally {  }"
                        + "if (d) for (var i:int = 0; i < len; i++) break;"
                        + "if (a) { with (ab) { c(); } "
                        + "do {a++;do a++; while(a > b);} while(c > d); }"
                        + "if (b) { try { a; throw new Error('foo'); } catch (e:Error) { "
                        + " switch(i){case 1: break; default: return;}"
                        + " } catch (f:Error) { c; eee.dd; } finally { "
                        + "  d;  var a:Object = function(foo:int, bar:String = 'goo'):int{return -1;};"
                        + "  eee.dd; eee.dd; eee.dd; eee.dd;} }"
                        + "foo: for each(var i:int in obj) break foo;",
                IFileNode.class);
        visitor.visitFile(node);
        assertOut("");
    }


    protected IBackend createBackend()
    {
        return new GoogBackend();
    }

}
