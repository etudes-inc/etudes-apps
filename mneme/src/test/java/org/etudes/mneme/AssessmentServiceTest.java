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

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.etudes.apps.db.DB;
import org.etudes.mneme.data.AssessmentData;
import org.etudes.mneme.impl.AssessmentDataJDBIImpl;
import org.etudes.mneme.impl.AssessmentServiceImpl;
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
 * Test the AssessmentService impl
 */
public class AssessmentServiceTest {

	private static AssessmentService service = null;

	/**
	 * Before each test, setup ...
	 */
	@Before
	public void setup() {

		// create a db and AssessmentData
		DataSourceFactory database = new DataSourceFactory();
		database.setDriverClass("org.h2.Driver");
		database.setUrl("jdbc:h2:mem:AssessmentDataJDBITest;mode=mysql");
		database.setUser("u");
		database.setPassword("p");
		Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
		DB db = new DB(new DBIFactory().build(environment, database, "db"), true);
		AssessmentData data = new AssessmentDataJDBIImpl(db);

		// create the service
		service = new AssessmentServiceImpl(data);
	}

	/**
	 * Cleanup after the test, resetting ...
	 */
	@After
	public void tearDown() {
	}

	@Test
	public void testCreate() {

		final String title = "TITLE";
		final long sub = 1l;
		final String context = "CLASS";
		final long user = 22l;

		// add this assessment
		Assessment a = new Assessment();
		a.setTitle(title);
		a.setType(Type.assignment);
		a.getStatus().setPublished(true);
		a.getSchedule().setDue(new Date(20000000l));
		a.getSchedule().setHideUntilOpen(false);
		a.getSchedule().setOpen(new Date(10000000l));
		a.getSchedule().setUntil(new Date(30000000l));
		// TODO: more settings

		Optional<Assessment> created = service.createAssessment(sub, context, user, a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id, subscription, context, created, modified, will be set, asmt will be 'cleaned', though)
		a.setId(created.get().getId());
		a.setSubscription(created.get().getSubscription());
		a.setContext(created.get().getContext());
		a.getCreated().set(created.get().getCreated());
		a.getModified().set(created.get().getModified());
		Assertions.assertThat(created.get().getId()).isPositive();
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);
	}

	@Test
	public void testCreateTitleCleanup() {

		final String title = RandomStringUtils.randomAlphanumeric(260);
		final String shortenedTitle = title.substring(0, 255);
		final long sub = 1l;
		final String context = "CLASS";
		final long user = 22l;

		// add this assessment
		Assessment a = new Assessment();
		a.setTitle(title);
		a.setType(Type.assignment);
		a.getStatus().setPublished(true);
		a.getSchedule().setDue(new Date(20000000l));
		a.getSchedule().setHideUntilOpen(false);
		a.getSchedule().setOpen(new Date(10000000l));
		a.getSchedule().setUntil(new Date(30000000l));
		// TODO: more settings

		Optional<Assessment> created = service.createAssessment(sub, context, user, a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id, subscription, context, created, modified, will be set, asmt will be 'cleaned', though)
		a.setId(created.get().getId());
		a.setSubscription(created.get().getSubscription());
		a.setContext(created.get().getContext());
		a.getCreated().set(created.get().getCreated());
		a.getModified().set(created.get().getModified());
		a.setTitle(shortenedTitle);
		Assertions.assertThat(created.get().getTitle()).isEqualTo(shortenedTitle);
		Assertions.assertThat(created.get()).isEqualTo(a);
	}

	@Test
	public void testListByContext() {
	}

	@Test
	public void testGetById() {

		final String title = "TITLE";
		final long sub = 1l;
		final String context = "CLASS";
		final long user = 22l;

		// add this assessment
		Assessment a = new Assessment();
		a.setTitle(title);
		a.setType(Type.assignment);
		a.getStatus().setPublished(true);
		a.getSchedule().setDue(new Date(20000000l));
		a.getSchedule().setHideUntilOpen(false);
		a.getSchedule().setOpen(new Date(10000000l));
		a.getSchedule().setUntil(new Date(30000000l));
		// TODO: more settings

		Optional<Assessment> created = service.createAssessment(sub, context, user, a);

		// make sure it got created, and we got an assessment back
		Assertions.assertThat(created).isNotNull();
		Assertions.assertThat(created).isPresent();

		// make sure the thing we got back matches what we sent in (id, subscription, context, created, modified, will be set, asmt will be 'cleaned', though)
		a.setId(created.get().getId());
		a.setSubscription(created.get().getSubscription());
		a.setContext(created.get().getContext());
		a.getCreated().set(created.get().getCreated());
		a.getModified().set(created.get().getModified());
		Assertions.assertThat(created.get().getId()).isPositive();
		Assertions.assertThat(created.get().getTitle()).isEqualTo(title);
		Assertions.assertThat(created.get()).isEqualTo(a);

		// read it by id
		final long id = created.get().getId();
		Optional<Assessment> byId = service.getAssessment(id);

		// make sure it got found
		Assertions.assertThat(byId).isNotNull();
		Assertions.assertThat(byId).isPresent();

		// and that it matches
		Assertions.assertThat(byId.get()).isEqualTo(a);

	}
}
