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

package org.etudes.apps.dispatcher;

/**
 * Unified model of the user's role in the context, from the particular role values provider by various LTI consumers
 */

public enum LTIRole {

	admin, //
	instructor, //
	none, //
	observer, //
	student, //
	ta;

	/**
	 * Set from a roles string from LTI launch from Canvas. The string may contain many roles.
	 * 
	 * @param role
	 * @return the role.
	 */
	public static LTIRole fromLaunch(String role) {
		if (role == null)
			return none;

		// most powerful first, so if the user has many roles, the most powerful is selected for our use
		if (role.indexOf("Administrator") != -1)
			return admin;
		if (role.indexOf("Instructor") != -1)
			return instructor;
		if (role.indexOf("Student") != -1)
			return student;
		if (role.indexOf("Learner") != -1)
			return student;
		if (role.indexOf("Observer") != -1)
			return observer;

		return none;
	}
}
