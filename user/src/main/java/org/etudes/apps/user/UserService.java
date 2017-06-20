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

package org.etudes.apps.user;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.etudes.apps.db.DB.Holder;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.Tokens;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserService {

	/**
	 * Get the user with this email (login) and password.
	 * 
	 * @param email
	 *            The user email.
	 * @param password
	 *            the user (clear text) password.
	 * @return The user, if found and authenticated.
	 */
	Optional<User> getAuthenticatedUser(String email, String password);

	/**
	 * Get this user's login
	 * 
	 * @param id
	 *            The user id.
	 * @return The login, or not.
	 */
	Optional<Login> getLogin(Long id);

	/**
	 * Get this user's LMS tokens
	 * 
	 * @param id
	 *            The user id.
	 * @return The tokens, or not.
	 */
	Optional<Tokens> getTokens(Long id);

	/**
	 * Get the user with this id.
	 * 
	 * @param id
	 *            The internal user id.
	 * @return The user, if found.
	 */
	Optional<User> getUser(Long id);

	/**
	 * Find, or create, a user mapped to this external (LMS) id from this client.
	 * 
	 * @param subscriptionId
	 *            The subscription id to which this user belongs.
	 * @param lmsId
	 *            The user's external LMS id.
	 * @return The found or created and saved user.
	 */
	Optional<User> getUser(Long subscriptionId, String lmsId);

	/**
	 * Get all users from this subscription
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @return The list of users from this subscription, possibly empty.
	 */
	List<User> getUsersForSubscription(Long subscriptionId);

	/**
	 * Refresh the user's tokens. If they have changed, return the new tokens. Otherwise, get new tokens running the job, then update the db. Synchronized
	 * through the db so the job is only run once at a time.
	 * 
	 * @param tokens
	 *            The user's tokens. Must exist with an id.
	 * @param job
	 *            The job to run that updates the tokens.
	 * @return The updated tokens, or not if the job runs and fails.
	 */
	Optional<Tokens> refreshOrUpdateTokens(Tokens tokens, Consumer<Holder<String>> job);

	/**
	 * Remove all users from this subscription.
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 */
	void removeSubscriptionUsers(Long subscriptionId);

	/**
	 * Remove these tokens from the user.
	 * 
	 * @param tokens
	 *            The tokens to remove.
	 */
	void removeTokens(Tokens tokens);

	/**
	 * Remove this user.
	 * 
	 * @param user
	 *            The user to remove.
	 */
	void removeUser(User user);

	/**
	 * Save changes in this user's login.
	 * 
	 * @param login
	 *            The login to save.
	 */
	void saveLogin(Login login);

	/**
	 * Save changes in this user's tokens.
	 * 
	 * @param tokens
	 *            The tokens to save.
	 */
	void saveTokens(Tokens tokens);

	/**
	 * Save changes in this user. Update any fields that change as a result of the save.
	 * 
	 * @param user
	 *            The user to save.
	 */
	void saveUser(User user);
}
