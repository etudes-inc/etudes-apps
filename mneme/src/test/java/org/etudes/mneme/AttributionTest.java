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

package org.etudes.mneme;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.Attribution;
import org.junit.Test;

public class AttributionTest {

	@Test
	public void test_set() {
		Attribution a = new Attribution(new Date(2222222l), 22l);

		Attribution a2 = new Attribution().set(a);
		Assertions.assertThat(a2).isNotNull();
		Assertions.assertThat(a).isEqualTo(a2);
	}

	@Test
	public void test_setDate_nullNotAllowed() {
		Attribution a = new Attribution();
		Assertions.assertThat(a.getDate()).isNotNull();

		try {
			a.setDate(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
			// System.out.println(e);
		}
		Assertions.assertThat(a.getDate()).isNotNull();
	}
}
