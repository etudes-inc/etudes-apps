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
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.etudes.apps.db.DB;
import org.etudes.mneme.data.AssessmentData;
import org.etudes.mneme.impl.AssessmentDataJDBIImpl;
import org.etudes.mneme.model.Assessment;
import org.etudes.mneme.model.Assessment.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

/**
 * Test the AssessmentDataJDBIImpl
 */
public class AssessmentDataJDBITest {

	// create the DBI to test
	private static AssessmentData data = null;

	// each test gets a new database URL to keep the records separate
	protected static int testSequence = 0;

	final protected String context = "CLASS";
	final protected String context2 = "COURSE";
	final protected long missingId = 99999l;
	final protected String presentationText = "Presentation Text";
	final protected String presentationText2 = "Another presentation";
	final protected long sub = 4l;
	final protected long sub2 = 5l;
	final protected String title = "TITLE";
	final protected String title2 = "Other Title";
	final protected long user = 22l;
	final protected long user2 = 222l;
	final protected long user3 = 2222l;

	@Before
	public void setUp() throws Exception {

		DataSourceFactory database = new DataSourceFactory();
		database.setDriverClass("org.h2.Driver");
		database.setUrl("jdbc:h2:mem:AssessmentDataJDBITest" + (testSequence++) + ";mode=mysql");
		database.setUser("u");
		database.setPassword("p");

		Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);

		DB db = new DB(new DBIFactory().build(environment, database, "db"), true);

		data = new AssessmentDataJDBIImpl(db);
	}

	/**
	 * Cleanup after the test, resetting the mocked services.
	 */
	@After
	public void tearDown() {
	}

	@Test
	public void testAssessmentCreateWithAllProperties() {
		// add an assessment
		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);
	}

	@Test
	public void testAssessmentCreateWithMinimalProperties() {
		// add a minimally set assessment
		Assessment a = new Assessment();
		a.setSubscription(sub);
		a.setContext(context);
		a.getCreated().setDate(new Date());
		a.getCreated().setUserId(user);
		a.getModified().setDate(new Date());
		a.getModified().setUserId(user2);

		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get()).isEqualTo(a);
	}

	@Test
	public void testAssessmentDelete() {
		// add an assessment
		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		final long id = created.get().getId();

		// read by id
		Optional<Assessment> read = data.readAssessment(id);

		// make sure we got it
		Assertions.assertThat(read).isNotNull();
		Assertions.assertThat(read).isPresent();

		// and that it matches as created
		Assertions.assertThat(read.get()).isEqualTo(created.get());

		// delete
		boolean success = data.delete(created.get());
		Assertions.assertThat(success).isTrue();

		// try a re-read
		Optional<Assessment> missing = data.readAssessment(id);
		Assertions.assertThat(missing).isNotNull();
		Assertions.assertThat(missing).isNotPresent();
	}

	@Test
	public void testAssessmentDeleteBySubscription() {

		// final long sub = 8l;
		// final long sub2 = 9l;

		// add an assessment
		Assessment a = buildAssessment();
		// a.setSubscription(sub);

		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		// create another in another subscription
		Assessment a2 = buildAssessment();
		a2.setTitle(title2);
		a2.setSubscription(sub2);

		Optional<Assessment> created2 = data.create(a2);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created2).isNotNull();
		Assertions.assertThat(created2).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a2.setId(created2.get().getId());
		Assertions.assertThat(created2.get().getTitle()).isEqualTo(title2);
		Assertions.assertThat(created2.get()).isEqualTo(a2);

		// read by subscription id - s1
		List<Assessment> asmtsS1 = data.readAssessments(sub);

		// make sure we got a list of 1 entry, the one for s1
		Assertions.assertThat(asmtsS1).isNotNull();
		Assertions.assertThat(asmtsS1).hasSize(1);
		Assertions.assertThat(asmtsS1).contains(created.get());

		// read by subscription id - s2
		List<Assessment> asmtsS2 = data.readAssessments(sub2);

		// make sure we got a list of 1 entry, the one for s2
		Assertions.assertThat(asmtsS2).isNotNull();
		Assertions.assertThat(asmtsS2).hasSize(1);
		Assertions.assertThat(asmtsS2).contains(created2.get());

		// delete s1
		boolean success = data.deleteAssessments(sub);
		Assertions.assertThat(success).isTrue();

		// read by subscription id - s1
		asmtsS1 = data.readAssessments(sub);

		// make sure we got a list of 0 entries
		Assertions.assertThat(asmtsS1).isNotNull();
		Assertions.assertThat(asmtsS1).hasSize(0);

		// read by subscription id - s2
		asmtsS2 = data.readAssessments(sub2);

		// make sure we got a list of 1 entry, the one for s2
		Assertions.assertThat(asmtsS2).isNotNull();
		Assertions.assertThat(asmtsS2).hasSize(1);
		Assertions.assertThat(asmtsS2).contains(created2.get());
	}

	@Test
	public void testAssessmentRead() {

		// read by non existing id
		Optional<Assessment> missing = data.readAssessment(missingId);
		Assertions.assertThat(missing).isNotNull();
		Assertions.assertThat(missing).isNotPresent();

		// add an assessment
		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		final long id = created.get().getId();

		// read by id
		Optional<Assessment> read = data.readAssessment(id);

		// make sure we got it
		Assertions.assertThat(read).isNotNull();
		Assertions.assertThat(read).isPresent();

		// and that it matches as created
		Assertions.assertThat(read.get()).isEqualTo(created.get());
	}

	@Test
	public void testAssessmentReadByContext() {

		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		// create another in another context
		Assessment a2 = buildAssessment();
		a2.setTitle(title2);
		a2.setContext(context2);
		Optional<Assessment> created2 = data.create(a2);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created2).isNotNull();
		Assertions.assertThat(created2).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a2.setId(created2.get().getId());
		Assertions.assertThat(created2.get().getTitle()).isEqualTo(title2);
		Assertions.assertThat(created2.get()).isEqualTo(a2);

		// read by context - 1
		List<Assessment> asmtsS1 = data.readAssessments(sub, context);

		// make sure we got a list of 1 entry, the one for context
		Assertions.assertThat(asmtsS1).isNotNull();
		Assertions.assertThat(asmtsS1).hasSize(1);
		Assertions.assertThat(asmtsS1).contains(created.get());

		// read by context - 2
		List<Assessment> asmtsS2 = data.readAssessments(sub, context2);

		// make sure we got a list of 1 entry, the one for context2
		Assertions.assertThat(asmtsS2).isNotNull();
		Assertions.assertThat(asmtsS2).hasSize(1);
		Assertions.assertThat(asmtsS2).contains(created2.get());
	}

	@Test
	public void testAssessmentReadBySubscription() {

		// add an assessment
		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		// create another in another subscription
		Assessment a2 = new Assessment();
		a2.setTitle(title2);
		a2.setSubscription(sub2);
		Optional<Assessment> created2 = data.create(a2);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created2).isNotNull();
		Assertions.assertThat(created2).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a2.setId(created2.get().getId());
		Assertions.assertThat(created2.get().getTitle()).isEqualTo(title2);
		Assertions.assertThat(created2.get()).isEqualTo(a2);

		// read by subscription id - s1
		List<Assessment> asmtsS1 = data.readAssessments(sub);

		// make sure we got a list of 1 entry, the one for s1
		Assertions.assertThat(asmtsS1).isNotNull();
		Assertions.assertThat(asmtsS1).hasSize(1);
		Assertions.assertThat(asmtsS1).contains(created.get());

		// read by subscription id - s2
		List<Assessment> asmtsS2 = data.readAssessments(sub2);

		// make sure we got a list of 1 entry, the one for s2
		Assertions.assertThat(asmtsS2).isNotNull();
		Assertions.assertThat(asmtsS2).hasSize(1);
		Assertions.assertThat(asmtsS2).contains(created2.get());
	}

	@Test
	public void testAssessmentUpdate() {

		// add an assessment
		Assessment a = buildAssessment();
		Optional<Assessment> created = data.create(a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id will be set, though)
		a.setId(created.get().getId());
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		final long id = created.get().getId();

		// read by id
		Optional<Assessment> read = data.readAssessment(id);

		// make sure we got it
		Assertions.assertThat(read).isNotNull();
		Assertions.assertThat(read).isPresent();

		// and that it matches as created
		Assertions.assertThat(read.get()).isEqualTo(created.get());

		// get another copy
		Optional<Assessment> toChange = data.readAssessment(id);

		// make sure we got it
		Assertions.assertThat(toChange).isPresent();

		// update
		toChange.get().setTitle(title2);
		toChange.get().setTitle(presentationText2);
		toChange.get().setType(Type.assignment);
		toChange.get().getSchedule().setDue(Optional.of(new Date(25000000l)));
		toChange.get().getSchedule().setHideUntilOpen(true);
		toChange.get().getSchedule().setOpen(Optional.of(new Date(15000000l)));
		toChange.get().getSchedule().setUntil(Optional.of(new Date(35000000l)));
		toChange.get().getModified().setDate(new Date());
		toChange.get().getModified().setUserId(user3);
		// TODO: more changes

		Optional<Assessment> updated = data.updateAssessment(read.get(), toChange.get());

		// make sure we got the updated version
		Assertions.assertThat(updated).isNotNull();
		Assertions.assertThat(updated).isPresent();

		// and that it matches
		Assertions.assertThat(updated.get()).isEqualTo(toChange.get());
	}

	/**
	 * @return an assessment with all properties set.
	 */
	protected Assessment buildAssessment() {
		Assessment a = new Assessment();
		a.setTitle(title);
		a.getPresentation().setText(presentationText);
		a.setSubscription(sub);
		a.setContext(context);
		a.setType(Type.offline);
		a.getStatus().setPublished(true);
		a.getSchedule().setDue(Optional.of(new Date(20000000l)));
		a.getSchedule().setHideUntilOpen(false);
		a.getSchedule().setOpen(Optional.of(new Date(10000000l)));
		a.getSchedule().setUntil(Optional.of(new Date(30000000l)));
		a.getCreated().setDate(new Date());
		a.getCreated().setUserId(user);
		a.getModified().setDate(new Date());
		a.getModified().setUserId(user2);
		// TODO: more settings

		return a;
	}
}
