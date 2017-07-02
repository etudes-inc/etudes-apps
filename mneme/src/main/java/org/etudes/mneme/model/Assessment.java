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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
	@NonNull
	protected String context = "";

	@NonNull
	protected Attribution created = new Attribution();

	protected long id = 0l;

	@NonNull
	protected Attribution modified = new Attribution();

	@NonNull
	protected ArrayList<Part> parts = new ArrayList<>();

	@NonNull
	protected Presentation presentation = new Presentation();

	@NonNull
	protected Schedule schedule = new Schedule();

	@NonNull
	protected AssessmentStatus status = new AssessmentStatus();

	protected long subscription = 0l;

	@NonNull
	protected String title = "";

	@NonNull
	protected Type type = Type.test;

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Assessment set(Assessment other) {
		this.context = other.getContext();
		this.created.set(other.getCreated());
		this.id = other.getId();
		this.modified.set(other.getModified());
		this.parts.clear();
		for (Part p : other.getParts()) {
			this.parts.add(new Part().set(p));
		}
		this.presentation.set(other.getPresentation());
		this.schedule.set(other.getSchedule());
		this.status.set(other.getStatus());
		this.subscription = other.getSubscription();
		this.title = other.getTitle();
		this.type = other.getType();

		return this;
	}
}
