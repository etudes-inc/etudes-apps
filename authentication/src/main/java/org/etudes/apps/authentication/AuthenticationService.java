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

package org.etudes.apps.authentication;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AuthenticationService {

	/** cookie name for special admin token from browser */
	final String ADMIN_TOKEN = "Etudes.apps.admin.token";
	final String ADMIN_TOKEN_ALT = "b";

	/** ipAddress value in authentication indicating don't validate IP when authenticating (test fixture) */
	final String IGNORE_IP = "0.0.0.0";

	/** cookie name for sending token from browser */
	final String TOKEN = "Etudes.apps.authentication.token";
	final String TOKEN_ALT = "a";

	/**
	 * Authenticate by token.
	 * 
	 * @param token
	 *            The authentication token.
	 * @param alternate
	 *            An alternate token to use if the token is null (otherwise must match token).
	 * @param req
	 *            The request.
	 * @return The Authentication if found and valid, or not.
	 */
	Optional<Authentication> authenticateByToken(Long token, Long alternate, HttpServletRequest req);

	/**
	 * Create an authentication for a user who has been vetted.
	 * 
	 * @param user
	 *            The user.
	 * @param ipAddress
	 *            The ip address.
	 * @param browserUserAgent
	 *            the browser.
	 * @param context
	 *            The authentication context.
	 * @param role
	 *            The user's role in the context.
	 * @param placement
	 *            The placement id.
	 * @return The authentication.
	 */
	Optional<Authentication> authenticateUser(User user, String ipAddress, String browserUserAgent, String context, String role, String placement);

	/**
	 * Get the authentications from users in this subscription
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @return The list of authentications from users in this subscription.
	 */
	List<Authentication> getAuthentications(Long subscriptionId);

	/**
	 * Return the browser user-agent string registered with this id.
	 * 
	 * @param id
	 *            The browser id.
	 * @return The browser user-agent string for this id, or not.
	 */
	Optional<String> getBroswerUserAgent(Long id);

	/**
	 * Return the browser user-agent code registered for this id, creating if needed.
	 * 
	 * @param browserUserAgent
	 *            The browser id.
	 * @return The browser user-agent code for this string.
	 */
	Optional<Long> getBroswerUserAgent(String browserUserAgent);

	/**
	 * Remove all authentication (and access records) from this subscription.
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 */
	void removeSubscriptionAuthentications(Long subscriptionId);
}
