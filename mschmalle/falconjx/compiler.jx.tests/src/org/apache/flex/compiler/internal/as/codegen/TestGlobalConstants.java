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

package org.apache.flex.compiler.internal.as.codegen;

import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.junit.Test;

/**
 * @author Erik de Bruin
 */
public class TestGlobalConstants extends TestWalkerBase
{
    @Test
    public void testInfinity()
    {
        IVariableNode node = getField("var a:Number = Infinity;");
        visitor.visitVariable(node);
        assertOut("var a:Number = Infinity");
    }

    @Test
    public void testNegativeInfinity()
    {
        IVariableNode node = getField("var a:Number = -Infinity;");
        visitor.visitVariable(node);
        assertOut("var a:Number = -Infinity");
    }

    @Test
    public void testNaN()
    {
        IVariableNode node = getField("var a:Number = NaN;");
        visitor.visitVariable(node);
        assertOut("var a:Number = NaN");
    }

    @Test
    public void testUndefined()
    {
        IVariableNode node = getField("var a:* = undefined;");
        visitor.visitVariable(node);
        assertOut("var a:* = undefined");
    }

    protected IVariableNode getField(String code)
    {
        String source = "package {public class A {" + code + "}}";
        IFileNode node = getFileNode(source);
        IVariableNode child = (IVariableNode) findFirstDescendantOfType(node,
                IVariableNode.class);
        return child;
    }
}
