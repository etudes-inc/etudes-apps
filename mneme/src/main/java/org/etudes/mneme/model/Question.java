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
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Question models assessment questions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Question {

	/**
	 * Type enumerates the different assessment types.
	 */
	public enum Type {
		multipleChoice("M"), trueFalse("T");

		public static Type fromCode(String code) {
			for (Type t : Type.values()) {
				if (t.getCode().equalsIgnoreCase(code)) {
					return t;
				}
			}

			return trueFalse;
		}

		/** Defines the codes to use to reconstruct the types (for example, from a db). */
		private final String code;

		private Type(String code) {
			this.code = code;
		}

		public String getCode() {
			return this.code;
		}
	}

	protected long id = 0l;

	@NonNull
	protected Presentation presentation = new Presentation();

	@NonNull
	protected Type type = Type.trueFalse;

	/**
	 * 
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Question set(Question other) {
		this.id = other.getId();
		this.presentation.set(other.getPresentation());
		this.type = other.getType();

		return this;
	}

}
