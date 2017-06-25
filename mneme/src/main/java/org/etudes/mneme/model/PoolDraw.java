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

import org.etudes.mneme.api.Pool;
import org.etudes.mneme.api.Shuffler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PoolDraw contain the details of a part's draw from a pool.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PoolDraw extends PartDetail {
	/** The number of questions to draw from the pool. */
	protected int numQuestions = 0;

	/** The original pool id. */
	protected String origPoolId = null;

	/** The actual pool id. */
	protected String poolId = null;

	/**
	 * Draw questions based on this random seed.
	 * 
	 * @param shuffler
	 *            The shuffler.
	 * @return A List of question ids drawn from the pool.
	 */
	public List<String> drawQuestionIds(Shuffler shuffler) {
		// TODO:
		// Pool pool = getPool();
		// if (pool == null)
		// return new ArrayList<String>();
		//
		// Assessment assessment = this.assessment;
		// if ((assessment == null) && (this.part != null))
		// assessment = this.part.getAssessment();
		//
		// // for a uniform pool, draw from any survey or not; otherwise match the draw to the assessment type
		// Pool.PoolCounts counts = pool.getDetailedNumQuestions();
		// Boolean survey = null;
		// if ((counts.validAssessment != 0) && (counts.validSurvey != 0) && (assessment != null)) {
		// survey = Boolean.valueOf(assessment.getType() == AssessmentType.survey);
		// }
		//
		// // we need to overdraw by the number of manual questions this assessment uses from the pool
		// List<String> manualQuestionIds = (assessment != null) ? ((AssessmentPartsImpl) assessment.getParts()).getQuestionPicksFromPool(pool, survey)
		// : new ArrayList<String>();
		//
		// int size = this.numQuestions + manualQuestionIds.size();
		//
		// List<String> rv = pool.drawQuestionIds(shuffler, size, survey);
		//
		// // we need to remove from rv any manual questions used in the assessment
		// rv.removeAll(manualQuestionIds);
		//
		// // we need just our count
		// if (rv.size() > this.numQuestions) {
		// rv = rv.subList(0, this.numQuestions);
		// }
		//
		// return rv;
		return new ArrayList<>();
	}

	/**
	 * Access all questions.
	 * 
	 * @return A List of question ids from the pool.
	 */
	public List<String> getAllQuestionIds() {
		// TODO:
		// Pool pool = getPool();
		// if (pool == null)
		// return new ArrayList<String>();
		//
		// return pool.getAllQuestionIds(null, Boolean.TRUE);
		return new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSupportingPoints() {
		// TODO:
		// Pool pool = getPool();
		// if (pool == null)
		// return Boolean.FALSE;
		//
		// // if the assessment is non-survey, and the draw pool has non-survey questions, we have points
		// Assessment assessment = this.assessment;
		// if ((assessment == null) && (this.part != null))
		// assessment = this.part.getAssessment();
		// if (assessment == null)
		// return Boolean.FALSE;
		// if (assessment.getType() == AssessmentType.survey)
		// return Boolean.FALSE;
		//
		// Pool.PoolCounts counts = pool.getDetailedNumQuestions();
		// if (counts.validAssessment != 0)
		// return Boolean.TRUE;
		//
		// return Boolean.FALSE;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	// public String getDescription()
	// {
	// // TODO: return ((PartImpl) this.part).messages.getFormattedMessage("random-draw-description", null);
	// }

	/**
	 * {@inheritDoc}
	 */
	public String getInvalidMessage() {
		// TODO:
		// // we need a valid pool and a positive count that is within the pool's question limit
		// if (this.poolId == null) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-pool", null);
		// if (this.numQuestions == null) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-count", null);
		// if (this.numQuestions.intValue() <= 0) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-count", null);
		//
		// Pool p = getPool();
		// if (p == null) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-pool", null);
		//
		// // each draw must have enough questions in the pool to draw
		// if (getPoolNumAvailableQuestions() < getNumQuestions())
		// {
		// Object[] args = new Object[1];
		// args[0] = p.getTitle();
		// return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-overdraw", args);
		// }
		//
		// // the pool must be completely valid
		// Pool.PoolCounts counts = p.getDetailedNumQuestions();
		// if (counts.invalidAssessment + counts.invalidSurvey > 0)
		// {
		// Object[] args = new Object[1];
		// args[0] = p.getTitle();
		// return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-invalid-pool", args);
		// }
		//
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getNonOverridePoints() {
		if (this.numQuestions == 0)
			return Float.valueOf(0);
		if (!isSupportingPoints())
			return Float.valueOf(0);

		// TODO:
		// // pool's point value * num questions
		// Pool pool = this.poolService.getPool(this.poolId);
		// if (pool == null)
		// return Float.valueOf(0);
		//
		// float poolPoints = pool.getPoints().floatValue();
		// return Float.valueOf(poolPoints * this.numQuestions.intValue());
		return Float.valueOf(0);
	}

	/**
	 * Get the number of questions available in the pool.<br />
	 * The current pool size is reduced by the number of questions manually drawn from the pool in this assessment.
	 * 
	 * @return The number of questions available in the pool.
	 */
	public Integer getPoolNumAvailableQuestions() {
		// TODO:
		// Pool pool = getPool();
		// if (pool != null) {
		// Assessment assessment = this.assessment;
		// if ((assessment == null) && (this.part != null))
		// assessment = this.part.getAssessment();
		//
		// // for a uniform pool, draw from any survey or not; otherwise match the draw to the assessment type
		// Pool.PoolCounts counts = pool.getDetailedNumQuestions();
		// Boolean survey = null;
		// if ((counts.validAssessment != 0) && (counts.validSurvey != 0) && (assessment != null)) {
		// survey = Boolean.valueOf(assessment.getType() == AssessmentType.survey);
		// }
		//
		// int size = 0;
		//
		// // if uniform, count them all
		// if (survey == null) {
		// size = counts.validAssessment + counts.validSurvey;
		// }
		//
		// // if not uniform, use the count that matches the assessment
		// else {
		// if (survey) {
		// size = counts.validSurvey;
		// } else {
		// size = counts.validAssessment;
		// }
		// }
		//
		// // reduce by the number of picks from this pool in the assessment
		// if (assessment != null) {
		// size -= ((AssessmentPartsImpl) assessment.getParts()).getQuestionPicksFromPool(pool, survey).size();
		// }
		//
		// return Integer.valueOf(size);
		// }

		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getQuestionPoints() {
		if (this.numQuestions <= 0)
			return Float.valueOf(0);
		if (!isSupportingPoints())
			return Float.valueOf(0);

		float rv = getTotalPoints() / this.numQuestions;

		// round away bogus decimals
		rv = Math.round(rv * 100.0f) / 100.0f;

		return Float.valueOf(rv);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle() {
		// TODO: return ((PartImpl) this.part).messages.getFormattedMessage("random-draw-title", null);
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		// TODO: return ((PartImpl) this.part).messages.getFormattedMessage("random-draw-type", null);
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPhantom() {
		return false;
	}

	/**
	 * @return if this draws from the same pool as the other.
	 */
	public boolean isSamePool(PoolDraw other) {
		return this.poolId.equals(other.poolId);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSpecific() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		// we need a valid pool and a positive count that is within the pool's question limit
		if (this.poolId == null)
			return false;
		if (this.numQuestions <= 0)
			return false;

		// TODO:
		// Pool p = getPool();
		// if (p == null)
		// return Boolean.FALSE;
		//
		// // each draw must have enough questions in the pool to draw
		// if (getPoolNumAvailableQuestions() < getNumQuestions()) {
		// return Boolean.FALSE;
		// }
		//
		// // if the assessment is locked, the pool must be completely valid
		// if (this.getPart().getAssessment().getIsLocked()) {
		// Pool.PoolCounts counts = p.getDetailedNumQuestions();
		// if (counts.invalidAssessment + counts.invalidSurvey > 0)
		// return Boolean.FALSE;
		// }

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean restoreToOriginal(Map<String, String> poolIdMap, Map<String, String> questionIdMap) {
		// TODO:
		// // if the map is present, translate to another pool id
		// if (poolIdMap != null) {
		// String translated = poolIdMap.get(this.origPoolId);
		// if (translated != null) {
		// this.origPoolId = translated;
		// }
		// }
		//
		// // if there has been no change, we are done.
		// if (this.poolId.equals(this.origPoolId))
		// return true;
		//
		// // check that the original pool is available
		// Pool pool = this.poolService.getPool(this.origPoolId);
		// if ((pool == null) || (pool.getIsHistorical()))
		// return false;
		//
		// // set it
		// this.poolId = this.origPoolId;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPool(Pool pool) {
		this.poolId = pool.getId();

		// set the original only once
		if (this.origPoolId == null) {
			this.origPoolId = pool.getId();
		}
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public PoolDraw set(PoolDraw other) {
		super.set(other);
		this.numQuestions = other.numQuestions;
		this.origPoolId = other.origPoolId;
		this.poolId = other.poolId;

		return this;
	}
}
