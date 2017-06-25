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
 * DeliveryOptions contain the assessment settings related to delivering the assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DeliveryOptions {
	
	/** If an honor pledge is required to participate. */
	protected boolean honorPledge = false;

	/** The assessment password. */
	protected AssessmentPassword password = null;

	/** How to group questions when presenting the assessment. */
	protected QuestionGrouping questionGrouping = QuestionGrouping.question;

	/** If the questions are allowed to be accessed in any order. If not, they are presented linearly. */
	protected boolean randomAccess = true;

	/** If hints should be shown when delivering the assessment. */
	protected boolean showHints = false;

	/** If model answers should be shown when delivering the assessment. */
	protected boolean showModelAnswer = true;

	/** If the parts' shuffle choices setting should be overridden at the assessment level ... ??? */
	protected boolean shuffleChoicesOverride = false;

	/** Time limit for taking the assessment, if any. */
	protected Long timeLimit = null;

	/** Tries limit for the assessment, if any. */
	protected Integer tries = Integer.valueOf(1);
}
