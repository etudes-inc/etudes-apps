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

package org.etudes.apps.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.etudes.apps.db.DB;
import org.etudes.apps.db.DB.Holder;
import org.etudes.apps.user.data.UserData;
import org.etudes.apps.user.model.Login;
import org.etudes.apps.user.model.Tokens;
import org.etudes.apps.user.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.LongColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDataJDBIImpl implements UserData {

	/**
	 * Map a result set to a Login object.
	 */
	protected class LoginMapper implements ResultSetMapper<Login> {
		@Override
		public Login map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Login rv = new Login(r.getLong("id"), r.getString("email"), r.getString("password"), r.getLong("user"));
			return rv;
		}
	}

	/**
	 * Map a result set to a Token object.
	 */
	protected class TokensMapper implements ResultSetMapper<Tokens> {
		@Override
		public Tokens map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Tokens rv = new Tokens(r.getLong("id"), r.getString("lms_refresh"), r.getString("lms_token"), r.getLong("user"));
			return rv;
		}
	}

	/**
	 * Map a result set to a User object - minimal fields.
	 */
	protected class UserMapperMin implements ResultSetMapper<User> {
		@Override
		public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			User rv = new User(r.getLong("id"), r.getString("lms_id"), r.getLong("subscription"));

			return rv;
		}
	}

	/**
	 * Map a result set to a User object - minimal fields.
	 */
	protected class UserMapperAll implements ResultSetMapper<User> {
		@Override
		public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			User rv = new User(r.getLong("id"), r.getString("email"), r.getString("name_family"), r.getString("name_given"), r.getString("lms_id"), false,
					r.getString("sis_id"), r.getLong("subscription"), false);

			return rv;
		}
	}

	/**
	 * Map a result set to a User object, setting loginSet and tokensSet.
	 */
	protected class UserMapperFull implements ResultSetMapper<User> {
		@Override
		public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			User rv = new User(r.getLong("id"), r.getString("email"), r.getString("name_family"), r.getString("name_given"), r.getString("lms_id"),
					r.getLong("loginId") != 0L, r.getString("sis_id"), r.getLong("subscription"), r.getLong("tokensId") != 0L);
			return rv;
		}
	}

	final static private Logger logger = LoggerFactory.getLogger(UserDataJDBIImpl.class);

	protected DB db = null;

	@Inject
	public UserDataJDBIImpl(DB db) {
		this.db = db;
		logger.info("UserDataJDBIImpl() with DB: " + this.db);

		if (db.isAutoDdl()) {
			createTables();
		}
	}

	@Override
	public void createOrUpdateLogin(Login login) {
		db.transact(h -> {
			if ((login.get_id() == null) || (login.get_id().longValue() < 0L)) {
				Long id = h.createStatement("insert into login (email, password, user) values (:email, :password, :user)") //
						.bind("email", login.getEmail()) //
						.bind("password", login.getPassword()) //
						.bind("user", login.getUserId()) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();
				login.set_id(id);
			} else {
				h.createStatement("update login set email = :email, password = :password where user = :user") //
						.bind("email", login.getEmail()) //
						.bind("password", login.getPassword()) //
						.bind("user", login.getUserId()) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();
			}
		});
	}

	@Override
	public void createOrUpdateTokens(Tokens tokens) {
		db.transact(h -> {
			if ((tokens.get_id() == null) || (tokens.get_id().longValue() < 0L)) {
				Long id = h.createStatement("insert into tokens (lms_refresh, lms_token, user) values (:lms_refresh, :lms_token, :user)") //
						.bind("lms_refresh", tokens.getLmsRefresh()) //
						.bind("lms_token", tokens.getLmsToken()) //
						.bind("user", tokens.getUserId()) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();
				tokens.set_id(id);
			} else {
				h.createStatement("update tokens set lms_refresh = :lms_refresh, lms_token = :lms_token where user = :user") //
						.bind("lms_refresh", tokens.getLmsRefresh()) //
						.bind("lms_token", tokens.getLmsToken()) //
						.bind("user", tokens.getUserId()) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();
			}
		});
	}

	@Override
	public void deleteSubscriptionUsers(Long subscriptionId) {
		db.transact(h -> {
			h.createStatement("delete tokens from tokens join user on tokens.user = user.id where user.subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();

			h.createStatement("delete login from login join user on login.user = user.id where user.subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();

			h.createStatement("delete from user where subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();
		});
	}

	@Override
	public void deleteTokens(Long id) {
		db.transact(h -> {
			h.createStatement("delete tokens from tokens join user on tokens.user = user.id where user.id = :id") //
					.bind("id", id) //
					.execute();
		});
	}

	@Override
	public void deleteUser(Long id) {
		db.transact(h -> {
			h.createStatement("delete tokens from tokens join user on tokens.user = user.id where user.id = :id") //
					.bind("id", id) //
					.execute();

			h.createStatement("delete login from login join user on login.user = user.id where user.id = :id") //
					.bind("id", id) //
					.execute();

			h.createStatement("delete from user where id = :id") //
					.bind("id", id) //
					.execute();
		});
	}

	@Override
	public Optional<User> readCreateUser(Long subscriptionId, String lmsId) {
		Holder<User> rv = new Holder<>();

		// to read the created record
		String sqlRead = "select id, lms_id, subscription from user where subscription = :subscription and lms_id "
				+ ((lmsId == null) ? "is null" : "= :lms_id");

		db.transact(h -> {
			User user = h.createQuery(sqlRead) //
					.bind("subscription", subscriptionId) //
					.bind("lms_id", lmsId) //
					.map(new UserMapperMin()) //
					.first();

			// insert if not found
			if (user == null) {
				// attempt to insert the user - let it fail
				try {
					h.createStatement("insert into user (lms_id, subscription) values (:lms_id, :subscription)") //
							.bind("lms_id", lmsId) //
							.bind("subscription", subscriptionId) //
							.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
							.first();
				} catch (UnableToExecuteStatementException e) {
				}

				user = h.createQuery(sqlRead) //
						.bind("subscription", subscriptionId) //
						.bind("lms_id", lmsId) //
						.map(new UserMapperMin()) //
						.first();
			}

			rv.value = Optional.ofNullable(user);
		});

		return rv.value;
	}

	@Override
	public Optional<Login> readLogin(Long id) {
		Holder<Login> rv = new Holder<>();

		db.transact(h -> {
			Login login = h.createQuery("select id, email, password, user from login where user = :id") //
					.bind("id", id) //
					.map(new LoginMapper()) //
					.first();

			rv.value = Optional.ofNullable(login);
		});

		return rv.value;
	}

	@Override
	public List<Login> readLoginsByEmail(String email) {
		Holder<List<Login>> rv = new Holder<>();

		db.transact(h -> {
			List<Login> logins = h.createQuery("select id, email, password, user from login where email = :email") //
					.bind("email", email) //
					.map(new LoginMapper()) //
					.list();

			rv.value = Optional.ofNullable(logins);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public Optional<Tokens> readTokens(Long id) {
		Holder<Tokens> rv = new Holder<>();

		db.transact(h -> {
			Tokens tokens = h.createQuery("select id, lms_refresh, lms_token, user from tokens where user = :id") //
					.bind("id", id) //
					.map(new TokensMapper()) //
					.first();

			rv.value = Optional.ofNullable(tokens);
		});

		return rv.value;
	}

	@Override
	public Optional<User> readUser(Long id) {
		Holder<User> rv = new Holder<>();

		db.transact(h -> {
			User user = h.createQuery("select id, email, name_family, name_given, lms_id, sis_id, subscription from user where id = :id") //
					.bind("id", id) //
					.map(new UserMapperAll()) //
					.first();

			rv.value = Optional.ofNullable(user);
		});

		return rv.value;
	}

	@Override
	public List<User> readUsersForSubscription(Long subscriptionId) {
		Holder<List<User>> rv = new Holder<>();

		db.transact(h -> {
			List<User> users = h.createQuery(
					"select U.id, U.email, U.name_family, U.name_given, U.lms_id, U.sis_id, U.subscription, L.id as loginId, T.id as tokensId from user U " //
							+ "left outer join login L on U.id = L.user left outer join tokens T on U.id = T.user " //
							+ "where subscription = :subscription") //
					.bind("subscription", subscriptionId) //
					.map(new UserMapperFull()) //
					.list();

			rv.value = Optional.ofNullable(users);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public Optional<Tokens> refreshOrUpdateTokens(Tokens tokens, Consumer<Holder<String>> job) {
		Holder<Tokens> rv = new Holder<>();

		db.transact(h -> {

			// read, locking
			// Long start = System.currentTimeMillis();
			// logger.info("refreshOrUpdateTokens: select for update ...");
			Tokens latest = h.createQuery("select id, lms_refresh, lms_token, user from tokens where id = :id for update") //
					.bind("id", tokens.get_id()) //
					.map(new TokensMapper()) //
					.first();
			// logger.info("refreshOrUpdateTokens: select for update: done: " + (System.currentTimeMillis() - start));

			// if the token has changed, return this
			if (!latest.getLmsToken().equals(tokens.getLmsToken())) {
				// logger.info("refreshOrUpdateTokens: found new token");
				rv.value = Optional.of(latest);
			}

			// if we had the latest, run the job with our holder, then, if we get a new token, update the db
			else {
				Holder<String> newToken = new Holder<>();

				job.accept(newToken);

				// if we got a new token, update the db
				if (newToken.value.isPresent()) {
					// logger.info("refreshOrUpdateTokens: update db");
					h.createStatement("update tokens set lms_token = :lms_token where id = :id") //
							.bind("lms_token", newToken.value.get()) //
							.bind("id", tokens.get_id()) //
							.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
							.first();

					// return the updated tokens
					tokens.setLmsToken(newToken.value.get());
					rv.value = Optional.of(tokens);
				}
			}
		});

		// logger.info("refreshOrUpdateTokens: transaction done");
		return rv.value;
	}

	@Override
	public void updateUser(User user) {
		db.transact(h -> {
			h.createStatement(
					"update user set email = :email, name_family = :name_family, name_given = :name_given, lms_id = :lms_id, sis_id = :sis_id where id = :id") //
					.bind("email", user.getEmail()) //
					.bind("name_family", user.getFamilyName()) //
					.bind("name_given", user.getGivenName()) //
					.bind("lms_id", user.getLmsId()) //
					.bind("sis_id", user.getSisId()) //
					.bind("id", user.get_id()) //
					.execute();
		});
	}

	/**
	 * Create our table if needed.
	 */
	protected void createTables() {
		db.schedule(h -> {
			h.execute("create table if not exists user (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "email varchar (255)," //
					+ "name_family varchar (255)," //
					+ "name_given varchar (255)," //
					+ "lms_id varchar (255)," //
					+ "sis_id varchar (255)," //
					+ "subscription bigint unsigned not null," //
					+ "unique key user_s_lid (subscription, lms_id)" //
					+ ")");

			h.execute("create table if not exists login (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "email varchar (255) not null," //
					+ "password varchar (255) not null," //
					+ "user bigint unsigned not null," //
					+ "unique key login_u (user)," //
					+ "key login_e (email)" //
					+ ")");

			h.execute("create table if not exists tokens (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "lms_refresh varchar (255) not null," //
					+ "lms_token varchar (255) not null," //
					+ "user bigint unsigned not null," //
					+ "unique key tokens_u (user)" //
					+ ")");

			// load initial data
			try {
				h.begin();

				Long uid = h.createStatement("insert into user (id, subscription) values (:id, :subscription)") //
						.bind("id", 1L) //
						.bind("subscription", 1L) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();

				h.createStatement("insert into login (id, email, password, user) values (:id, :email, :password, :user)") //
						.bind("id", 1L) //
						.bind("email", "admin@etudes.org") //
						.bind("password", "rEFrX5bUuAkzQQ2ULinR/Q==") //
						.bind("user", uid) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE) //
						.first();

				h.commit();
			} catch (Throwable t) {
				h.rollback();
			}
		});
	}
}
