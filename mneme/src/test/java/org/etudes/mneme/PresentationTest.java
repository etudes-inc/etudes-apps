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

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.Presentation;
import org.junit.Test;

public class PresentationTest {

	@Test
	public void test_isEmpty() {
		Presentation p = new Presentation();
		Assertions.assertThat(p.isEmpty()).isTrue();

		final String text = "This is presentation text";
		p.setText(text);
		Assertions.assertThat(p.isEmpty()).isFalse();
		Assertions.assertThat(p.getText()).isEqualTo(text);
	}

	@Test
	public void test_set() {
		final String text = "Some Text";
		Presentation p = new Presentation(text);

		Presentation p2 = new Presentation().set(p);
		Assertions.assertThat(p2).isNotNull();
		Assertions.assertThat(p).isEqualTo(p2);
	}

	@Test
	public void test_setText_nullNotAllowed() {
		// what happens when we try to set null text?
		Presentation p = new Presentation();
		Assertions.assertThat(p.getText()).isNotNull();

		try {
			p.setText(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
			// System.out.println(e);
		}
		Assertions.assertThat(p.getText()).isNotNull();

		final String text = "A presentation";
		p.setText(text);
		Assertions.assertThat(p.getText()).isEqualTo(text);
	}
}
