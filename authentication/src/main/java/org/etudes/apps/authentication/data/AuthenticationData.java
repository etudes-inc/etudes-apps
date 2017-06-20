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

package org.etudes.apps.authentication.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AuthenticationData {

	/**
	 * Create a new authentication.
	 * 
	 * @return A newly created (and remembered) authentication.
	 */
	Optional<Authentication> createAuthentication(User user, String ipAddress, Long browser, String context, String role, String placement, Date date);

	/**
	 * Create if needed a browser record for this browser user agent string.
	 * 
	 * @param browserUserAgent
	 *            The browser user agent string.
	 * @return The id of the browser record for this agent string, created or existing.
	 */
	Optional<Long> createBrowser(String browserUserAgent);

	/**
	 * Delete the authentication (and access records) for any user in this subscription.
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @return true if successful, false if not.
	 */
	boolean deleteAuthentications(Long subscriptionId);

	/**
	 * Read the authentication with this id.
	 * 
	 * @param id
	 *            The authentication id.
	 * @return The optional authentication.
	 */
	Optional<Authentication> readAuthentication(Long id);

	/**
	 * Read the authentications from users in this subscription
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @return The list of authentications from users in this subscription.
	 */
	List<Authentication> readAuthentications(Long subscriptionId);

	/**
	 * Read the browser user agent string for this id.
	 * 
	 * @param id
	 *            The browser id.
	 * @return The browser user agent string, or not.
	 */
	Optional<String> readBrowser(Long id);

	/**
	 * Read the browser id for this user agent string .
	 * 
	 * @param browserUserAgent
	 *            The browser user agent string.
	 * @return The browser id for this user agent string, or not.
	 */
	Optional<Long> readBrowser(String browserUserAgent);
}
