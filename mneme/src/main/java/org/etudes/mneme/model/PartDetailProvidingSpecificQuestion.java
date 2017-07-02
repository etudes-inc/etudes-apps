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

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * PartDetailProvidingSpecificQuestion is a part detail that adds a single, specific question to the part.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PartDetailProvidingSpecificQuestion extends PartDetail {

	protected long questionId = 0l;

	public PartDetailProvidingSpecificQuestion() {
		super();
	}

	public PartDetailProvidingSpecificQuestion(long id, Optional<Float> overrideSumQuestionPoints, long questionId) {
		super(id, overrideSumQuestionPoints);
		this.questionId = questionId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumQuestions() {
		return 1;
	}

	@Override
	public PartDetail set(PartDetail other) {
		super.set(other);

		if (!(other instanceof PartDetailProvidingSpecificQuestion)) {
			throw new IllegalArgumentException("PartDetailProvidingSpecificQuestion.set expects another PartDetailProvidingSpecificQuestion");
		}
		PartDetailProvidingSpecificQuestion o = (PartDetailProvidingSpecificQuestion) other;

		this.questionId = o.getQuestionId();

		return this;
	}

	@Override
	public float sumQuestionPoints() {
		// TODO: we need to access the points defined for the single question
		final float questionPoints = 0;

		return questionPoints;
	}
}
