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

package org.etudes.apps.user.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.etudes.apps.db.DB.Holder;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.data.UserData;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.Tokens;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
	final static private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/** Data access */
	protected UserData data = null;

	/**
	 * Create the user service
	 * 
	 * @param config
	 *            The configuration
	 * @param data
	 *            The UserData object that interfaces with the persisted user data.
	 */
	@Inject
	public UserServiceImpl(UserData data) {
		this.data = data;
		logger.info("UserService:  data = " + this.data);
	}

	@Override
	public Optional<User> getAuthenticatedUser(String email, String password) {

		// start with all users with this login
		List<Login> logins = data.readLoginsByEmail(email);

		// if we have any users with this login, pick the first one that also has this password
		List<Login> authenticated = logins.stream().filter(l -> l.checkPassword(password)).collect(Collectors.toList());

		// if we have more than one authenticated, that's not good
		if (authenticated.size() != 1)
			return Optional.empty();

		// get and return this user
		return data.readUser(authenticated.get(0).getUserId());
	}

	@Override
	public Optional<Login> getLogin(Long id) {
		return data.readLogin(id);
	}

	@Override
	public Optional<Tokens> getTokens(Long id) {
		return data.readTokens(id);
	}

	@Override
	public Optional<User> getUser(Long id) {
		if ((id == null) || (id.equals(0L)))
			return Optional.empty();

		Optional<User> rv = data.readUser(id);
		return rv;
	}

	@Override
	public Optional<User> getUser(Long clientId, String lmsId) {

		Optional<User> rv = data.readCreateUser(clientId, lmsId);

		return rv;
	}

	@Override
	public List<User> getUsersForSubscription(Long subscriptionId) {

		List<User> rv = data.readUsersForSubscription(subscriptionId);

		return rv;
	}

	@Override
	public Optional<Tokens> refreshOrUpdateTokens(Tokens tokens, Consumer<Holder<String>> job) {
		return data.refreshOrUpdateTokens(tokens, job);
	}

	@Override
	public void removeSubscriptionUsers(Long subscriptionId) {

		data.deleteSubscriptionUsers(subscriptionId);
	}

	@Override
	public void removeTokens(Tokens tokens) {
		data.deleteTokens(tokens.getUserId());
		tokens.set_id(null);
		tokens.setLmsRefresh(null);
		tokens.setLmsToken(null);
	}

	@Override
	public void removeUser(User user) {

		data.deleteUser(user.get_id());
	}

	@Override
	public void saveLogin(Login login) {
		data.createOrUpdateLogin(login);
	}

	@Override
	public void saveTokens(Tokens tokens) {
		data.createOrUpdateTokens(tokens);
	}

	@Override
	public void saveUser(User user) {
		data.updateUser(user);
	}
}
