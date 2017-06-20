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

import org.assertj.core.api.Assertions;
import org.etudes.apps.user.model.Login;
import org.junit.Test;

public class LoginTest {

	@Test
	public void testPasswordEncoding() {
		Login login = new Login(null, "email", null, 1l);
		login.changePassword("Pass2Word");

		String encoded = login.getPassword();
		Assertions.assertThat(encoded).isEqualTo("rEFrX5bUuAkzQQ2ULinR/Q==");

		boolean valid = login.checkPassword("Pass2Word");
		Assertions.assertThat(valid).isTrue();

		// to get any encoding
		Long idToTest = 1L;
		String pwToTest = "Pass3Word";
		Login l2 = new Login(null, "email", null, idToTest);

		l2.changePassword(pwToTest);
		Assertions.assertThat(l2.getPassword()).isNotNull(); // null would mean pwToTest is not strong and was rejected

		System.out.println("id: " + l2.get_id() + "  clear: " + pwToTest + "  encoded: " + l2.getPassword());

		Assertions.assertThat(l2.getPassword()).isNotEqualTo(login.getPassword());
	}
}
