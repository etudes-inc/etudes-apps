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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * AssessmentStatus contain the status flags for an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AssessmentStatus {

	protected boolean published = false;
	protected boolean valid = true;

	/**
	 * Set from another.
	 * 
	 * @param other
	 *            The other.
	 * @return this (for chaining).
	 */
	public AssessmentStatus set(AssessmentStatus other) {
		this.published = other.isPublished();
		this.valid = other.isValid();

		return this;
	}
}
