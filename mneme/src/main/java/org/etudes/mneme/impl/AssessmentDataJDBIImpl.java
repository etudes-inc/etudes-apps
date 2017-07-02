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
import static org.etudes.apps.db.DB.fromOptionalDate;
import static org.etudes.apps.db.DB.toBoolean;
import static org.etudes.apps.db.DB.toDate;
import static org.etudes.apps.db.DB.toOptionalDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.etudes.apps.authentication.impl.AuthenticationDataJDBIImpl;
import org.etudes.apps.db.DB;
import org.etudes.apps.db.DB.Holder;
import org.etudes.mneme.data.AssessmentData;
import org.etudes.mneme.model.Assessment;
import org.etudes.mneme.model.AssessmentStatus;
import org.etudes.mneme.model.Attribution;
import org.etudes.mneme.model.Presentation;
import org.etudes.mneme.model.Schedule;
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
			final long id = r.getLong("id");
			final long subscription = r.getLong("subscription");
			final String contet = r.getString("context");
			final String title = r.getString("title");
			final String presentationText = r.getString("presentation_text");
			final Assessment.Type type = Assessment.Type.fromCode(r.getString("atype"));
			final boolean published = toBoolean(r.getInt("published"));
			final boolean valid = toBoolean(r.getInt("valid"));
			final Optional<Date> due = toOptionalDate(r.getLong("due"));
			final boolean hideUntilOpen = toBoolean(r.getInt("hideUntilOpen"));
			final Optional<Date> open = toOptionalDate(r.getLong("open"));
			final Optional<Date> until = toOptionalDate(r.getLong("until"));
			final long createdBy = r.getLong("created_by");
			final Date createdOn = toDate(r.getLong("created_on"));
			final long modifiedBy = r.getLong("modified_by");
			final Date modifiedOn = toDate(r.getLong("modified_on"));

			return new Assessment(contet, new Attribution(createdOn, createdBy), id, new Attribution(modifiedOn, modifiedBy), new ArrayList<>(),
					new Presentation(presentationText), new Schedule(due, hideUntilOpen, open, until), new AssessmentStatus(published, valid), subscription,
					title, type);
		}
	}

	final static private Logger logger = LoggerFactory.getLogger(AuthenticationDataJDBIImpl.class);

	protected DB db = null;

	@Inject
	public AssessmentDataJDBIImpl(DB db) {
		this.db = db;

		logger.info("AssessmentDataJDBIImpl() with DBI: " + this.db);

		if (this.db.isAutoDdl()) {
			this.createTables();
		}
	}

	@Override
	public Optional<Assessment> create(Assessment asmt) {
		Holder<Assessment> rv = new Holder<>();

		this.db.transact(h -> {
			Long id = h.createStatement(
					"insert into assessment (subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on) " //
							+ "values (:subscription, :context, :title, :presentation_text, :atype, :published, :valid, :due, :hideUntilOpen, :open, :until, " //
							+ ":created_by, :created_on, :modified_by, :modified_on)")
					.bind("subscription", asmt.getSubscription()) //
					.bind("context", asmt.getContext()) //
					.bind("title", asmt.getTitle()) //
					.bind("presentation_text", asmt.getPresentation().getText()) //
					.bind("atype", asmt.getType().getCode()) //
					.bind("published", fromBoolean(asmt.getStatus().isPublished())) //
					.bind("valid", fromBoolean(asmt.getStatus().isValid())) //
					.bind("due", fromOptionalDate(asmt.getSchedule().getDue())) //
					.bind("hideUntilOpen", fromBoolean(asmt.getSchedule().isHideUntilOpen())) //
					.bind("open", fromOptionalDate(asmt.getSchedule().getOpen())) //
					.bind("until", fromOptionalDate(asmt.getSchedule().getUntil())) //
					.bind("created_by", asmt.getCreated().getUserId()) //
					.bind("created_on", fromDate(asmt.getCreated().getDate())) //
					.bind("modified_by", asmt.getModified().getUserId()) //
					.bind("modified_on", fromDate(asmt.getModified().getDate())) //
					.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE).first();

			Assessment a = h
					.createQuery("select id, subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", id) //
					.map(new AssessmentMapper()).first();

			rv.value = Optional.ofNullable(a);
		});

		return rv.value;
	}

	@Override
	public boolean delete(Assessment asmt) {
		Holder<Boolean> rv = new Holder<>();

		this.db.transact(h -> {
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

		this.db.transact(h -> {
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

		this.db.transact(h -> {
			Assessment a = h
					.createQuery("select id, subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", id).map(new AssessmentMapper()) //
					.first();

			rv.value = Optional.ofNullable(a);
		});

		return rv.value;
	}

	@Override
	public List<Assessment> readAssessments(long subscriptionId) {
		Holder<List<Assessment>> rv = new Holder<>();

		this.db.transact(h -> {
			List<Assessment> asmts = h
					.createQuery("select id, subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on from assessment where subscription = :subscription") //
					.bind("subscription", subscriptionId) //
					.map(new AssessmentMapper()) //
					.list();

			rv.value = Optional.ofNullable(asmts);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public List<Assessment> readAssessments(long subscriptionId, String context) {
		Holder<List<Assessment>> rv = new Holder<>();

		this.db.transact(h -> {
			List<Assessment> asmts = h
					.createQuery("select id, subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on from assessment where subscription = :subscription and context = :context") //
					.bind("subscription", subscriptionId) //
					.bind("context", context) //
					.map(new AssessmentMapper()) //
					.list();

			rv.value = Optional.ofNullable(asmts);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public Optional<Assessment> updateAssessment(Assessment old, Assessment updated) {
		Holder<Assessment> rv = new Holder<>();

		this.db.transact(h -> {
			h.createStatement("update assessment set " //
					+ "title = :title, "//
					+ "presentation_text = :presentation_text, " //
					+ "atype = :atype, " //
					+ "published = :published, "//
					+ "valid = :valid, "//
					+ "due = :due, "//
					+ "hideUntilOpen = :hideUntilOpen, "//
					+ "open = :open, "//
					+ "until = :until, "//
					+ "modified_by = :modified_by, "//
					+ "modified_on = :modified_on "//
					+ "where id = :id") //
					.bind("title", updated.getTitle()) //
					.bind("presentation_text", updated.getPresentation().getText()) //
					.bind("id", old.getId()) //
					.bind("atype", updated.getType().getCode()) //
					.bind("published", fromBoolean(updated.getStatus().isPublished())) //
					.bind("valid", fromBoolean(updated.getStatus().isValid())) //
					.bind("due", fromOptionalDate(updated.getSchedule().getDue())) //
					.bind("hideUntilOpen", fromBoolean(updated.getSchedule().isHideUntilOpen())) //
					.bind("open", fromOptionalDate(updated.getSchedule().getOpen())) //
					.bind("until", fromOptionalDate(updated.getSchedule().getUntil())) //
					.bind("modified_by", updated.getModified().getUserId()) //
					.bind("modified_on", fromDate(updated.getModified().getDate())) //
					.execute();

			Assessment a = h
					.createQuery("select id, subscription, context, title, presentation_text, atype, published, valid, due, hideUntilOpen, open, until, " //
							+ "created_by, created_on, modified_by, modified_on from assessment where id = :id") //
					.bind("id", old.getId()) //
					.map(new AssessmentMapper()).first();

			rv.value = Optional.ofNullable(a);
		});

		return rv.value;
	}

	/**
	 * Create our tables if needed.
	 */
	protected void createTables() {
		this.db.schedule(h -> {
			h.execute("create table if not exists assessment (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "subscription bigint unsigned not null," //
					+ "context varchar (255) not null," //
					+ "title varchar (255) not null," //
					+ "presentation_text longtext not null," //
					+ "atype char (1) not null," //
					+ "published tinyint not null," //
					+ "valid tinyint not null," //
					+ "due bigint," //
					+ "hideUntilOpen tinyint not null," //
					+ "open bigint," //
					+ "until bigint," //
					+ "created_by bigint not null," //
					+ "created_on bigint not null," //
					+ "modified_by bigint not null," //
					+ "modified_on bigint not null," //
					+ "key asmt_s_c (subscription, context)" //
					+ ")");
		});
	}
}
