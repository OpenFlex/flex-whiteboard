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
import org.apache.flex.compiler.internal.as.codegen.TestFieldMembers;
import org.apache.flex.compiler.internal.js.driver.goog.GoogBackend;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests the production of 'goog' JavaScript for Class Field members.
 * 
 * @author Michael Schmalle
 * @author Erik de Bruin
 */
public class TestGoogFieldMembers extends TestFieldMembers
{
    //--------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------

    @Override
    @Test
    public void testField()
    {
    	// TODO (erikdebruin) a variable without an explicit namespace in an AS 
    	//                    class is treated as being 'internal' (visible only 
    	//                    in within the package): is there a 'goog' 
    	//                    equivalent for the 'internal' namespace?
        IVariableNode node = getField("var foo;");
        visitor.visitVariable(node);
        assertOut("/**\n * @type {*}\n */\nA.prototype.foo");
    }

    @Override
    @Test
    public void testField_withType()
    {
        IVariableNode node = getField("var foo:int;");
        visitor.visitVariable(node);
        assertOut("/**\n * @type {number}\n */\nA.prototype.foo");
    }

    @Override
    @Test
    public void testField_withTypeValue()
    {
        IVariableNode node = getField("var foo:int = 420;");
        visitor.visitVariable(node);
        assertOut("/**\n * @type {number}\n */\nA.prototype.foo = 420");
    }

    @Override
    @Test
    public void testField_withNamespaceTypeValue()
    {
        IVariableNode node = getField("private var foo:int = 420;");
        visitor.visitVariable(node);
        assertOut("/**\n * @private\n * @type {number}\n */\nA.prototype.foo = 420");
    }

    @Override
    @Test
    public void testField_withCustomNamespaceTypeValue()
    {
        IVariableNode node = getField("mx_internal var foo:int = 420;");
        visitor.visitVariable(node);
        // (erikdebruin) we ignore custom namespaces completely (are there side effects I'm missing?)
        assertOut("/**\n * @type {number}\n */\nA.prototype.foo = 420");
    }

    @Override
    @Test
    public void testField_withNamespaceTypeCollection()
    {
        IVariableNode node = getField("protected var foo:Vector.<Foo>;");
        visitor.visitVariable(node);
        assertOut("/**\n * @protected\n * @type {Array.<Foo>}\n */\nA.prototype.foo");
    }

    @Override
    @Ignore
    @Test
    public void testField_withNamespaceTypeCollectionComplex()
    {
    	// TODO (erikdebruin) not sure how to annotate this using 'goog'
        IVariableNode node = getField("protected var foo:Vector.<Vector.<Vector.<Foo>>>;");
        visitor.visitVariable(node);
        assertOut("");
    }

    @Override
    @Test
    public void testField_withNamespaceTypeValueComplex()
    {
        IVariableNode node = getField("protected var foo:Foo = new Foo('bar', 42);");
        visitor.visitVariable(node);
        assertOut("/**\n * @protected\n * @type {Foo}\n */\nA.prototype.foo = new Foo('bar', 42)");
    }

    @Override
    @Test
    public void testField_withList()
    {
        IVariableNode node = getField("protected var a:int = 4, b:int = 11, c:int = 42;");
        visitor.visitVariable(node);
        assertOut("/**\n * @protected\n * @type {number}\n */\nA.prototype.a = 4;\n\n/**\n * @protected\n * @type {number}\n */\nA.prototype.b = 11;\n\n/**\n * @protected\n * @type {number}\n */\nA.prototype.c = 42");
    }

    //--------------------------------------------------------------------------
    // Constants
    //--------------------------------------------------------------------------

    @Override
    @Test
    public void testConstant()
    {
        IVariableNode node = getField("static const foo;");
        visitor.visitVariable(node);
        assertOut("/**\n * @const\n * @type {*}\n */\nA.foo");
    }

    @Override
    @Test
    public void testConstant_withType()
    {
        IVariableNode node = getField("static const foo:int;");
        visitor.visitVariable(node);
        assertOut("/**\n * @const\n * @type {number}\n */\nA.foo");
    }

    @Override
    @Test
    public void testConstant_withTypeValue()
    {
        IVariableNode node = getField("static const foo:int = 420;");
        visitor.visitVariable(node);
        assertOut("/**\n * @const\n * @type {number}\n */\nA.foo = 420");
    }

    @Override
    @Test
    public void testConstant_withNamespaceTypeValue()
    {
        IVariableNode node = getField("private static const foo:int = 420;");
        visitor.visitVariable(node);
        assertOut("/**\n * @private\n * @const\n * @type {number}\n */\nA.foo = 420");
    }

    @Override
    @Test
    public void testConstant_withCustomNamespaceTypeValue()
    {
        IVariableNode node = getField("mx_internal static const foo:int = 420;");
        visitor.visitVariable(node);
        // (erikdebruin) we ignore custom namespaces completely (are there side effects I'm missing?)
        assertOut("/**\n * @const\n * @type {number}\n */\nA.foo = 420");
    }

    @Override
    protected IBackend createBackend()
    {
        return new GoogBackend();
    }
}
