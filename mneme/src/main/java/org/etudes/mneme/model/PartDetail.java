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
import java.util.Map;
import java.util.Objects;

import org.etudes.mneme.api.Ordering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PartDetail models the PoolDraw or QuestionPick entries that specify the question makeup of a Part.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PartDetail {
	public class MyOrdering implements Ordering<PartDetail> {
		protected PartDetail detail = null;

		public MyOrdering(PartDetail detail) {
			this.detail = detail;
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean getIsFirst() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return Boolean.TRUE;
			// if (this.detail.part == null)
			// return true;
			//
			// if (this.detail.equals((this.detail.part.getDetails().get(0))))
			// return true;

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean getIsLast() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return Boolean.TRUE;
			// if (this.detail.part == null)
			// return true;
			//
			// if (this.detail.equals(this.detail.part.getDetails().get(this.detail.part.getDetails().size() - 1)))
			// return true;

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public PartDetail getNext() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return null;
			// if (this.detail.part == null)
			// return null;
			//
			// int index = this.detail.part.getDetails().indexOf(detail);
			// if (index == this.detail.part.getDetails().size() - 1)
			// return null;
			//
			// return this.detail.part.getDetails().get(index + 1);
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Integer getPosition() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return null;
			// if (this.detail.part == null)
			// return null;
			//
			// int index = this.detail.part.getDetails().indexOf(detail);
			//
			// return index + 1;
			return null;
		}

		/**
		 * @return The detail's id; this is a match to setPositioning, which will re-order based on ids.
		 */
		public String getPositioning() {
			if (this.detail.isPhantom())
				return null;
			return this.detail.getId();
		}

		/**
		 * @return a set of Position objects for the details in the part - with the detail position and id.
		 */
		public List<Position> getPositions() {
			List<Position> rv = new ArrayList<Position>();

			// TODO:
			// if (detail.part == null)
			// return rv;
			//
			// for (int i = 0; i < detail.part.getDetails().size(); i++) {
			// Position p = new Position(Integer.valueOf(i + 1).toString(), detail.part.getDetails().get(i).getId());
			// rv.add(p);
			// }

			return rv;
		}

		/**
		 * {@inheritDoc}
		 */
		public PartDetail getPrevious() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return null;
			// if (this.detail.part == null)
			// return null;
			//
			// int index = this.detail.part.getDetails().indexOf(detail);
			// if (index == 0)
			// return null;
			//
			// return this.detail.part.getDetails().get(index - 1);
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Integer getSize() {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return null;
			// if (this.detail.part == null)
			// return null;
			//
			// return Integer.valueOf(this.detail.part.getDetails().size());
			return null;
		}

		/**
		 * Change the detail's position within the part
		 * 
		 * @param id
		 *            The id in the position this one wants to move to.
		 */
		public void setPositioning(String id) {
			// TODO:
			// if (this.detail.getIsPhantom())
			// return;
			// if (id == null)
			// return;
			//
			// if (id.equals(this.detail.getId()))
			// return;
			//
			// int curPos = getPosition().intValue();
			//
			// // find the position of the id'ed element
			// PartDetail targetDetail = this.detail.part.getAssessment().getParts().getDetailId(id);
			// if (targetDetail == null)
			// return;
			//
			// int newPos = targetDetail.getOrdering().getPosition().intValue();
			// if (curPos == newPos)
			// return;
			//
			// // remove
			// this.detail.part.getDetails().remove(this.detail);
			//
			// // re-insert
			// this.detail.part.getDetails().add(newPos - 1, this.detail);
			//
			// // mark as changed
			// ((PartImpl) this.detail.part).setChanged();
		}
	}

	public class Position {
		String id;
		String position;

		public Position(String position, String id) {
			this.position = position;
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

		public String getPosition() {
			return this.position;
		}
	}

	/** Part detail id. */
	protected String id = null;

	/** My ordering. */
	protected MyOrdering ordering = new MyOrdering(this);

	/** Points override value for the questions in the part. */
	protected Float points = null;

	/** The sequence in the part at the time this was read (-1 if not set). Note: the actual sequence is the detail's position in the part's details list. */
	protected int seq = -1;

	/**
	 * @return the effective points value - if there is an override, use that, else use the total points computed from the question pool values.
	 */
	public Float getEffectivePoints() {
		return this.getTotalPoints();
	}

	/**
	 * @return The total points for the part detail, or 0 if there are no points.
	 */
	public Float getTotalPoints() {
		if (!isSupportingPoints())
			return Float.valueOf(0);

		// if set in the part, use this
		Float overridePoints = getPoints();
		if (overridePoints != null) {
			return overridePoints;
		}

		return getNonOverridePoints();
	}

	/**
	 * Set the points. If it matches the non-override value, clear the override, else set this as the override value.
	 * 
	 * @param points
	 *            The point value for all the questions in the part.
	 */
	public void setEffectivePoints(Float points) {
		// change to a reasonable range
		if ((points != null) && (points.floatValue() < 0f))
			points = Float.valueOf(0.0f);
		if ((points != null) && (points.floatValue() > 10000f))
			points = Float.valueOf(10000.0f);

		// if the points value matches the non-override points, clear the override
		if (Objects.equals(points, this.getNonOverridePoints())) {
			setPoints(null);
		}

		// otherwise, set this as the override
		else {
			setPoints(points);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPoints(Float points) {
		// massage points - 2 decimal places
		if (points != null) {
			points = Float.valueOf(((float) Math.round(points.floatValue() * 100.0f)) / 100.0f);
		}

		this.points = points;
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 */
	protected void set(PartDetail other) {
		this.id = other.id;
		this.points = other.points;
		this.seq = other.seq;
	}

	/**
	 * @return The total point value ignoring any override set.
	 */
	abstract Float getNonOverridePoints();

	/**
	 * Get the number of questions this detail provides.
	 * 
	 * @return The number of questions this detail adds to the part.
	 */
	abstract int getNumQuestions();

	/**
	 * Access the pool id associated with this detail (the question's pool or the pool to draw from).
	 * 
	 * @return The pool id.
	 */
	abstract String getPoolId();

	/**
	 * @return The point value for a single question in the part, or null if not set.
	 */
	abstract Float getQuestionPoints();

	/**
	 * Access the detail's type; either a question type if this is a specific question, or "draw" for random draw.
	 * 
	 * @return The detail type.
	 */
	abstract String getType();

	/**
	 * @return if the detail is a phantom (not real).
	 */
	abstract boolean isPhantom();

	/**
	 * @return if the detail is specific to a question (like a pick), not general to a set of questions (like random draw).
	 */
	abstract boolean isSpecific();

	// TODO: if setPoints is called with new points, assessment needs a submission re-scoring

	/**
	 * @return if the detail supports points. Survey questions, or draws of Surveys questions, for example, do not support points.
	 */
	abstract boolean isSupportingPoints();

	/**
	 * @return validity.
	 */
	abstract boolean isValid();

	/**
	 * Restore the detail's pool and or question id to the original, translated by the maps if present.
	 * 
	 * @param poolIdMap
	 *            A map of old-to-new pool ids.
	 * @param questionIdMap
	 *            A map of old-to-new question ids.
	 * @return true if successful, false if not.
	 */
	abstract boolean restoreToOriginal(Map<String, String> poolIdMap, Map<String, String> questionIdMap);
}
