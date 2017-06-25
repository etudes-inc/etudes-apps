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
 * PointsOptions contains the assessment level points details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PointsOptions {

	/** The assessment points value (overrides points set in parts) - used for offline (which have no parts) */
	protected float points = 0f;

	/** If the assessment needs to have points defined in the parts. */
	protected boolean pointsReqired = true;

	/**
	 * Set from another.
	 * 
	 * @param other
	 *            The other.
	 * @return this (for chaining).
	 */
	public PointsOptions set(PointsOptions other) {
		this.points = other.getPoints();
		this.pointsReqired = other.isPointsReqired();

		return this;
	}
}
