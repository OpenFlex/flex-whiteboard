/*

   Copyright 2002  The Apache Software Foundation 

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
package org.apache.flex.forks.batik.css.parser;

import org.w3c.flex.forks.css.sac.CombinatorCondition;
import org.w3c.flex.forks.css.sac.Condition;

/**
 * This class provides an abstract implementation of the
 * {@link org.w3c.flex.forks.css.sac.CombinatorCondition} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractCombinatorCondition.java,v 1.3 2004/08/18 07:13:02 vhardy Exp $
 */

public abstract class AbstractCombinatorCondition
    implements CombinatorCondition {

    /**
     * The first condition.
     */
    protected Condition firstCondition;

    /**
     * The second condition.
     */
    protected Condition secondCondition;

    /**
     * Creates a new CombinatorCondition object.
     */
    protected AbstractCombinatorCondition(Condition c1, Condition c2) {
	firstCondition = c1;
	secondCondition = c2;
    }

    /**
     * <b>SAC</b>: Implements {@link CombinatorCondition#getFirstCondition()}.
     */    
    public Condition getFirstCondition() {
	return firstCondition;
    }

    /**
     * <b>SAC</b>: Implements {@link CombinatorCondition#getSecondCondition()}.
     */
    public Condition getSecondCondition() {
	return secondCondition;
    }
}
