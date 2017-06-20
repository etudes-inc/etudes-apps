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

package org.etudes.apps.authentication.impl;

import static org.etudes.apps.db.DB.toDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.etudes.apps.authentication.data.AuthenticationData;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.db.DB;
import org.etudes.apps.db.DB.Holder;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.LongColumnMapper;
import org.skife.jdbi.v2.util.StringColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationDataJDBIImpl implements AuthenticationData {
	/**
	 * Map a result set to an Authentication object.
	 */
	protected class AuthenticationMapper implements ResultSetMapper<Authentication> {
		@Override
		public Authentication map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Authentication rv = new Authentication(r.getLong("id"), r.getLong("browser"), r.getString("context"), toDate(r.getLong("date")), r.getString("ip"),
					r.getString("placement"), r.getString("role"), userOf(r.getLong("user")));
			return rv;
		}

		protected User userOf(Long id) {

			Optional<User> user = userService.getUser(id);
			if (user.isPresent()) {
				return user.get();
			} else {
				// TODO: what to do if we don't find a user?
				logger.warn("userOf: not found: " + id);
				return null;
			}
		}
	}

	final static private Logger logger = LoggerFactory.getLogger(AuthenticationDataJDBIImpl.class);

	protected DB db = null;

	protected UserService userService;

	@Inject
	public AuthenticationDataJDBIImpl(DB db, UserService userService) {
		this.db = db;
		this.userService = userService;

		logger.info("UserDataJDBIImpl() with DBI: " + this.db);

		if (db.isAutoDdl()) {
			createTables();
		}
	}

	@Override
	public Optional<Authentication> createAuthentication(User user, String ipAddress, Long browser, String context, String role, String placement, Date date) {
		Holder<Authentication> rv = new Holder<>();

		db.transact(h -> {
			Long id = h
					.createStatement("insert into authentication (browser, context, date, ip, placement, role, user)" //
							+ " values (:browser, :context, :date, :ip, :placement, :role, :user)")
					.bind("browser", browser) //
					.bind("context", context) //
					.bind("date", date.getTime()) //
					.bind("ip", ipAddress) //
					.bind("placement", placement) //
					.bind("role", role) //
					.bind("user", user.get_id()) //
					.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE).first();

			Authentication auth = h.createQuery("select id, browser, context, date, ip, placement, role, user from authentication where id = :id") //
					.bind("id", id) //
					.map(new AuthenticationMapper()).first();

			rv.value = Optional.ofNullable(auth);
		});

		return rv.value;
	}

	@Override
	public Optional<Long> createBrowser(String browserUserAgent) {
		Holder<Long> rv = new Holder<>();

		// first try a read
		rv.value = readBrowser(browserUserAgent);
		if (rv.value.isPresent())
			return rv.value;

		db.transact(h -> {
			// to assure we have a browser record only for new agents, and in a race we don't get a failure, insert ignore first, then read if needed

			Long id = null;
			// insert ignore is not supported by H2 so is not testable - instead, just catch the exception when the browser record already exists
			try {
				id = h.createStatement("insert into browser (agent) values (:agent)") //
						.bind("agent", browserUserAgent) //
						.executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE).first();
			} catch (UnableToExecuteStatementException e) {
			}

			// if this did not create, we need to read
			if (id == null) {
				id = h.createQuery("select id from browser where agent = :agent") //
						.bind("agent", browserUserAgent) //
						.map(LongColumnMapper.PRIMITIVE) //
						.first();
			}

			rv.value = Optional.ofNullable(id);
		});

		return rv.value;
	}

	@Override
	public boolean deleteAuthentications(Long subscriptionId) {
		Holder<Boolean> rv = new Holder<>();

		db.transact(h -> {
			// see org.etudes.apps.dw.RequestLogJDBIFactory - deleting access for the authentication
			h.createStatement(
					"delete access from access join authentication on access.authentication = authentication.id join user on authentication.user = user.id where user.subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();
			h.createStatement("delete authentication from authentication join user on authentication.user = user.id where user.subscription = :subscriptionId") //
					.bind("subscriptionId", subscriptionId) //
					.execute();

			rv.value = Optional.ofNullable(Boolean.TRUE);
		});

		return rv.value.orElse(Boolean.FALSE);
	}

	@Override
	public Optional<Authentication> readAuthentication(Long id) {
		Holder<Authentication> rv = new Holder<>();

		db.transact(h -> {
			Authentication auth = h.createQuery("select id, browser, context, date, ip, placement, role, user from authentication where id = :id") //
					.bind("id", id).map(new AuthenticationMapper()) //
					.first();

			rv.value = Optional.ofNullable(auth);
		});

		return rv.value;
	}

	@Override
	public List<Authentication> readAuthentications(Long subscriptionId) {
		Holder<List<Authentication>> rv = new Holder<>();

		db.transact(h -> {
			List<Authentication> auths = h
					.createQuery("select authentication.id, authentication.browser, authentication.context, authentication.date, authentication.ip, " //
							+ "authentication.placement, authentication.role, authentication.user " //
							+ "from authentication join user on authentication.user = user.id where user.subscription = :subscription") //
					.bind("subscription", subscriptionId) //
					.map(new AuthenticationMapper()) //
					.list();

			rv.value = Optional.ofNullable(auths);
		});

		return rv.value.orElse(new ArrayList<>());
	}

	@Override
	public Optional<String> readBrowser(Long id) {
		Holder<String> rv = new Holder<>();

		db.transact(h -> {
			String browser = h.createQuery("select agent from browser where id = :id") //
					.bind("id", id) //
					.map(StringColumnMapper.INSTANCE) //
					.first();

			rv.value = Optional.ofNullable(browser);
		});

		return rv.value;
	}

	@Override
	public Optional<Long> readBrowser(String browserUserAgent) {
		Holder<Long> rv = new Holder<>();

		db.transact(h -> {
			Long id = h.createQuery("select id from browser where agent = :agent") //
					.bind("agent", browserUserAgent) //
					.map(LongColumnMapper.PRIMITIVE) //
					.first();

			rv.value = Optional.ofNullable(id);
		});

		return rv.value;
	}

	/**
	 * Create our tables if needed.
	 */
	protected void createTables() {
		db.schedule(h -> {
			h.execute("create table if not exists authentication (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "browser bigint unsigned not null," //
					+ "context varchar (255) not null," //
					+ "date bigint not null," //
					+ "ip varchar (39) not null," // large enough for ipv6
					+ "placement varchar (255)," //
					+ "role varchar (255) not null," //
					+ "user bigint unsigned not null" //
					+ ")"); // key authentication_u (user)

			h.execute("create table if not exists browser (" //
					+ "id bigint unsigned auto_increment not null primary key," //
					+ "agent varchar (255) not null," //
					+ "unique key browser_agent(agent)" //
					+ ")");
		});
	}
}
