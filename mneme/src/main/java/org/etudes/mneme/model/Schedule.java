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

import org.etudes.mneme.api.MnemeService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Schedule contain the various dates related to an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Schedule {

	/** The number of ms we allow answers and completions of submissions after hard deadlines. */
	static final long GRACE = 2 * 60 * 1000;

	/** The date the assessment was archived. */
	protected Date archived = null;

	/** The due date. Submissions after this date are considered late, if they are accepted at all. */
	protected Date due = null;

	/** If the assessment should be hidden from students until open. */
	protected boolean hideUntilOpen = false;

	/** The open date. Only after this date (if defined) is the assessment open for submission. If not defined, it is open when published. */
	protected Date open = null;

	/** The date after which submissions will not be accepted - if null, submissions will continue to be accepted. */
	protected Date until = null;

	/**
	 * @return The number of ms from now that the due date on this assessment will be reached, 0 if it has already been reached, or null if it has no due date.
	 */
	public Long getDurationTillDue() {
		// if no due date
		if (getDue() == null)
			return null;

		// if we have started, the clock is running - compute how long from NOW the end is
		Date now = new Date();
		long tillDue = getDue().getTime() - now.getTime();
		if (tillDue <= 0)
			return new Long(0);

		return new Long(tillDue);
	}

	/**
	 * @return The expiration information for the assessment.
	 */
	public Expiration getExpiration() {
		// see if the assessment has a hard due date (no submissions allowed)
		Date closedDate = this.getSubmitUntil();
		if (closedDate == null)
			return null;

		// compute an end time based on the assessment's closed date
		Date now = new Date();

		// if we are past it already
		if (closedDate.before(now))
			return null;

		// the closeDate is the end time
		long endTime = closedDate.getTime();

		// if this closed date is more than 2 hours from now, ignore it and say we have no expiration
		if (endTime > now.getTime() + (2l * 60l * 60l * 1000l))
			return null;

		// set the limit to 2 hours
		final long limit = 2l * 60l * 60l * 1000l;

		// how long from now till endTime?
		long tillExpires = endTime - now.getTime();
		if (tillExpires <= 0)
			tillExpires = 0;

		return new Expiration(Expiration.Cause.closedDate, tillExpires, limit, closedDate);
	}

	/**
	 * @return the date after which submissions are not allowed. Computed based on due and accept-until dates.
	 */
	public Date getSubmitUntil() {
		// this is the acceptUntil date, if defined, or the due date.
		Date closedDate = getUntil();
		if (closedDate == null)
			closedDate = getDue();
		return closedDate;
	}

	/**
	 * @param assessment
	 *            The assessment these dates are for.
	 * @return if the assessment is closed for submissions - unpublished, archived, not yet open or past submit-until date.
	 */
	public boolean isClosed(Assessment assessment) {
		// if (this.assessment.getArchived())
		// return Boolean.TRUE;
		// if (!this.assessment.getPublished())
		// return Boolean.TRUE;
		// if (this.assessment.getFrozen())
		// return Boolean.TRUE;
		// TODO:

		// if there is no end to submissions, we are never closed
		if (getSubmitUntil() == null)
			return false;

		// we are closed if after the submit until date
		Date now = new Date();
		if (now.after(getSubmitUntil()))
			return true;

		return false;
	}

	/**
	 * @return if we are now between due and accept until dates.
	 */
	public boolean isLate() {
		Date acceptUntil = getUntil();
		Date due = getDue();
		if ((acceptUntil != null) && (due != null)) {
			Date now = new Date();
			return (now.after(due) && now.before(acceptUntil));
		}

		return false;
	}

	/**
	 * @param assessment
	 *            The assessment these dates are for.
	 * @param withGrace
	 *            if we should consider the grace period.
	 * @return if the assessment is open for submissions - published, not archived, past open date, before submit-until date.
	 */
	public boolean isOpen(Assessment assessment, boolean withGrace) {
		// if (this.assessment.getArchived())
		// return Boolean.FALSE;
		// if (!this.assessment.getPublished())
		// return Boolean.FALSE;
		// if (this.assessment.getFrozen())
		// return Boolean.FALSE;
		// TODO:

		Date now = new Date();
		long grace = withGrace ? MnemeService.GRACE : 0l;

		// if we have an open date and we are not there yet
		if ((getOpen() != null) && (now.before(getOpen())))
			return Boolean.FALSE;

		// if we have a submit-until date and we are past it, considering grace
		if ((getSubmitUntil() != null) && (now.getTime() > (getSubmitUntil().getTime() + grace)))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	/**
	 * @return if the assessment dates are valid; i.e. has no inconsistencies in the definition.
	 */
	public boolean isValid() {
		// open, if defined, must be before acceptUntil and due, if defined
		if ((getOpen() != null) && (getDue() != null) && (!getOpen().before(getDue())))
			return false;
		if ((getOpen() != null) && (getUntil() != null) && (!getOpen().before(getUntil())))
			return false;

		// due, if defined, must be not after acceptUntil, if defined
		if ((getDue() != null) && (getUntil() != null) && (getDue().after(getUntil())))
			return false;

		return true;
	}

	// TODO: need to enforce: setAcceptUntilDate cannot be made to locked assessments if set to a formal course evaluation
	// TODO: need to enforce: setDueDate cannot be made to locked assessments if set to a formal course evaluation

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Schedule set(Schedule other) {
		this.until = other.until;
		this.archived = other.archived;
		this.due = other.due;
		this.open = other.open;
		this.hideUntilOpen = other.hideUntilOpen;

		return this;
	}
}
