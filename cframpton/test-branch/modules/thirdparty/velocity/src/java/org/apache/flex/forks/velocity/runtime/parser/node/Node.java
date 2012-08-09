/*
 * Copyright 2000-2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *  Modified by Adobe Flex.
 */

package org.apache.flex.forks.velocity.runtime.parser.node;

import java.io.Serializable;
import java.io.Writer;
import java.io.IOException;

import org.apache.flex.forks.velocity.context.InternalContextAdapter;
import org.apache.flex.forks.velocity.runtime.parser.Token;

import org.apache.flex.forks.velocity.exception.MethodInvocationException;
import org.apache.flex.forks.velocity.exception.ParseErrorException;
import org.apache.flex.forks.velocity.exception.ResourceNotFoundException;

/**
 *  All AST nodes must implement this interface.  It provides basic
 *  machinery for constructing the parent and child relationships
 *  between nodes. 
 */

public interface Node extends Serializable
{

    /**
     *  This method is called after the node has been made the current
     *  node.  It indicates that child nodes can now be added to it. 
     */
    public void jjtOpen();

    /** 
     *  This method is called after all the child nodes have been
     *  added. 
     */
    public void jjtClose();

    /** 
     *  This pair of methods are used to inform the node of its
     *  parent. 
     */
    public void jjtSetParent(Node n);
    public Node jjtGetParent();

    /** 
     *  This method tells the node to add its argument to the node's
     *   list of children.  
     */
    public void jjtAddChild(Node n, int i);

    /** 
     *  This method returns a child node.  The children are numbered
     *  from zero, left to right. 
     */
    public Node jjtGetChild(int i);

    /** Return the number of children the node has. */
    public int jjtGetNumChildren();

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data);

    public Object childrenAccept(ParserVisitor visitor, Object data);

    // added
    public String getFirstTokenImage();
    public Token getFirstToken();
    public Token getLastToken();
    public int getType();

    public Object init( InternalContextAdapter context, Object data) throws Exception;

    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException;

    public Object value( InternalContextAdapter context)
        throws MethodInvocationException;

    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException, ParseErrorException, ResourceNotFoundException;

    public Object execute(Object o, InternalContextAdapter context)
      throws MethodInvocationException;

    public void setInfo(int info);
    public int getInfo();

    public String literal();
    public void setInvalid();
    public boolean isInvalid();
    public int getLine();
    public int getColumn();
}
