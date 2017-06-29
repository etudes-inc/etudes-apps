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

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Models a Mneme assessment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Assessment {

	/**
	 * Type enumerates the different assessment types.
	 */
	public enum Type {
		assignment("A"), offline("O"), survey("S"), test("T");

		public static Type fromCode(String code) {
			for (Type t : Type.values()) {
				if (t.getCode().equalsIgnoreCase(code)) {
					return t;
				}
			}

			return test;
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

	/** The assessment context - where it belongs in the LMS. */
	@NotNull
	protected String context = "";

	@NotNull
	protected Attribution created = new Attribution();

	protected long id = 0l;

	@NotNull
	protected Attribution modified = new Attribution();

	@NotNull
	protected ArrayList<Part> parts = new ArrayList<>();

	@NotNull
	protected Presentation presentation = new Presentation();

	@NotNull
	protected Schedule schedule = new Schedule();

	@NotNull
	protected AssessmentStatus status = new AssessmentStatus();

	protected long subscription = 0l;

	@NotNull
	protected String title = "";

	@NotNull
	protected Type type = Type.test;
}
