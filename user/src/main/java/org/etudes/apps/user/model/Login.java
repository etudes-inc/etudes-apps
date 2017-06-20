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

package org.etudes.apps.user.model;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonIgnoreProperties(ignoreUnknown = true)
public class Login {
	final static private Logger logger = LoggerFactory.getLogger(Login.class);

	/** The internal user id */
	@JsonProperty("id")
	protected Long _id;

	/** The user's email address / login id. */
	protected String email;

	/** A local login encoded password. Not many users will have local login. */
	protected String password;

	/** The user id holding this login. */
	protected Long userId;

	/**
	 * Set the password from this new plain text value to a new encoded value. Password must be 'strong'.
	 * 
	 * @param password
	 *            The new plain text password.
	 * @return true if successful, false if not.
	 */
	public boolean changePassword(String password) {
		if (password == null)
			return false;

		// reject weak passwords
		if (!strongPassword(password))
			return false;

		this.password = encodePassword(password);
		return true;
	}

	/**
	 * Check if the encoding of the plain text password given matches the registered encoded password for the user.
	 * 
	 * @param password
	 *            the plain text password.
	 * @return true if matches, false if not.
	 */
	public boolean checkPassword(String password) {
		if ((password == null) || (this.password == null))
			return false;

		String encoded = encodePassword(password);

		// logger.info(password + " : " + encoded);
		// return true;

		boolean matches = encoded.equals(this.password);
		return matches;
	}

	/**
	 * Encode the clear text password.
	 * 
	 * @param password
	 *            The clear text password.
	 * @return The encoded password, or null if there was a problem.
	 */
	protected String encodePassword(String password) {
		try {
			// static and dynamic salt the password
			String salted = "ETUDES@" + password + "@" + userId.toString();
			byte[] bytes = salted.getBytes("UTF-8");

			// digest with MD5, repeated a few times
			MessageDigest md = MessageDigest.getInstance("MD5");

			for (int i = 0; i < 1001; i++) {
				md.update(bytes);
				bytes = md.digest();
			}

			String encoded = encodeBase64String(bytes);
			return encoded;
		} catch (NoSuchAlgorithmException e) {
			logger.warn("encodePassword: " + e.toString());
		} catch (UnsupportedEncodingException e) {
			logger.warn("encodePassword: " + e.toString());
		}

		return null;
	}

	/**
	 * Check strong password rules: must be 8 characters or longer, include uppercase, lowercase, and numeric.
	 * 
	 * @param pw
	 *            The clear text password to check.
	 * @return true if strong, false if not.
	 */
	protected boolean strongPassword(String pw) {
		String trim = pw.trim();
		if (trim.length() < 8)
			return Boolean.FALSE;
		if (!Pattern.matches(".*?[A-Z].*?", trim))
			return Boolean.FALSE;
		if (!Pattern.matches(".*?[a-z].*?", trim))
			return Boolean.FALSE;
		if (!Pattern.matches(".*?[0-9].*?", trim))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}
}
