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
 * DeliveryOptions contain the assessment settings related to delivering the assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DeliveryOptions {

	/**
	 * QuestionGrouping enumerates the different options for grouping questions for display at delivery.
	 */
	public enum QuestionGrouping {
		question, part, assessment
	}

	/** If an honor pledge is required to participate. */
	protected boolean honorPledge = false;

	/** The assessment password. */
	protected Password password = null;

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

	/**
	 * Set from another.
	 * 
	 * @param other
	 *            The other.
	 * @return this (for chaining).
	 */
	public DeliveryOptions set(DeliveryOptions other) {
		this.honorPledge = other.honorPledge;
		this.password = new Password().set(other.getPassword());
		this.questionGrouping = other.questionGrouping;
		this.randomAccess = other.randomAccess;
		this.showHints = other.showHints;
		this.showModelAnswer = other.showModelAnswer;
		this.shuffleChoicesOverride = other.shuffleChoicesOverride;
		this.timeLimit = other.timeLimit;
		this.tries = other.tries;

		return this;
	}
}
