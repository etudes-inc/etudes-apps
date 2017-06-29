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
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A Part provides a collection of questions for an assessment - it may contain many different sets of questions, each from a PartDetail.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Part {

	protected String id = null;
	protected Presentation presentation = new Presentation();
	protected String title = null;
	List<PartDetail> details = new ArrayList<PartDetail>();

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Part set(Part other) {
		this.id = other.id;
		this.presentation.set(other.presentation);
		this.title = other.title;

		this.details.clear();
		try {
			for (PartDetail detail : other.getDetails()) {
				PartDetail detailClone = (PartDetail) detail.clone();
				this.details.add(detailClone);
			}
		} catch (CloneNotSupportedException e) {
		}

		return this;
	}
}
