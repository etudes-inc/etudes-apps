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
import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.Assessment;
import org.etudes.mneme.model.AssessmentStatus;
import org.etudes.mneme.model.Attribution;
import org.etudes.mneme.model.Part;
import org.etudes.mneme.model.Presentation;
import org.etudes.mneme.model.Schedule;
import org.junit.Test;

public class AssessmentTest {

	final protected Optional<Date> due = Optional.of(new Date(33333333l));
	final protected boolean hideUntilOpen = true;
	final protected Optional<Date> open = Optional.of(new Date(22222222l));
	final protected Optional<Date> until = Optional.of(new Date(44444444l));

	@Test
	public void test_nullsNotAllowed() {
		Assessment a = new Assessment();
		Assertions.assertThat(a.getContext()).isNotNull();
		Assertions.assertThat(a.getCreated()).isNotNull();
		Assertions.assertThat(a.getModified()).isNotNull();
		Assertions.assertThat(a.getParts()).isNotNull();
		Assertions.assertThat(a.getPresentation()).isNotNull();
		Assertions.assertThat(a.getSchedule()).isNotNull();
		Assertions.assertThat(a.getStatus()).isNotNull();
		Assertions.assertThat(a.getTitle()).isNotNull();
		Assertions.assertThat(a.getType()).isNotNull();

		try {
			a.setContext(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getContext()).isNotNull();

		try {
			a.setCreated(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getCreated()).isNotNull();

		try {
			a.setModified(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getModified()).isNotNull();

		try {
			a.setParts(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getParts()).isNotNull();

		try {
			a.setPresentation(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getPresentation()).isNotNull();

		try {
			a.setSchedule(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getSchedule()).isNotNull();

		try {
			a.setStatus(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getStatus()).isNotNull();

		try {
			a.setTitle(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getTitle()).isNotNull();

		try {
			a.setType(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
		}
		Assertions.assertThat(a.getType()).isNotNull();
	}

	@Test
	public void test_set() {
		final String context = "Context";
		final long userId = 42;
		final Attribution created = new Attribution(new Date(22222222l), userId);
		final long id = 22;
		final Attribution modified = new Attribution(new Date(33333333l), userId);
		final ArrayList<Part> parts = new ArrayList<>();
		final String presentationText = "Presentation";
		final Presentation presentation = new Presentation(presentationText);
		final Optional<Date> due = Optional.of(new Date(33333333l));
		final Optional<Date> open = Optional.of(new Date(22222222l));
		final Optional<Date> until = Optional.of(new Date(44444444l));
		final boolean hideUntilOpen = true;
		final Schedule schedule = new Schedule(due, hideUntilOpen, open, until);
		final boolean published = true;
		final boolean valid = true;
		final AssessmentStatus status = new AssessmentStatus(published, valid);
		final long subscription = 6l;
		final String title = "Title";
		final Assessment.Type type = Assessment.Type.assignment;
		Assessment a = new Assessment(context, created, id, modified, parts, presentation, schedule, status, subscription, title, type);

		Assessment a2 = new Assessment().set(a);
		Assertions.assertThat(a2).isNotNull();
		Assertions.assertThat(a).isEqualTo(a2);
	}
}
