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

package flash.tools.debugger;
import java.io.IOException;

/**
 * AlreadyActiveApplicationException is thrown when run/debug the application while there is 
 * an already running application. 
 * Exception is detected with ADL exit code 1 (Successful invocation of an already running AIR application. ADL exits immediately.)
 * 
 * @author sakkus
 */
public class AlreadyActiveApplicationException extends IOException {
	private static final long serialVersionUID = 0L;
	
	private boolean m_isDebugging;
	
	public AlreadyActiveApplicationException (String detailedMessage,boolean isDebugging)
	{
		super(detailedMessage);
		m_isDebugging=isDebugging;
	}
	
    public String getMessage()
	{
    	if(m_isDebugging) 	//DEBUGGING
    		return Bootstrap.getLocalizationManager().getLocalizedTextString("maybeAlreadyRunningForDebug"); //$NON-NLS-1$
    	else    			//RUNNING
    		return Bootstrap.getLocalizationManager().getLocalizedTextString("maybeAlreadyRunningForRun"); //$NON-NLS-1$
	}
}
