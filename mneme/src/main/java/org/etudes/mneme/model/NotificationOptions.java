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
 * NotificationOptions contains the details related to notifications at assessment close.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationOptions {

	/** If a notification should be sent when the assessment closes... ??? */
	protected boolean notifyEval = false;

	/** Who to send the assessment results to. */
	protected String resultsEmail = null;

	/**
	 * Set from another.
	 * 
	 * @param other
	 *            The other.
	 * @return this (for chaining).
	 */
	public NotificationOptions set(NotificationOptions other) {
		this.notifyEval = other.isNotifyEval();
		this.resultsEmail = other.getResultsEmail();

		return this;
	}
}
