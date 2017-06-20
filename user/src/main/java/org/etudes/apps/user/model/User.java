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

package org.etudes.apps.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	// final static private Logger logger = LoggerFactory.getLogger(User.class);

	/** The internal user id */
	@JsonProperty("id")
	protected Long _id;

	/** User's email address. */
	protected String email;

	/** User's family name. */
	protected String familyName;

	/** User's given name. */
	protected String givenName;

	/** The user's LMS ID. Very few users (i.e Etudes staff) may not have one, but most do, so I'm leaving this String not Optional. */
	protected String lmsId;

	/** "loginSet" is include in the json but ignored coming back. */
	@Setter(AccessLevel.NONE)
	protected boolean loginSet;

	/** User's LMS SIS id. */
	protected String sisId;

	/** The subscription this user is part of. */
	protected Long subscriptionId;

	/** "tokensSet" is include in the json but ignored coming back. */
	@Setter(AccessLevel.NONE)
	protected boolean tokensSet;

	/**
	 * Construct with the non-optional fields.
	 * 
	 * @param id
	 *            The user id.
	 * @param lmsId
	 *            The user's LMS id.
	 * @param subscription
	 *            The subscription id.
	 */
	public User(Long id, String lmsId, Long subscription) {
		this._id = id;
		this.lmsId = lmsId;
		this.subscriptionId = subscription;
	}

	/**
	 * Access the lmsId as a long.
	 * 
	 * @return The long value of the lmsId, or 0 if it is not numeric.
	 */
	public Long getLmsIdL() {
		try {
			return Long.valueOf(lmsId);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
}
