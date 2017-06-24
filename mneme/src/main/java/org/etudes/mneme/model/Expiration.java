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

/**
 * Expiration models information about the time a submission or assessment expires and can no longer be worked upon.
 */
@Data
@AllArgsConstructor
public class Expiration {
	/**
	 * The possible expiration causes.
	 */
	enum Cause {
		closedDate, timeLimit
	}

	/** The cause of the expiration. */
	protected final Cause cause;

	/** The duration, in ms, till expiration. */
	protected final long duration;

	/** The time limit that is set for this submission. */
	protected final long limit;

	/** the time (Date) that marks the expiration date. */
	protected final Date time;
}
