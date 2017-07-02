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
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.mneme.model.Schedule;
import org.junit.Test;

public class ScheduleTest {

	final protected Optional<Date> due = Optional.of(new Date(33333333l));
	final protected boolean hideUntilOpen = true;
	final protected Optional<Date> open = Optional.of(new Date(22222222l));
	final protected Optional<Date> until = Optional.of(new Date(44444444l));

	@Test
	public void test_nullDatesNotAllowed() {
		Schedule s = new Schedule();
		Assertions.assertThat(s.getDue()).isNotNull();
		Assertions.assertThat(s.getOpen()).isNotNull();
		Assertions.assertThat(s.getUntil()).isNotNull();

		try {
			s.setOpen(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
			// System.out.println(e);
		}
		Assertions.assertThat(s.getOpen()).isNotNull();

		try {
			s.setDue(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
			// System.out.println(e);
		}
		Assertions.assertThat(s.getDue()).isNotNull();

		try {
			s.setUntil(null);
			Assertions.fail("null allowed");
		} catch (NullPointerException e) {
			// System.out.println(e);
		}
		Assertions.assertThat(s.getUntil()).isNotNull();
	}

	@Test
	public void test_optionalDatesAllowed() {
		Schedule s = new Schedule();
		Assertions.assertThat(s.getDue()).isNotNull().isNotPresent();
		Assertions.assertThat(s.getOpen()).isNotNull().isNotPresent();
		Assertions.assertThat(s.getUntil()).isNotNull().isNotPresent();

		s.setOpen(open);
		s.setDue(due);
		s.setUntil(until);

		Assertions.assertThat(s.getDue()).isPresent();
		Assertions.assertThat(s.getOpen()).isPresent();
		Assertions.assertThat(s.getUntil()).isPresent();

		s.setOpen(Optional.empty());
		s.setDue(Optional.empty());
		s.setUntil(Optional.empty());

		Assertions.assertThat(s.getDue()).isNotPresent();
		Assertions.assertThat(s.getOpen()).isNotPresent();
		Assertions.assertThat(s.getUntil()).isNotPresent();
	}

	@Test
	public void test_set() {
		Schedule s = new Schedule(due, hideUntilOpen, open, until);

		Schedule s2 = new Schedule().set(s);
		Assertions.assertThat(s2).isNotNull();
		Assertions.assertThat(s).isEqualTo(s2);
	}
}
