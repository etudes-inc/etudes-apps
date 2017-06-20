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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.data.UserData;
import org.etudes.apps.user.impl.UserServiceImpl;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test the UserService impl
 * 
 * Developer Note: do NOT use Eclipse's "source > sort members" feature, as it messes up the order of mock objects
 */
public class UserServiceTest {

	// mock the data access
	private static final UserData userData = Mockito.mock(UserData.class);

	// create the service to test
	private static final UserService userService = new UserServiceImpl(userData);

	private static User USER_1 = new User(1l, "lms-1", 10l);
	private static User USER_2 = new User(2l, "lms-2", 10l);
	private static User USER_3 = new User(3l, "lms-3", 10l);
	private static User USER_4 = new User(4l, null, 10l);
	private static Login LOGIN_4 = new Login(1L, "", "", 4L);

	/**
	 * Before each test, setup the mock services to return the mock models, if asked for nicely ;-)
	 */
	@Before
	public void setup() {

		Mockito.when(userData.readUser(1L)).thenReturn(Optional.of(USER_1));
		Mockito.when(userData.readUser(2L)).thenReturn(Optional.of(USER_2));
		Mockito.when(userData.readUser(3L)).thenReturn(Optional.of(USER_3));
		Mockito.when(userData.readUser(4L)).thenReturn(Optional.of(USER_4));

		Mockito.when(userData.readCreateUser(10L, "lms-1")).thenReturn(Optional.of(USER_1));
		Mockito.when(userData.readCreateUser(10L, "lms-2")).thenReturn(Optional.of(USER_2));
		Mockito.when(userData.readCreateUser(10L, "lms-3")).thenReturn(Optional.of(USER_3));

		LOGIN_4.setEmail("login@client.org");
		LOGIN_4.changePassword("Pass4word");

		List<Login> loginList = new ArrayList<>();
		loginList.add(LOGIN_4);
		Mockito.when(userData.readLoginsByEmail(Mockito.anyString())).thenReturn(loginList);
	}

	/**
	 * Cleanup after the test, resetting the mocked services.
	 */
	@After
	public void tearDown() {

		Mockito.reset(userData);
	}

	@Test
	public void testFetch() {
		Assertions.assertThat(userService.getUser(1L)).isEqualTo(Optional.of(USER_1));
		Assertions.assertThat(userService.getUser(2L)).isEqualTo(Optional.of(USER_2));
		Assertions.assertThat(userService.getUser(3L)).isEqualTo(Optional.of(USER_3));
		Assertions.assertThat(userService.getUser(0L)).isEqualTo(Optional.empty());

		Assertions.assertThat(userService.getUser(10L, "lms-1")).isEqualTo(Optional.of(USER_1));
		Assertions.assertThat(userService.getUser(10L, "lms-2")).isEqualTo(Optional.of(USER_2));
		Assertions.assertThat(userService.getUser(10L, "lms-3")).isEqualTo(Optional.of(USER_3));
	}

	@Test
	public void testLogin() {
		Optional<User> u = userService.getAuthenticatedUser("login@client.org", "Pass4word");
		Assertions.assertThat(u).isEqualTo(Optional.of(USER_4));
	}
}
