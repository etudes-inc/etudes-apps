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
import org.etudes.mneme.model.Question;
import org.etudes.mneme.model.Question.Type;
import org.junit.Test;

public class QuestionTest {

	protected final String presentationText = "Presentation";

	@Test
	public void test_nullsNotAllowed() {
		Question q = new Question();
		Assertions.assertThat(q.getPresentation()).isNotNull();
		Assertions.assertThat(q.getType()).isNotNull();

		try {
			q.setPresentation(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(q.getPresentation()).isNotNull();

		try {
			q.setType(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(q.getType()).isNotNull();
	}

	@Test
	public void test_set() {
		Question q = new Question(1l, new Presentation(presentationText), Type.multipleChoice);

		Question q2 = new Question().set(q);
		Assertions.assertThat(q2).isNotNull();
		Assertions.assertThat(q).isEqualTo(q2);
	}
}
