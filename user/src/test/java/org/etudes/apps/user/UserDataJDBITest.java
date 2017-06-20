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

import org.assertj.core.api.Assertions;
import org.etudes.apps.db.DB;
import org.etudes.apps.user.data.UserData;
import org.etudes.apps.user.impl.UserDataJDBIImpl;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.Tokens;
import org.etudes.apps.user.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

/**
 * Test the UserDataJDBI impl
 */
public class UserDataJDBITest {

	// create the DBI to test
	private static UserData data = null;

	@Before
	public void setUp() throws Exception {

		DataSourceFactory database = new DataSourceFactory();
		database.setDriverClass("org.h2.Driver");
		database.setUrl("jdbc:h2:mem:UserDataJDBITest;mode=mysql");
		database.setUser("u");
		database.setPassword("p");

		Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);

		DB db = new DB(new DBIFactory().build(environment, database, "db"), true);

		data = new UserDataJDBIImpl(db);
	}

	/**
	 * Cleanup after the test, resetting the mocked services.
	 */
	@After
	public void tearDown() {

	}

	/**
	 * Test the /profile path to get the user's profile
	 */
	@Test
	public void test() {

		// test a failed read
		Optional<User> maybe = data.readUser(4L);
		Assertions.assertThat(maybe.isPresent()).isEqualTo(false);

		// create some users
		Optional<User> u = data.readCreateUser(2L, "lms1");
		Optional<User> u2 = data.readCreateUser(2L, "lms2");

		// verify we can find our first by values
		Optional<User> found = data.readCreateUser(2L, "lms1");
		Assertions.assertThat(found.isPresent()).isTrue();

		// verify we can find our first by id
		maybe = data.readUser(u.get().get_id());
		Assertions.assertThat(maybe).isEqualTo(u);

		// set all fields and update
		u.get().setEmail("email@address.com");
		u.get().setFamilyName("Smith");
		u.get().setGivenName("Janet");
		u.get().setSisId("214365879");
		data.updateUser(u.get());

		// verify we can find the user, and the found user matches in all fields
		maybe = data.readUser(u.get().get_id());
		Assertions.assertThat(maybe).isEqualTo(u);

		// add tokens
		Tokens t = new Tokens(null, "refresh", "token", u.get().get_id());
		data.createOrUpdateTokens(t);

		// fail a get
		Optional<Tokens> toks = data.readTokens(u2.get().get_id());
		Assertions.assertThat(toks.isPresent()).isEqualTo(false);

		// read
		toks = data.readTokens(u.get().get_id());
		Assertions.assertThat(toks.isPresent()).isEqualTo(true);
		Assertions.assertThat(toks.get()).isEqualTo(t);

		// update tokens
		toks.get().setLmsToken("newToken");
		data.createOrUpdateTokens(toks.get());

		// add login
		Login l = new Login(null, "email", "password", u.get().get_id());
		data.createOrUpdateLogin(l);

		// fail a get
		Optional<Login> log = data.readLogin(u2.get().get_id());
		Assertions.assertThat(log.isPresent()).isEqualTo(false);

		// read
		log = data.readLogin(u.get().get_id());
		Assertions.assertThat(log.isPresent()).isEqualTo(true);
		Assertions.assertThat(log.get()).isEqualTo(l);

		// find the login by email
		List<Login> maybies = data.readLoginsByEmail(l.getEmail());
		Assertions.assertThat(maybies).hasSize(1).contains(l);

		// update login
		log.get().setPassword("newPassword");
		data.createOrUpdateLogin(log.get());

		// refresh u and u2
		u = data.readUser(u.get().get_id());
		u2 = data.readUser(u2.get().get_id());

		// test the full subscription read
		List<User> subUsers = data.readUsersForSubscription(2L);
		Assertions.assertThat(subUsers).isNotNull();
		Assertions.assertThat(subUsers).contains(u2.get());
		// u will be different, as the token / login flags will be set in subUsers but not in the single read values
		Assertions.assertThat(subUsers).hasSize(2);

		// remove a user, verify user, login, tokens gone
		// H2 does not support this form of delete statement
		// data.deleteUser(u.get().get_id());
		// maybe = data.readUser(u.get().get_id());
		// Assertions.assertThat(maybe).isEqualTo(Optional.empty());
		// toks = data.readTokens(u.get().get_id());
		// Assertions.assertThat(maybe.isPresent()).isEqualTo(false);
		// log = data.readLogin(u.get().get_id());
		// Assertions.assertThat(log.isPresent()).isEqualTo(false);

		// remove all subscription users, verify gone
		// data.deleteSubscriptionUsers(2L);
		// maybe = data.readUser(u2.get().get_id());
		// Assertions.assertThat(maybe).isEqualTo(Optional.empty());
	}
}
