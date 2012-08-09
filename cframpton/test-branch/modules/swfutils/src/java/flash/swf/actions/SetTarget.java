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

package flash.swf.actions;

import flash.swf.Action;
import flash.swf.ActionHandler;
import flash.swf.ActionConstants;

/**
 * Represents an AS2 "set target" byte code.
 *
 * @author Clement Wong
 */
public class SetTarget extends Action
{
	public SetTarget()
	{
		super(ActionConstants.sactionSetTarget);
	}

    public void visit(ActionHandler h)
	{
		h.setTarget(this);
	}

    /**
	 * name of target object for subsequent actions.  Empty string ("") means
	 * the current movie.
	 */
	public String targetName;

    public boolean equals(Object object)
    {
        boolean isEqual = false;

        if (super.equals(object) && (object instanceof SetTarget))
        {
            SetTarget setTarget = (SetTarget) object;

            if ( equals(setTarget.targetName, this.targetName))
            {
                isEqual = true;
            }
        }

        return isEqual;
    }
}
