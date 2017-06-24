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

import org.etudes.mneme.api.ReviewShowCorrect;
import org.etudes.mneme.api.ReviewTiming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssessmentReview contain the details of how submissions can be reviewed.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentReview {

	/** the review date (for timing=BY_DATE) after which submissions may be reviewed */
	protected Date date = null;

	/** ReviewShowCorrect setting. */
	protected ReviewShowCorrect showCorrectAnswer = ReviewShowCorrect.yes;

	/** if review includes the authored correct / incorrect feedback */
	protected boolean showFeedback = true;

	/** if review includes summary of data visible to students. */
	protected boolean showSummary = true;

	/** when review can happen */
	protected ReviewTiming timing = ReviewTiming.submitted;

	/**
	 * @return if review is enabled right now for this assessment, considering when now is and all the review settings.
	 */
	public boolean isNowAvailable() {
		// if review timing is date, we can tell without a submission
		if (this.timing == ReviewTiming.date) {
			if (this.date != null) {
				// we can now review if NOW is after the review date
				return new Date().after(this.date);
			}

			// no date? no review
			return false;
		}

		// TODO: need the submission context ... pass in?
		// // otherwise we need a submission
		// if (this.assessment.getSubmissionContext() == null)
		// return Boolean.FALSE;
		//
		// // for submitted
		// if (this.timing == ReviewTiming.submitted) {
		// return this.assessment.getSubmissionContext().getIsComplete();
		// }
		//
		// // for graded
		// if (this.timing == ReviewTiming.graded) {
		// return this.assessment.getSubmissionContext().getIsReleased();
		// }

		return false;
	}

	/**
	 * @return validity: the review date, if defined, must be after the assessment is finally closed
	 */
	public boolean isValid() {
		if (getDate() == null)
			return true;

		// TODO: needs the dates to compare with - pass in?
		// Date openDate = this.assessment.dates.getOpenDate();
		// Date dueDate = this.assessment.dates.getDueDate();
		// Date acceptUntilDate = this.assessment.dates.getAcceptUntilDate();
		//
		// if (openDate != null && getDate().before(openDate))
		// return Boolean.FALSE;
		// if (dueDate != null && getDate().before(dueDate))
		// return Boolean.FALSE;
		// if (acceptUntilDate != null && getDate().before(acceptUntilDate))
		// return Boolean.FALSE;

		return true;
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(AssessmentReview other) {
		this.date = other.date;
		this.showCorrectAnswer = other.showCorrectAnswer;
		this.showFeedback = other.showFeedback;
		this.showSummary = other.showSummary;
		this.timing = other.timing;
	}
}
