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

package org.etudes.mneme.impl;

import static org.etudes.apps.db.DB.fromBoolean;
import static org.etudes.apps.db.DB.fromDate;
import static org.etudes.apps.db.DB.toBoolean;
import static org.etudes.apps.db.DB.toDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.etudes.apps.authentication.impl.AuthenticationDataJDBIImpl;
import org.etudes.apps.db.DB;
import org.etudes.apps.db.DB.Holder;
import org.etudes.mneme.data.AssessmentData;
import org.etudes.mneme.model.Assessment;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.LongColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssessmentDataJDBIImpl implements AssessmentData {

	/**
	 * Map a result set to an Assessment object.
	 */
	protected class AssessmentMapper implements ResultSetMapper<Assessment> {
		@Override
		public Assessment map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Assessment rv = new Assessment();
			rv.setId(r.getLong("id"));
			rv.setSubscription(r.getLong("subscription"));
			rv.setContext(r.getString("context"));
			rv.setTitle(r.getString("title"));
			rv.setType(Assessment.Type.fromCode(r.getString("atype")));
			rv.getStatus().setPublished(toBoolean(r.getInt("published")));
			rv.getSchedule().setDue(toDate(r.getLong("due")));
			rv.getSchedule().setHideUntilOpen(toBoolean(r.getInt("hideUntilOpen")));
			rv.getSchedule().setOpen(toDate(r.getLong("open")));
			rv.getSchedule().setUntil(toDate(r.getLong("until")));
			rv.getCreated().setUserId(r.getLong("created_by"));
			rv.getCreated().setDate(toDate(r.getLong("created_on")));
			rv.getModified().setUserId(r.getLong("modified_by"));
			rv.getModified().setDate(toDate(r.getLong("modified_on")));
			return rv;
		}
	}

	final static private Logger logger = LoggerFactory.getLogger(AuthenticationDataJDBIImpl.class);

	protected DB db = null;

	@Inject
	public AssessmentDataJDBIImpl(DB db) {
		this.db = db;

		logger.info("AssessmentDataJDBIImpl() with DBI: " + this.db);

		if (db.isAutoDdl()) {
			createTables();
		}
	}

	@Override
	public Optional<Assessment> create(Assessment asmt) {
		Holder<Assessment> rv = new Holder<>();

		db.transact(h -> {
			Long id = h.createStatement(
					"insert into assessment (subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on)" //
							+ " values (:subscription, :context, :title, :atype, :published, :due, :hideUntilOpen, :open, :until, :created_by, :created_on, :modified_by, :modified_on)")
					.bind("subscription", asmt.getSubscription()) //
					.bind("context", asmt.getContext()) //
					.bind("title", asmt.getTitle()) //
					.bind("atype", asmt.getType().getCode()) //
					.bind("published", fromBoolean(asmt.getStatus().isPublished())) //
					.bind("due", fromDate(asmt.getSchedule().getDue())) //
					.bind("hideUntilOpen", fromBoolean(asmt.getSchedule().isHideUntilOpen())) //
					.bind("open", fromDate(asmt.getSchedule().getOpen())) //
					.bind("until", fromDate(asmt.getSchedule().getUntil())) //
					.bind("created_by", asmt.getCreated().getUserId()) //
					.bind("created_on", fromDate(asmt.getCreated().getDate())) //
					.bind("modified_by", asmt.getModified().getUserId()) //
					.bind("modified_on", fromDate(asmt.getModified().getDate())) //
					.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE).first();

			Assessment auth = h.createQuery(
					"select id, subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", id) //
					.map(new AssessmentMapper()).first();

			rv.value = Optional.ofNullable(auth);
		});

		return rv.value;
	}

	@Override
	public boolean delete(Assessment asmt) {
		Holder<Boolean> rv = new Holder<>();

		db.transact(h -> {
			h.createStatement("delete from assessment where id = :id") //
					.bind("id", asmt.getId()) //
					.execute();

			rv.value = Optional.ofNullable(Boolean.TRUE);
		});

		return rv.value.orElse(Boolean.FALSE);
	}

	@Override
	public boolean deleteAssessments(long subscriptionId) {
		Holder<Boolean> rv = new Holder<>();

		db.transact(h -> {
			h.createStatement("delete from assessment where subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();

			rv.value = Optional.ofNullable(Boolean.TRUE);
		});

		return rv.value.orElse(Boolean.FALSE);
	}

	@Override
	public Optional<Assessment> readAssessment(long id) {
		Holder<Assessment> rv = new Holder<>();

		db.transact(h -> {
			Assessment a = h.createQuery(
					"select id, subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", id).map(new AssessmentMapper()) //
					.first();

			rv.value = Optional.ofNullable(a);
		});

		return rv.value;
	}

	@Override
	public List<Assessment> readAssessments(long subscriptionId) {
		Holder<List<Assessment>> rv = new Holder<>();

		db.transact(h -> {
			List<Assessment> auths = h.createQuery(
					"select id, subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on from assessment where subscription = :subscription") //
					.bind("subscription", subscriptionId) //
					.map(new AssessmentMapper()) //
					.list();

			rv.value = Optional.ofNullable(auths);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public List<Assessment> readAssessments(long subscriptionId, String context) {
		Holder<List<Assessment>> rv = new Holder<>();

		db.transact(h -> {
			List<Assessment> auths = h.createQuery(
					"select id, subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on from assessment where subscription = :subscription and context = :context") //
					.bind("subscription", subscriptionId) //
					.bind("context", context) //
					.map(new AssessmentMapper()) //
					.list();

			rv.value = Optional.ofNullable(auths);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public Optional<Assessment> updateAssessment(Assessment old, Assessment updated) {
		Holder<Assessment> rv = new Holder<>();

		// Node:id, subscription and ??? context ??? don't change - TODO: context might

		// TODO: more fields...

		db.transact(h -> {
			h.createStatement("update assessment set " //
					+ "title = :title, "//
					+ "atype = :atype, " //
					+ "published = :published, "//
					+ "due = :due, "//
					+ "hideUntilOpen = :hideUntilOpen, "//
					+ "open = :open, "//
					+ "until = :until, "//
					+ "created_by = :created_by, "//
					+ "created_on = :created_on, "//
					+ "modified_by = :modified_by, "//
					+ "modified_on = :modified_on "//
					+ "where id = :id") //
					.bind("title", updated.getTitle()) //
					.bind("id", old.getId()) //
					.bind("atype", updated.getType().getCode()) //
					.bind("published", fromBoolean(updated.getStatus().isPublished())) //
					.bind("due", fromDate(updated.getSchedule().getDue())) //
					.bind("hideUntilOpen", fromBoolean(updated.getSchedule().isHideUntilOpen())) //
					.bind("open", fromDate(updated.getSchedule().getOpen())) //
					.bind("until", fromDate(updated.getSchedule().getUntil())) //
					.bind("created_by", updated.getCreated().getUserId()) //
					.bind("created_on", fromDate(updated.getCreated().getDate())) //
					.bind("modified_by", updated.getModified().getUserId()) //
					.bind("modified_on", fromDate(updated.getModified().getDate())) //
					.execute();

			Assessment auth = h.createQuery(
					"select id, subscription, context, title, atype, published, due, hideUntilOpen, open, until, created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", old.getId()) //
					.map(new AssessmentMapper()).first();

			rv.value = Optional.ofNullable(auth);
		});

		return rv.value;
	}

	/**
	 * Create our tables if needed.
	 */
	protected void createTables() {
		db.schedule(h -> {
			h.execute("create table if not exists assessment (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "subscription bigint unsigned not null," //
					+ "context varchar (255) not null," //
					+ "title varchar (255)," //
					+ "atype char (1)," //
					+ "published tinyint not null," //
					+ "due bigint not null," //
					+ "hideUntilOpen tinyint not null," //
					+ "open bigint not null," //
					+ "until bigint not null," //
					+ "created_by bigint not null," //
					+ "created_on bigint not null," //
					+ "modified_by bigint not null," //
					+ "modified_on bigint not null," //
					+ "key asmt_s_c (subscription, context)" //
					+ ")");
		});
	}
}
