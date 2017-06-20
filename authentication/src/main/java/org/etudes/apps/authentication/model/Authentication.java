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

package org.etudes.apps.authentication.model;

import java.util.Date;

import org.etudes.apps.user.model.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication models a user authentication event to an app.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {

	/** The authentication id - can be used as a token to authenticate-by-token. */
	@JsonProperty("id")
	protected Long _id;

	/** The user's browser user agent code. */
	protected Long browser;

	/** The context (course, account) LMS id related to this authentication. */
	protected String context;

	/** Date authenticated. */
	protected Date date;
	
	/** The user's IP address. */
	protected /* byte[] */ String ipAddress;

	/** The placement id related to this authentication. */
	protected String placement;

	/** The user's role in the context. */
	protected String role;

	/** The user. */
	protected User user;

	@JsonIgnore
	public Long getContextL() {
		try {
			return Long.valueOf(context);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
}
