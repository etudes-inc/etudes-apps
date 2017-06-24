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

package org.etudes.mneme.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssessmentAccess holds details of a single special access grant for select users to an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentAccess {
	/** The overridden accept until date. */
	protected Date acceptUntil = null;
	protected boolean acceptUntilOverride = false;

	/** The overridden due date. */
	protected Date due = null;
	protected boolean dueOverride = false;

	/** Overridden hide until open flag. */
	protected boolean hideUntilOpen = false;
	protected boolean hideUntilOpenOverride = false;

	protected String id = null;

	/** Overridden open date. */
	protected Date open = null;
	protected boolean openOverride = false;

	protected Changeable owner = null;
	/** Overridden password. */
	protected AssessmentPassword password = new AssessmentPassword();

	protected boolean passwordOverride = false;
	/** Overridden time limit. */
	protected Long timeLimit = null;

	protected boolean timeLimitOverride = false;

	/** Overridden tries. */
	protected Integer tries = null;
	protected boolean triesOverride = false;

	/** User ids for this access. */
	protected List<String> userIds = new ArrayList<String>();

	/**
	 * Construct.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	public AssessmentAccess(AssessmentAccess other) {
		set(other);
	}

	/**
	 * @param base
	 *            The base assessment dates.
	 * @return If overridden the override, else the base accept until date.
	 */
	public Date getEffectiveAcceptUntil(AssessmentDates base) {
		if (!this.acceptUntilOverride)
			return base.getAcceptUntil();

		return this.acceptUntil;
	}

	/**
	 * @param base
	 *            The base assessment dates.
	 * @return If overridden the override, else the base due date.
	 */
	public Date getEffectiveDue(AssessmentDates base) {
		if (!this.dueOverride)
			return base.getDue();

		return this.due;
	}

	/**
	 * @param base
	 *            The base assessment dates.
	 * @return If overridden the override, else the base open date.
	 */
	public Date getEffectiveOpen(AssessmentDates base) {
		if (!this.openOverride)
			return base.getOpen();

		return this.open;
	}

	/**
	 * @param base
	 *            The base password.
	 * @return If overridden the override, else the base password.
	 */
	public AssessmentPassword getEffectivePassword(AssessmentPassword base) {
		if (!this.passwordOverride)
			return base;

		return this.password;
	}

	/**
	 * @param base
	 *            The base password.
	 * @return If overridden the override, else the base password value.
	 */
	public String getEffectivePasswordValue(AssessmentPassword base) {
		if (!this.passwordOverride)
			return base.getPassword();

		return this.password.getPassword();
	}

	/**
	 * @param base
	 *            The base time limit.
	 * @return If overridden the override, else the base password value.
	 */
	public Long getEffectiveTimeLimit(Long base) {
		if (!this.timeLimitOverride)
			return base;

		return this.timeLimit;
	}

	/**
	 * @param base
	 *            The base tries limit.
	 * @return If overridden the override, else the base password value.
	 */
	public Integer getEffectiveTries(Integer base) {
		if (!this.triesOverride)
			return base;

		return this.tries;
	}

	/**
	 * @param base
	 *            The base assessment hide until open flag.
	 * @return If overridden the override, else the base hide until open flag.
	 */
	public boolean isEffectiveHideUntilOpen(boolean base) {
		if (!this.hideUntilOpenOverride)
			return base;

		return this.hideUntilOpen;
	}

	/**
	 * @param base
	 *            The base assessment time limit.
	 * @return If overridden the override, else the presence of the base time limit.
	 */
	public boolean isEffectiveTimeLimitSet(Long base) {
		if (!this.timeLimitOverride)
			return base != null;

		return this.timeLimit != null;
	}

	/**
	 * @param base
	 *            The base assessment tries limit.
	 * @return If overridden the override, else the presence of the base tries limit.
	 */
	public Boolean isEffectiveTriesLimitSet(Integer base) {
		if (!this.triesOverride)
			return base != null;

		return this.tries != null;
	}

	/**
	 * @return if there are no users, or if the users are no longer in the site.
	 */
	public boolean isEffectiveUsersEmpty() {
		// TODO: big time!
		// if (this.userIds.size() == 0)
		// return Boolean.TRUE;
		// SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);
		//
		// Set<String> userStrs = securityService.getUsersIsAllowed(MnemeService.SUBMIT_PERMISSION, assessment.getContext());
		// List<User> users = UserDirectoryService.getUsers(userStrs);
		// userStrs.clear();
		// for (User user : users) {
		// userStrs.add(user.getId());
		// }
		//
		// boolean userExists = false;
		// for (String user : this.userIds) {
		// if (!userStrs.contains(user)) {
		// userExists = false;
		// } else {
		// userExists = true;
		// break;
		// }
		// }
		// // This means none of the users exist
		// if (!userExists)
		// return Boolean.TRUE;

		// TODO: if we have users no longer in the site...
		return this.userIds.isEmpty();
	}

	/**
	 * @base The base assessment dates.
	 * @return validity
	 */
	public boolean isEffectiveValid(AssessmentDates base) {

		// TODO: must also be valid against the base assessment dates as currently defined

		// open, if defined, must be before acceptUntil and due, if defined
		if ((getOpen() != null) && (getDue() != null) && (!getOpen().before(getDue())))
			return false;
		if ((getOpen() != null) && (getAcceptUntil() != null) && (!getOpen().before(getAcceptUntil())))
			return false;

		// due, if defined, must be not after acceptUntil, if defined
		if ((getDue() != null) && (getAcceptUntil() != null) && (getDue().after(getAcceptUntil())))
			return false;

		return true;
	}

	/**
	 * @param userId
	 *            The id of the user to check.
	 * @return if the access applies to this user.
	 */
	public boolean isForUser(String userId) {
		return this.userIds.contains(userId);
	}

	/**
	 * @param other
	 *            The other AssessmentAccess
	 * @return if this and the other are the same AssessmentAccess (by id).
	 */
	public boolean isSameId(AssessmentAccess other) {
		return this.getId().equals(other.getId());
	}

	/**
	 * Set the effective accept until date.
	 * 
	 * @param date
	 *            The accept until date, or null if there is none.
	 * @param base
	 *            The assessment's dates.
	 */
	public void setEffectiveAcceptUntil(Date date, AssessmentDates base) {
		boolean override = false;
		Date d = null;

		// compute what we should have based on the new setting and the assessment setting
		if (!Objects.equals(date, base.getAcceptUntil())) {
			override = true;
			d = date;
		}

		this.acceptUntilOverride = override;
		this.acceptUntil = d;
	}

	/**
	 * Set the effective due date.
	 * 
	 * @param date
	 *            The due date, or null if there is none.
	 * @param base
	 *            The assessment's dates.
	 */
	public void setEffectiveDueDate(Date date, AssessmentDates base) {
		boolean override = false;
		Date d = null;

		// compute what we should have based on the new setting and the assessment setting
		if (!Objects.equals(date, base.getDue())) {
			override = true;
			d = date;
		}

		this.dueOverride = override;
		this.due = d;
	}

	/**
	 * Set the effective hide until open flag.
	 * 
	 * @param hideUntilOpen
	 *            The hide until open flag.
	 * @param base
	 *            The base hide until open flag.
	 */
	public void setEffectiveHideUntilOpen(boolean hideUntilOpen, boolean base) {
		boolean override = false;
		boolean flag = false;

		if ((hideUntilOpen != base)) {
			override = true;
			flag = hideUntilOpen;
		}

		this.hideUntilOpenOverride = override;
		this.hideUntilOpen = flag;
	}

	/**
	 * Set the effective open date.
	 * 
	 * @param date
	 *            the effective open date.
	 * @param base
	 *            The assessment's dates.
	 */
	public void setEffectiveOpen(Date date, AssessmentDates base) {
		boolean override = false;
		Date d = null;

		// compute what we should have based on the new setting and the assessment setting
		if (!Objects.equals(date, base.getOpen())) {
			override = true;
			d = date;
		}

		this.openOverride = override;
		this.open = d;
	}

	/**
	 * Set the effective password.
	 * 
	 * @param password
	 *            The password (clear text).
	 * @param base
	 *            the assessment's password.
	 */
	public void setEffectivePassword(String password, AssessmentPassword base) {
		// massage the password
		if (password != null) {
			password = password.trim();
			if (password.length() > 255)
				password = password.substring(0, 255);
			if (password.length() == 0)
				password = null;
		}

		boolean override = false;
		String pw = null;

		if (!Objects.equals(password, base.getPassword())) {
			override = true;
			pw = password;
		}

		this.passwordOverride = override;
		this.password.setPassword(pw);
	}

	/**
	 * Set the effective time limit.
	 * 
	 * @param limit
	 *            The time limit.
	 * @param base
	 *            The assessment's time limit.
	 */
	public void setEffectiveTimeLimit(Long limit, Long base) {
		boolean override = false;
		Long tl = null;

		if (!Objects.equals(limit, base)) {
			override = true;
			tl = limit;
		}

		this.timeLimitOverride = override;
		this.timeLimit = tl;
	}

	/**
	 * Set the effective tries limit.
	 * 
	 * @param tries
	 *            The tries limit.
	 * @param base
	 *            The assessment's tries limit.
	 */
	public void setEffectiveTries(Integer count, Integer base) {
		boolean override = false;
		Integer t = null;

		if (!Objects.equals(count, base)) {
			override = true;
			t = count;
		}

		this.triesOverride = override;
		this.tries = t;
	}

	/**
	 * Set the users to have this access.
	 * 
	 * @param newIds
	 *            The new list of user ids.
	 */
	public void setUsers(List<String> newIds) {

		this.userIds.clear();
		if (newIds != null) {
			this.userIds.addAll(newIds);
		}

		// TODO: probably need to do this above: make sure that this access is the only one that pertains to these users
		// ((AssessmentSpecialAccessImpl) this.assessment.getSpecialAccess()).assureSingleAccessForUser(this);
	}

	/**
	 * Set as a copy of another (deep copy).
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(AssessmentAccess other) {

		/** The overridden accept until date. */
		this.acceptUntil = other.acceptUntil;
		this.acceptUntilOverride = other.acceptUntilOverride;

		/** The overridden due date. */
		this.due = other.due;
		this.dueOverride = other.dueOverride;

		/** Overridden hide until open flag. */
		this.hideUntilOpen = other.hideUntilOpen;
		this.hideUntilOpenOverride = other.hideUntilOpenOverride;

		this.id = other.id;

		/** Overridden open date. */
		this.open = other.open;
		this.openOverride = other.openOverride;

		/** Overridden time limit. */
		this.timeLimit = other.timeLimit;
		this.timeLimitOverride = other.timeLimitOverride;

		/** Overridden tries. */
		this.tries = other.tries;
		this.triesOverride = other.triesOverride;

		/** Overridden password. */
		this.password = new AssessmentPassword();
		this.password.set(other.password);
		this.passwordOverride = other.passwordOverride;

		/** User ids for this access. */
		this.userIds = new ArrayList<String>(other.userIds);
	}
}
