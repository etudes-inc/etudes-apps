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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AssessmentPassword models passwords on assessments.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentPassword {
	/** The password - clear text. */
	protected String password = null;

	/**
	 * @param password
	 *            The clear text password as entered.
	 * @return if the provided password (clear text) matches the defined password for the assessment.
	 */
	public boolean checkPassword(String password) {
		if (password == null)
			return false;

		return password.equals(this.password);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPassword(String password) {
		// massage the password
		if (password != null) {
			password = password.trim();
			if (password.length() > 255)
				password = password.substring(0, 255);
			if (password.length() == 0)
				password = null;
		}

		this.password = password;
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(AssessmentPassword other) {
		this.password = other.password;
	}
}
