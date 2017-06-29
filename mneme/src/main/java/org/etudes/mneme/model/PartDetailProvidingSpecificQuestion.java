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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PartDetailProvidingSpecificQuestion is a part detail that adds a single, specific question to the part.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PartDetailProvidingSpecificQuestion extends PartDetail {

	protected long questionId = 0l;

	/**
	 * {@inheritDoc}
	 */
	public int getNumQuestions() {
		return 1;
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public PartDetailProvidingSpecificQuestion set(PartDetailProvidingSpecificQuestion other) {
		super.set(other);
		
		this.questionId = other.getQuestionId();

		return this;
	}

	@Override
	public float sumQuestionPoints() {
		// TODO: we need to access the points defined for the single question
		final float questionPoints = 0;

		return questionPoints;
	}
}
