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
import java.util.Optional;

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

	/** The due date. Submissions after this date are considered late, if they are accepted at all. */
	protected Optional<Date> due = Optional.empty();

	/** If the assessment should be hidden from students until open. */
	protected boolean hideUntilOpen = false;

	/** The open date. Only after this date (if defined) is the assessment open for submission. If not defined, it is open when published. */
	protected Optional<Date> open = Optional.empty();

	/** The date after which submissions will not be accepted - if empty, submissions will be accepted while published. */
	protected Optional<Date> until = Optional.empty();

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Schedule set(Schedule other) {
		this.due = other.getDue();
		this.hideUntilOpen = other.hideUntilOpen;
		this.open = other.getOpen();
		this.until = other.getUntil();

		return this;
	}
}
