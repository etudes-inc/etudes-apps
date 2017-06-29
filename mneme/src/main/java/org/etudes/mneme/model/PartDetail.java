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
import lombok.experimental.Accessors;

/**
 * A PartDetail specifies which questions are included in an assessment part.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class PartDetail implements Cloneable {

	protected long id = 0l;

	/** Points override total value for all the questions in the part detail. If not set, the individual question points are used. */
	protected Float overrideSumQuestionPoints = null;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return The total points for the part detail, or 0 if there are no points.
	 */
	public float getTotalPoints() {
		if (overrideSumQuestionPoints != null) {
			return overrideSumQuestionPoints;
		}

		return sumQuestionPoints();
	}

	/**
	 * Set the points. If it matches the points computed from the individual questions, then make sure the override points is clear. Otherwise, set it.
	 * 
	 * @param points
	 *            The point total value for all the questions in the part.
	 */
	public void setEffectivePoints(float points) {
		if (points == sumQuestionPoints()) {
			setOverrideSumQuestionPoints(null);
		} else {
			setOverrideSumQuestionPoints(points);
		}
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(PartDetail other) {
		this.id = other.getId();
		this.overrideSumQuestionPoints = other.getOverrideSumQuestionPoints();
	}

	/**
	 * @return The number of questions this part detail adds to the part.
	 */
	abstract public int getNumQuestions();

	/**
	 * @return The total point value sum of each individual question in the part detail.
	 */
	abstract public float sumQuestionPoints();
}
