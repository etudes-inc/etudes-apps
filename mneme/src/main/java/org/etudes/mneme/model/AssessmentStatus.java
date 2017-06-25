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

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssessmentStatus contain the status flags of an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentStatus {

	/** If the assessment is archives. */
	protected boolean archived = false;

	/** When the assessment evaluation report was last sent. */
	protected Date evaluationSent = null;

	/** If this is a formal course evaluation assessment. */
	protected boolean formalCourseEval = false;

	/** If the assessment is "frozen"... ??? */
	protected boolean frozen = false;

	/** If the assessment is "live" ... ???. */
	protected boolean live = false;

	/** If the assessment is "locked" ... ??? */
	protected boolean locked = false;

	/** If the assessment has just been created, but has no user changes yet. */
	protected boolean mint = true;

	/** If the assessment is published. */
	protected boolean published = false;

	/** Date the results were last sent. */
	protected Date resultsSent = null;
}
