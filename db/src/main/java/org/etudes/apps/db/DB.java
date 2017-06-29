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

package org.etudes.apps.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {

	/**
	 * Represents an operation that accepts a single input argument and returns no result. Unlike most other functional interfaces, {@code Consumer} is expected
	 * to operate via side-effects.
	 *
	 * <p>
	 * This is a <a href="package-summary.html">functional interface</a> whose functional method is {@link #accept(Object)}.
	 *
	 * @param <T>
	 *            the type of the input to the operation
	 *
	 * @since 1.8
	 */
	@FunctionalInterface
	public interface ConsumerThrowing<T> {

		/**
		 * Performs this operation on the given argument.
		 *
		 * @param t
		 *            the input argument
		 */
		void accept(T t) throws DBIException;
	}

	/**
	 * A way to hold and update a value that can be made final and used in a lambda.
	 * 
	 * @param <T>
	 */
	public static class Holder<T> {
		public Optional<T> value = Optional.empty();
	}

	final static private Logger logger = LoggerFactory.getLogger(DB.class);

	/** How many retries we attempt before giving up, in transact(), in case of db down or SQLException deadlock. */
	protected final static int MAX_RETRIES = 5;

	/**
	 * Safe way to prepare a boolean for writing to the database (as a mysql tinyint 0/1).
	 * 
	 * @param b
	 *            The boolean.
	 * @return The value to write to the database.
	 */
	public static Integer fromBoolean(Boolean b) {
		return (b == null) ? 0 : (b ? 1 : 0);
	}

	/**
	 * Safe way to prepare a date for writing to the database (as a mysql bigint).
	 * 
	 * @param d
	 *            The date.
	 * @return The value to write to the database.
	 */
	public static Long fromDate(Date d) {
		if (d == null)
			return null;
		return d.getTime();
	}

	/**
	 * Safe way to prepare a date for writing to the database (as a mysql bigint).
	 * 
	 * @param d
	 *            The date.
	 * @return The value to write to the database.
	 */
	public static Long fromOptionalDate(Optional<Date> d) {
		if (!d.isPresent())
			return null;
		return d.get().getTime();
	}

	/**
	 * Read a value from the db into a Boolean.
	 * 
	 * @param value
	 *            The (int 0:false, 1:true) value.
	 * @return The Boolean value.
	 */
	public static Boolean toBoolean(int value) {
		return value == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * Read a value from the db into a Date. A 0 is null.
	 * 
	 * @param value
	 *            The (Long) value.
	 * @return The Date value.
	 */
	public static Date toDate(long value) {
		if (value == 0)
			return null;
		return new Date(value);
	}

	/**
	 * Read a value from the db into a Long. 0 is treated as null.
	 * 
	 * @param value
	 *            The (Long) value.
	 * @return The Date value.
	 */
	public static Long toLong(long value) {
		if (value == 0)
			return null;
		return Long.valueOf(value);
	}

	/**
	 * Read a value from the db into a Date. A 0 is null.
	 * 
	 * @param value
	 *            The (Long) value.
	 * @return The Date value.
	 */
	public static Optional<Date> toOptionalDate(long value) {
		if (value == 0)
			return Optional.empty();
		return Optional.of(new Date(value));
	}

	/** The AutoDDL setting. */
	protected final boolean autoDdl;

	/** The wrapped DBI. */
	protected final DBI dbi;

	protected List<Consumer<Handle>> deferredJobs = Collections.synchronizedList(new ArrayList<>());

	protected Boolean status = Boolean.FALSE;

	public DB(DBI dbi, boolean autoDdl) {
		this.autoDdl = autoDdl;
		this.dbi = dbi;
	}

	/**
	 * Is autoDDL enabled?
	 * 
	 * @return true if autoDDL, false if not.
	 */
	public boolean isAutoDdl() {
		return this.autoDdl;
	}

	/**
	 * Check if we have a good database connection.
	 * 
	 * @return TRUE if we do, FALSE if not.
	 */
	public Boolean isUp() {
		synchronized (status) {
			return status;
		}
	}

	public Optional<Handle> open() {
		try {
			Handle h = dbi.open();

			// we have a handle, see if we need to run deferred jobs
			runDeferred(h);

			// we are 'up'
			setUp();

			return Optional.of(h);
		} catch (UnableToObtainConnectionException e) {
			// we are 'down'
			setDown();

			return Optional.empty();
		}
	}

	/**
	 * Run this job now, if we can get a database handle.
	 * 
	 * @param job
	 *            The job.
	 * @return true if run, false if not.
	 */
	public boolean run(Consumer<Handle> job) {
		Optional<Handle> handle = open();
		if (handle.isPresent()) {
			try (Handle h = handle.get()) {
				job.accept(h);
			} catch (DBIException e) {
				logger.info(e.toString());
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Run this job now, if we can, or later, once we have a database handle.
	 * 
	 * @param job
	 *            The job.
	 * @return true if run now, false if schedule for later run.
	 */
	public boolean schedule(Consumer<Handle> job) {
		Optional<Handle> handle = open();
		if (handle.isPresent()) {
			try (Handle h = handle.get()) {
				job.accept(h);
			} catch (DBIException e) {
				logger.info(e.toString());
				// TODO: should we make this deferred, and then return false?
			}
			return true;
		} else {
			deferredJobs.add(job);
			return false;
		}
	}

	/**
	 * Run this job now, if we can get a database handle, and in case of deadlock error, retry a few times before giving up.
	 * 
	 * @param job
	 *            The job. All transaction code is added to the basic statements in the job, and should NOT be in the job. Job may run multiple times.
	 * @return true if run successfully, false if not.
	 */
	public boolean transact(ConsumerThrowing<Handle> job) {
		boolean success = false;

		// we will do MAX_RETRIES retries in case of deadlock or db not available
		int retries = MAX_RETRIES;
		while (retries > 0) {
			Optional<Handle> handle = open();
			if (handle.isPresent()) {
				try (Handle h = handle.get()) {
					try {
						h.begin();
						job.accept(h);
						h.commit();
						retries = 0;
						success = true;
					} catch (DBIException e) {
						logger.info("transact: retry: " + (MAX_RETRIES - retries) + " " + e.toString());
						if (e.getCause() instanceof SQLException) {
							SQLException s = (SQLException) e.getCause();

							if (s.getErrorCode() == 1213) {
								// a mysql deadlock killed the transaction, so we might retry
								retries--;
							} else {
								// some other error, we are done
								retries = 0;
							}
						}
						h.rollback();
					} catch (Throwable t) {
						logger.info("transact: fatal: " + (MAX_RETRIES - retries) + " " + t.toString());
						h.rollback();

						// we don't try again
						retries = 0;
					}
				}
			} else {
				// try again like a deadlock
				logger.info("transact: retry: " + (MAX_RETRIES - retries) + " DB Down");
				retries--;
			}

			// if we are retrying, take a quick break first
			if (retries > 0) {
				// sleep 1 .. 4 seconds, depending on how many retries we've done.
				try {
					Thread.sleep((5 - retries) * 1000);
				} catch (InterruptedException ie) {
				}
			}
		}

		return success;
	}

	/**
	 * Run the deferred jobs on this handle.
	 * 
	 * @param handle
	 *            The database handle.
	 */
	protected void runDeferred(Handle h) {

		// grab the current list of jobs, clearing it
		List<Consumer<Handle>> jobs = new ArrayList<>();
		synchronized (deferredJobs) {
			jobs.addAll(deferredJobs);
			deferredJobs.clear();
		}

		// run each
		jobs.stream().forEach(j -> j.accept(h));
	}

	protected void setDown() {
		synchronized (status) {
			if (status) {
				logger.error("DB Down");
				status = Boolean.FALSE;
			}
		}
	}

	protected void setUp() {
		synchronized (status) {
			if (!status) {
				logger.warn("DB Up");
				status = Boolean.TRUE;
			}
		}
	}
}
