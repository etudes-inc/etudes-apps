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

/**
 * AssessmentGrading contain the details of how submissions to an assessment are graded.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentGrading {
	/** if student identities are invisible to the grader when grading */
	protected boolean anonymous = false;

	/** if submissions are to be considered graded as soon as submitted (based only on the auto-scoring) */
	protected boolean autoRelease = true;

	/** if grades are to be sent to a gradebook */
	protected boolean gradebookIntegration = false;

	/** if the assessment was rejected as an entry in a gradebook */
	protected boolean gradebookRejectedAssessment = false;

	/**
	 * {@inheritDoc}
	 */
	public void setGradebookIntegration(boolean setting) {
		this.gradebookIntegration = setting;

		// clear the invalid flag if we are no longer integrated
		if (!this.gradebookIntegration) {
			this.gradebookRejectedAssessment = false;
		}
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(AssessmentGrading other) {
		this.autoRelease = other.autoRelease;
		this.gradebookIntegration = other.gradebookIntegration;
		this.gradebookRejectedAssessment = other.gradebookRejectedAssessment;
		this.anonymous = other.anonymous;
	}
}
