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
 * GradingOptions contain the details of how an assessment is to be graded.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GradingOptions {
	/** if student identities are invisible to the grader when grading */
	protected boolean anonymous = false;

	/** if submissions are to be considered graded as soon as submitted (based only on the auto-scoring) */
	protected boolean autoRelease = true;

	/** if grades are to be sent to a gradebook */
	protected boolean forGradebook = false;

	/** if the assessment was rejected as an entry in a gradebook */
	protected boolean rejectedByGradebook = false;

	/**
	 * Set the forGradebook flag, clearing the rejected flag if we are no longer for gradebook.
	 * 
	 * @param setting
	 *            The forGradebook flag.
	 */
	public void setForGradebook(boolean setting) {
		this.forGradebook = setting;

		// clear the invalid flag if we are no longer integrated
		if (!this.forGradebook) {
			this.rejectedByGradebook = false;
		}
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public GradingOptions set(GradingOptions other) {
		this.autoRelease = other.autoRelease;
		this.forGradebook = other.forGradebook;
		this.rejectedByGradebook = other.rejectedByGradebook;
		this.anonymous = other.anonymous;

		return this;
	}
}
