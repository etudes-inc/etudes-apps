/**********************************************************************************
 *
 * Copyright (c) 2017 Etudes, Inc.
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
 *
 **********************************************************************************/

package org.etudes.mneme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Changeable tracks changes for an object.
 */
@Data
@NoArgsConstructor
public class Changeable {
	protected boolean changed = false;

	/**
	 * Construct from another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	public Changeable(Changeable other) {
		this.changed = other.isChanged();
	}

	/**
	 * Clear the changed state.
	 */
	public void clearChanged() {
		this.changed = false;
	}

	/**
	 * Set the changed state.
	 */
	public void setChanged() {
		this.changed = true;
	}
}
