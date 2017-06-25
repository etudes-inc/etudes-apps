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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.etudes.mneme.api.Assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssessmentSpecialAccess holds details of special access for select users to an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentSpecialAccess {
	/** The special access definitions. */
	protected List<AssessmentAccess> specialAccess = new ArrayList<AssessmentAccess>();

	// TODO: addAccess cannot be made to locked assessments if set to a formal course evaluation

	/**
	 * Add a special access to the assessment.
	 * 
	 * @return The new special access.
	 */
	public AssessmentAccess addAccess() {
		AssessmentAccess rv = new AssessmentAccess();
		this.specialAccess.add(rv);

		return rv;
	}

	// TODO: assureUserAccess (adding a new access) cannot be made to locked assessments if set to a formal course evaluation

	/**
	 * Find the special access that applies to this user id, creating one if one is not found.
	 * 
	 * @param userId
	 *            The user id to look for.
	 * @return The special access for this userId, perhaps new.
	 */
	public AssessmentAccess assureUserAccess(String userId) {
		// find an access completely for this user
		Optional<AssessmentAccess> accessForUser = this.specialAccess.stream() //
				.filter(a -> ((a.getUserIds().size() == 1) && a.getUserIds().get(0).equals(userId))) //
				.findFirst();
		if (accessForUser.isPresent()) {
			return accessForUser.get();
		}

		// the user list of one
		List<String> userIds = new ArrayList<String>(1);
		userIds.add(userId);

		AssessmentAccess access = null;

		// see if there's one that pertains to this user, although it is not only for this user - add a copy if so
		Optional<AssessmentAccess> userAccess = getUserAccess(userId);
		if (userAccess.isPresent()) {
			// make a copy
			AssessmentAccess newAccess = new AssessmentAccess();
			newAccess.set(userAccess.get());
			newAccess.setId(null);

			this.specialAccess.add(newAccess);
			access = newAccess;
		}

		// else we need to create a new one
		else {
			access = addAccess();
		}

		// set the user
		access.setUsers(userIds);

		return access;
	}

	/**
	 * Make sure any undefined user ids or users without permissions are removed.
	 */
	public void assureValidUsers() {
		// TODO:!
		// // get all possible users who can submit - TODO: this could be a challenge
		// Set<String> userIds = this.securityService.getUsersIsAllowed(MnemeService.SUBMIT_PERMISSION, assessment.getContext());
		//
		// // filter out any userIds that are not currently defined
		// List<User> users = this.userDirectoryService.getUsers(userIds);
		// userIds.clear();
		// for (User user : users)
		// {
		// userIds.add(user.getId());
		// }
		//
		// // clear out any users not defined and permitted
		// List<AssessmentAccess> toRemove = new ArrayList<AssessmentAccess>();
		// for (AssessmentAccess access : this.specialAccess)
		// {
		// List<String> accessUsers = new ArrayList<String>(access.getUsers());
		// for (Iterator<String> i = accessUsers.iterator(); i.hasNext();)
		// {
		// String uid = i.next();
		//
		// if (!userIds.contains(uid))
		// {
		// i.remove();
		// }
		// }
		//
		// // if none left, remove it
		// if (accessUsers.isEmpty())
		// {
		// toRemove.add(access);
		// }
		//
		// // otherwise update the users
		// else
		// {
		// access.setUsers(accessUsers);
		// }
		// }
		//
		// // remove any that need removing
		// for (AssessmentAccess access : toRemove)
		// {
		// removeAccess(access);
		// }
	}

	// TODO: clear() is a change that cannot be made to locked assessments if set to a formal course evaluation

	/**
	 * Remove all special access.
	 */
	public void clear() {
		this.specialAccess.clear();
	}

	/**
	 * @param id
	 *            The special access id.
	 * @return the special access in this set with this id, if defined.
	 */
	public Optional<AssessmentAccess> getAccess(String id) {
		Optional<AssessmentAccess> rv = this.specialAccess.stream().filter(a -> a.getId().equals(id)).findFirst();
		return rv;
	}

	/**
	 * @return The special access defined for the assessment, sorted by user sort display.
	 */
	public List<AssessmentAccess> getOrderedAccess() {
		List<AssessmentAccess> rv = new ArrayList<AssessmentAccess>(this.specialAccess);

		// TODO: come back to this
		// // sort
		// Collections.sort(rv, new Comparator<AssessmentAccess>() {
		// public int compare(AssessmentAccess a0, AssessmentAccess a1) {
		// List<User> users0 = userDirectoryService.getUsers(a0.getUsers());
		// List<User> users1 = userDirectoryService.getUsers(a1.getUsers());
		//
		// // sort the multiple lists to find the first one to use
		// if (users0.size() > 1) {
		// Collections.sort(users0, new Comparator<User>() {
		// public int compare(User arg0, User arg1) {
		// int rv = arg0.getSortName().compareToIgnoreCase(arg1.getSortName());
		// return rv;
		// }
		// });
		// }
		//
		// if (users1.size() > 1) {
		// Collections.sort(users1, new Comparator<User>() {
		// public int compare(User arg0, User arg1) {
		// int rv = arg0.getSortName().compareToIgnoreCase(arg1.getSortName());
		// return rv;
		// }
		// });
		// }
		//
		// if ((users0.isEmpty()) && (users1.isEmpty())) {
		// return 0;
		// } else if (users0.isEmpty()) {
		// return -1;
		// } else if (users1.isEmpty()) {
		// return 1;
		// }
		// return users0.get(0).getSortName().compareToIgnoreCase(users1.get(0).getSortName());
		// }
		// });

		return rv;
	}

	/**
	 * @param userId
	 *            The user id.
	 * @return the special access for this user, if defined.
	 */
	public Optional<AssessmentAccess> getUserAccess(String userId) {
		Optional<AssessmentAccess> rv = this.specialAccess.stream().filter(a -> a.isForUser(userId)).findFirst();
		return rv;
	}

	/**
	 * @return if there is any special access defined.
	 */
	public boolean isDefined() {
		return !this.specialAccess.isEmpty();
	}

	/**
	 * @param base
	 *            The assessment's dates.
	 * @return validity.
	 */
	public boolean isValid(AssessmentDates base) {
		for (AssessmentAccess access : this.specialAccess) {
			if (access.isEmpty())
				continue;
			if (!access.isValid(base))
				return false;
		}

		return true;
	}

	// TODO: removeAccess() cannot be made to locked assessments if set to a formal course evaluation

	/**
	 * Remove one access.
	 * 
	 * @param access
	 *            the access to remove.
	 */
	public void removeAccess(AssessmentAccess remove) {
		this.specialAccess.remove(remove);
	}

	/**
	 * Make sure no other access is defined for the users in this one.
	 * 
	 * @param target
	 *            The access to keep.
	 */
	protected void assureSingleAccessForUser(AssessmentAccess target) {
		List<AssessmentAccess> toRemove = new ArrayList<AssessmentAccess>();

		for (AssessmentAccess access : this.specialAccess) {
			// skip the target
			if (access.equals(target))
				continue;

			// check each of the target's users
			for (String userId : target.getUserIds()) {
				// if this access was for that user
				if (access.isForUser(userId)) {
					// remove the user from the access
					List<String> userIds = access.getUserIds();
					userIds.remove(userId);

					// if the result is an access empty of users, remove it
					if (access.getUserIds().isEmpty()) {
						toRemove.add(access);
					}
				}
			}
		}

		// remove those we cleared out all users from
		for (AssessmentAccess access : toRemove) {
			removeAccess(access);
		}
	}

	// /**
	// * Clear out the deleted definitions.
	// */
	// protected void clearDeleted() {
	// this.deleted.clear();
	// }

	/**
	 * Before saving the assessment, remove any saved (i.e. with id) definitions with no settings or no users.
	 * 
	 * @param base
	 *            The assessment dates.
	 */
	protected void consolidate(AssessmentDates base) {
		// if any stored definitions override nothing, or have no users, remove them
		for (Iterator<AssessmentAccess> i = this.specialAccess.iterator(); i.hasNext();) {
			AssessmentAccess access = i.next();
			if (access.getId() == null)
				continue;

			boolean remove = access.getUserIds().isEmpty();

			if (!remove) {
				// TODO: check this logic...
				remove = (!access.isAcceptUntilOverride()) && (!access.isDueOverride()) && (!access.isOpenOverride()) && (!access.isHideUntilOpenOverride())
						&& (access.isHideUntilOpen() == base.isHideUntilOpen()) && (!access.isPasswordOverride()) && (!access.isTimeLimitOverride())
						&& (!access.isTriesOverride());
			}

			if (remove) {
				i.remove();
				// this.deleted.add(access);
			}
		}
	}

	// /**
	// * Access the deleted definitions.
	// *
	// * @return The List of deleted definitions.
	// */
	// protected List<AssessmentAccess> getDeleted() {
	// return this.deleted;
	// }
	//
	/**
	 * Set as a copy of another (deep copy).
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(Assessment assessment, AssessmentSpecialAccess other) {
		this.specialAccess.clear();
		for (AssessmentAccess access : other.specialAccess) {
			AssessmentAccess copy = new AssessmentAccess();
			copy.set(access);
			this.specialAccess.add(copy);
		}
	}
}
