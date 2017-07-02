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

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.Part;
import org.etudes.mneme.model.PartDetail;
import org.etudes.mneme.model.Presentation;
import org.junit.Test;

public class PartTest {

	final protected long id = 42l;
	final protected String presentationText = "Presentation Text";
	final protected String title = "Title";

	@Test
	public void test_nullsNotAllowed() {
		Part p = new Part();
		Assertions.assertThat(p.getDetails()).isNotNull();
		Assertions.assertThat(p.getPresentation()).isNotNull();
		Assertions.assertThat(p.getTitle()).isNotNull();

		try {
			p.setDetails(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(p.getDetails()).isNotNull();

		try {
			p.setPresentation(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(p.getPresentation()).isNotNull();

		try {
			p.setTitle(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(p.getTitle()).isNotNull();
	}

	@Test
	public void test_set() {
		final List<PartDetail> details = new ArrayList<PartDetail>();
		final Presentation presentation = new Presentation(presentationText);

		Part p = new Part(details, id, presentation, title);

		Part p2 = new Part().set(p);
		Assertions.assertThat(p2).isNotNull();
		Assertions.assertThat(p).isEqualTo(p2);
	}
}
