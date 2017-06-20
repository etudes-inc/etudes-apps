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

package org.etudes.apps.user.data;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.etudes.apps.db.DB.Holder;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.Tokens;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserData {

	/**
	 * Create or update the login.
	 * 
	 * @param login
	 *            The login.
	 */
	void createOrUpdateLogin(Login login);

	/**
	 * Create or update the tokens.
	 * 
	 * @param tokens
	 *            The tokens.
	 */
	void createOrUpdateTokens(Tokens tokens);

	/**
	 * Delete all users related to this subscription.
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 */
	void deleteSubscriptionUsers(Long subscriptionId);

	/**
	 * Delete the tokens for this user.
	 * 
	 * @param id
	 *            The user id.
	 */
	void deleteTokens(Long id);

	/**
	 * Delete the user with this id.
	 * 
	 * @param id
	 *            The user id to delete.
	 */
	void deleteUser(Long id);

	/**
	 * Read or, if missing, atomically create a user.
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @param lmsId
	 *            The user's external LMS id.
	 * @return The newly created and saved user, complete with internal id set.
	 */
	Optional<User> readCreateUser(Long subscriptionId, String lmsId);

	/**
	 * Read the user's login
	 * 
	 * @param id
	 *            The user id.
	 * @return The user's login, or not.
	 */
	Optional<Login> readLogin(Long id);

	/**
	 * Read all logins for this email.
	 * 
	 * @param email
	 *            The email (login id).
	 * @return The List of Logins, possibly empty.
	 */
	List<Login> readLoginsByEmail(String email);

	/**
	 * Read the user's tokens
	 * 
	 * @param id
	 *            The user id.
	 * @return The user's tokens, or not.
	 */
	Optional<Tokens> readTokens(Long id);

	/**
	 * Read the user with this id.
	 * 
	 * @param id
	 *            The internal user id.
	 * @return The user, if found.
	 */
	Optional<User> readUser(Long id);

	/**
	 * Get all users from this subscription
	 * 
	 * @param subscriptionId
	 *            The subscription id.
	 * @return The list of users from this subscription, possibly empty.
	 */
	List<User> readUsersForSubscription(Long subscriptionId);

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
	 * Update the user.
	 * 
	 * @param user
	 */
	void updateUser(User user);
}
