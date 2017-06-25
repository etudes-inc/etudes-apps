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

import java.util.Map;

import org.etudes.mneme.api.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * QuestionPick contain the details of a part's inclusion of a question from a manual selection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionPick extends PartDetail {
	/** The original question id. */
	protected String origQuestionId = null;

	/** The actual question id. */
	protected String questionId = null;

	/** A related detail. */
	protected transient PartDetail relatedDetail = null;

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		// TODO:
		// // use the question description
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return "?";
		//
		// // strip html
		// String value = FormattedText.convertFormattedTextToPlaintext(q.getDescription());
		// if (value == null) return "?";
		//
		// value = value.replace("\n", " ");
		// return value;

		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInvalidMessage() {
		// TODO:
		// // we need a question
		// if (this.questionId == null) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-question", null);
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-missing-question", null);
		//
		// // needs to be valid
		// if (!q.getIsValid().booleanValue()) return ((PartImpl) this.part).messages.getFormattedMessage("invalid-detail-invalid-question", null);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getNonOverridePoints() {
		// TODO:
		// // get the question's pool's point value
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return Float.valueOf(0);
		// if (!q.getHasPoints().booleanValue()) return Float.valueOf(0);
		//
		// Pool p = q.getPool();
		// if (p == null) return Float.valueOf(0);
		//
		// return p.getPoints();
		return Float.valueOf(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumQuestions() {
		return 1;
	}

	/**
	 * Access the original question id.
	 * 
	 * @return the original question id.
	 */
	public String getOrigQuestionId() {
		return this.origQuestionId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPoolId() {
		// TODO:
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return null;
		// return q.getPool().getId();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Float getQuestionPoints() {
		if (!isSupportingPoints())
			return Float.valueOf(0);

		// a single draw, just use the total
		return getTotalPoints();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle() {
		// TODO:
		// // use the question type
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return "?";
		// return q.getTitle();

		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		// TODO:
		// // use the question type
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return "?";
		// return q.getTypeName();

		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPhantom() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSpecific() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSupportingPoints() {
		// TODO:
		// if (this.questionId == null) return Boolean.FALSE;
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return Boolean.FALSE;
		//
		// // if the question supports points, so does this
		// return q.getHasPoints();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUsingSameQuestion(QuestionPick other) {
		return this.questionId.equals(other.questionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		// TODO:
		// // we need a question
		// if (this.questionId == null) return Boolean.FALSE;
		// Question q = this.questionService.getQuestion(this.questionId);
		// if (q == null) return Boolean.FALSE;
		//
		// // needs to be valid
		// if (!q.getIsValid().booleanValue()) return Boolean.FALSE;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean restoreToOriginal(Map<String, String> poolIdMap, Map<String, String> questionIdMap) {
		// TODO:
		// // if the map is present, translate to another question id
		// if (questionIdMap != null)
		// {
		// String translated = questionIdMap.get(this.origQuestionId);
		// if (translated != null)
		// {
		// this.origQuestionId = translated;
		// }
		// }
		//
		// // if no change
		// if (this.questionId.equals(this.origQuestionId)) return true;
		//
		// // the question must exist, and be non-historical, and its pool must exist and be non-historical
		// Question q = this.questionService.getQuestion(this.origQuestionId);
		// if ((q == null) || (q.getIsHistorical()) || q.getPool().getIsHistorical()) return false;
		//
		// // restore
		// this.questionId = this.origQuestionId;
		// setChanged();

		return true;
	}

	/**
	 * Set the question id (original and actual).
	 * 
	 * @param questionId
	 *            The question id.
	 */
	public void setQuestion(Question question) {
		this.questionId = question.getId();

		// set the original only once
		if (this.origQuestionId == null) {
			this.origQuestionId = question.getId();
		}
	}

	/**
	 * @return The related detail.
	 */
	protected PartDetail getRelatedDetail() {
		if (this.relatedDetail == null)
			return this;

		return this.relatedDetail;
	}

	/**
	 * Set as a copy of another.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public QuestionPick set(QuestionPick other) {
		super.set(other);
		this.origQuestionId = other.origQuestionId;
		this.questionId = other.questionId;

		return this;
	}
}
