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
 * PartDetailProvidingPoolDraw is a part detail that adds to the part one or more randomly drawn questions from a question pool.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PartDetailProvidingPoolDraw extends PartDetail {

	protected int numQuestionsToDraw = 0;
	protected long poolId = 0l;

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public PartDetailProvidingPoolDraw set(PartDetailProvidingPoolDraw other) {
		super.set(other);

		this.numQuestionsToDraw = other.getNumQuestionsToDraw();
		this.poolId = other.getPoolId();

		return this;
	}

	@Override
	public float sumQuestionPoints() {

		// TODO: we need to get the pool's question point value
		final float poolQuestionPointValue = 0;

		return poolQuestionPointValue * this.numQuestionsToDraw;
	}

	@Override
	public int getNumQuestions() {
		return this.numQuestionsToDraw;
	}
}
