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

import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.apps.authentication.data.AuthenticationData;
import org.etudes.apps.authentication.impl.AuthenticationDataJDBIImpl;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.db.DB;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

/**
 * Test the AuthenticationDataJDBI impl
 */
public class AuthenticationDataJDBITest {

	// create the DBI to test
	private static AuthenticationData data = null;

	private static User USER_1 = new User(1l, "email", "family", "given", "lms-1", false, "sis", 10l, false);

	private static final UserService userService = Mockito.mock(UserService.class);

	@Before
	public void setUp() throws Exception {

		DataSourceFactory database = new DataSourceFactory();
		database.setDriverClass("org.h2.Driver");
		database.setUrl("jdbc:h2:mem:test");
		database.setUser("u");
		database.setPassword("p");

		Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);

		DB db = new DB(new DBIFactory().build(environment, database, "db"), true);

		Mockito.when(userService.getUser(1L)).thenReturn(Optional.of(USER_1));

		data = new AuthenticationDataJDBIImpl(db, userService);
	}

	/**
	 * Cleanup after the test, resetting the mocked services.
	 */
	@After
	public void tearDown() {

	}

	/**
	 * Test the /profile path to get the authentication's profile
	 */
	@Test
	public void test() {

		// create a browser entry
		Optional<Long> browser = data.createBrowser("junit/test");
		Assertions.assertThat(browser.isPresent()).isTrue();

		// fail to find a browser entry
		Optional<String> browserAgent = data.readBrowser(0l);
		Assertions.assertThat(browserAgent.isPresent()).isEqualTo(false);

		// find a browser entry
		browserAgent = data.readBrowser(browser.get());
		Assertions.assertThat(browserAgent.isPresent()).isEqualTo(true);
		Assertions.assertThat(browserAgent.get()).isEqualTo("junit/test");

		Date date = new Date();

		// create an authentication
		Optional<Authentication> auth = data.createAuthentication(USER_1, "127.0.0.1", browser.get(), "context", "role", "placement", date);
		Assertions.assertThat(auth.isPresent()).isTrue();
		Assertions.assertThat(auth.get().get_id()).isNotNull();
		Assertions.assertThat(auth.get().getIpAddress()).isEqualTo("127.0.0.1");
		Assertions.assertThat(auth.get().getBrowser()).isEqualTo(browser.get());
		Assertions.assertThat(auth.get().getContext()).isEqualTo("context");
		Assertions.assertThat(auth.get().getPlacement()).isEqualTo("placement");
		Assertions.assertThat(auth.get().getRole()).isEqualTo("role");
		Assertions.assertThat(auth.get().getDate()).isEqualTo(date);
		Assertions.assertThat(auth.get().getUser()).isEqualTo(USER_1);

		// fail to find an authentication
		Optional<Authentication> authFound = data.readAuthentication(0L);
		Assertions.assertThat(authFound).isEqualTo(Optional.empty());

		// find an authentication
		authFound = data.readAuthentication(auth.get().get_id());
		Assertions.assertThat(authFound.isPresent()).isEqualTo(true);
		Assertions.assertThat(authFound.get()).isEqualTo(auth.get());
	}
}
