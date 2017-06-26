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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.etudes.mneme.Ordering;
import org.etudes.mneme.Shuffler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Part holds a set of questions within an assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Part {
	public class MyOrdering implements Ordering<Part> {
		protected Part part = null;

		protected List<Part> parts = null;

		public MyOrdering(Part part) {
			this.part = part;
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean getIsFirst() {
			if (parts == null)
				return true;

			if (part.equals(parts.get(0)))
				return true;

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public Boolean getIsLast() {
			if (parts == null)
				return true;

			if (part.equals(parts.get(parts.size() - 1)))
				return true;

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public Part getNext() {
			if (parts == null)
				return null;

			int index = parts.indexOf(part);
			if (index == parts.size() - 1)
				return null;

			return parts.get(index + 1);
		}

		/**
		 * {@inheritDoc}
		 */
		public Integer getPosition() {
			if (parts == null)
				return new Integer(1);

			int index = parts.indexOf(part);

			return index + 1;
		}

		/**
		 * {@inheritDoc}
		 */
		public Part getPrevious() {
			if (parts == null)
				return null;

			int index = parts.indexOf(part);
			if (index == 0)
				return null;

			return parts.get(index - 1);
		}

		/**
		 * {@inheritDoc}
		 */
		public Integer getSize() {
			if (parts == null)
				return new Integer(1);

			return Integer.valueOf(parts.size());
		}

		/**
		 * Initialize the parts list that contains this part.
		 * 
		 * @param parts
		 *            The parts list that contains this part.
		 */
		protected void initParts(List<Part> parts) {
			this.parts = parts;
		}
	}

	protected class ShufflerImpl implements Shuffler {
		// protected boolean old = false;

		protected String seedRoot = null;

		public ShufflerImpl(Part part) {
			// if (part.assessment.getSubmissionContext() != null) {
			// String crossoverIdStr = ((SubmissionServiceImpl) part.submissionService).get106ShuffleCrossoverId();
			// if (crossoverIdStr != null) {
			// Long crossoverId = Long.valueOf(crossoverIdStr);
			// Long subId = Long.valueOf(part.assessment.getSubmissionContext().getId());
			// if (subId <= crossoverId)
			// this.old = true;
			// }
			// }

			// TODO:
			this.seedRoot = part.id;
			// use the submission id as the seed root if available
			// if (part.assessment.getSubmissionContext() != null) {
			// this.seedRoot = part.assessment.getSubmissionContext().getId();
			// }
			//
			// // if no submission context, just the part id
			// else {
			// this.seedRoot = part.id;
			// }
		}

		/**
		 * {@inheritDoc}
		 */
		public void shuffle(List<? extends Object> source, String seedExtension) {
			// the old, 1.0.5 and before way of shuffling (ignore the seed extension, nothing fancy)
			// if (this.old) {
			// Collections.shuffle(source, new Random(this.seedRoot.hashCode()));
			// }
			//
			// else {
			// use the root and the extension, if given
			String seed = this.seedRoot + ((seedExtension != null) ? ("_" + seedExtension) : "");

			// we get much better results with 3 than 2 - use a null to pad it out, then remove it after shuffle
			if (source.size() == 2) {
				source.add(null);
				Collections.shuffle(source, new Random(seed.hashCode()));
				source.remove(null);
			} else {
				Collections.shuffle(source, new Random(seed.hashCode()));
			}
			// }
		}
	}

	/** Part id. */
	protected String id = null;

	/** Part ordering. */
	protected MyOrdering ordering = new MyOrdering(this);

	/** Part presentation. */
	protected Presentation presentation = new Presentation();

	/** If we should shuffle the questions from the as-authored order for delivery. */
	protected boolean randomize = false;

	/** Part title. */
	protected String title = null;

	/** List of part details. */
	List<PartDetail> details = new ArrayList<PartDetail>();

	// TODO: addDrawDetail, addPickDetail, removeDetail: change cannot be made to live tests

	/**
	 * Add a draw to the part; pool and count to draw from.
	 * 
	 * @param pool
	 *            The pool to draw from.
	 * @param numQuestions
	 *            The number of questions to draw.
	 * @return the PoolDraw detail added.
	 */
	public PoolDraw addDrawDetail(Pool pool, int numQuestions) {
		// do we have this pool already?
		for (PartDetail detail : getDetails()) {
			if (detail instanceof PoolDraw) {
				PoolDraw draw = (PoolDraw) detail;

				if (draw.getPoolId() == pool.getId()) {
					if (draw.getNumQuestions() == numQuestions) {
						// no change, we are done
						return draw;
					}

					// update the count
					draw.setNumQuestions(numQuestions);

					return draw;
				}
			}
		}

		// add this to the details
		PoolDraw rv = new PoolDraw(numQuestions, 0l, pool.getId());
		getDetails().add(rv);

		return rv;
	}

	/**
	 * Add a pick to the part; question.
	 * 
	 * @param question
	 *            The question to add.
	 * @return the QuestionPick detail added.
	 */
	public QuestionPick addPickDetail(Question question) {
		// do we have this already?
		for (PartDetail detail : getDetails()) {
			if (detail instanceof QuestionPick) {
				QuestionPick pick = (QuestionPick) detail;

				if (pick.getQuestionId() == question.getId()) {
					// no change, we are done
					return pick;
				}
			}
		}

		// add this to the details
		QuestionPick rv = new QuestionPick(0l, question.getId(), null);
		getDetails().add(rv);

		return rv;
	}

	/**
	 * Access the first question. The order will be in a random order (if enabled) based on the current user.
	 * 
	 * @return The first question, or null if there are none.
	 */
	public Question getFirstQuestion() {
		Question question = null;

		// TODO:
		// // if we are under a submission context, use the questions for the submission answers that are for this part
		// if (getAssessment().getSubmissionContext() != null) {
		// String questionId = null;
		// for (Answer answer : getAssessment().getSubmissionContext().getAnswers()) {
		// if (((AnswerImpl) answer).getPartId().equals(getId())) {
		// questionId = ((AnswerImpl) answer).questionId;
		// break;
		// }
		// }
		//
		// if (questionId != null) {
		// question = (QuestionImpl) getQuestion(questionId);
		// }
		// }
		//
		// else {
		// List<QuestionPick> order = getQuestionPickOrder();
		// question = (QuestionImpl) this.questionService.getQuestion(order.get(0).getQuestionId());
		//
		// // set the assessment, part and submission context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(((QuestionPick) order.get(0)).getRelatedDetail());
		// }

		return question;
	}

	/**
	 * Get a message describing what is wrong with the part.
	 * 
	 * @return A localized message describing what is wrong with the part, or null if the part is valid.
	 */
	public String getInvalidMessage() {
		// TODO:
		// // we must have details
		// if (getDetails().isEmpty())
		// {
		// Object[] args = new Object[1];
		// args[0] = this.getOrdering().getPosition().toString();
		// return messages.getFormattedMessage("invalid-part-empty", args);
		// }
		//
		// // each draw must have enough questions to draw
		// StringBuilder buf = new StringBuilder();
		// for (PartDetail detail : getDetails())
		// {
		// String msg = detail.getInvalidMessage();
		// if (msg != null)
		// {
		// if (buf.length() > 0)
		// {
		// buf.append("; ");
		// }
		// buf.append(msg);
		// }
		// }
		// if (buf.length() > 0)
		// {
		// Object[] args = new Object[2];
		// args[0] = this.getOrdering().getPosition().toString();
		// args[1] = buf.toString();
		// return messages.getFormattedMessage("invalid-part-details", args);
		// }
		//
		return null;
	}

	/**
	 * Access the last question. The order will be in a random order (if enabled) based on the current user.
	 * 
	 * @return The last question, or null if there are none.
	 */
	public Question getLastQuestion() {
		Question question = null;

		// TODO:
		// // if we are under a submission context, use the questions for the submission answers that are for this part
		// if (getAssessment().getSubmissionContext() != null) {
		// String questionId = null;
		// for (Answer answer : getAssessment().getSubmissionContext().getAnswers()) {
		// if (((AnswerImpl) answer).getPartId().equals(getId())) {
		// questionId = ((AnswerImpl) answer).questionId;
		// }
		// }
		//
		// if (questionId != null) {
		// question = (QuestionImpl) getQuestion(questionId);
		// }
		// }
		//
		// else {
		// List<QuestionPick> order = getQuestionPickOrder();
		// question = (QuestionImpl) this.questionService.getQuestion(order.get(order.size() - 1).getQuestionId());
		//
		// // set the assessment, part and submission context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(((QuestionPick) order.get(order.size() - 1)).getRelatedDetail());
		// }

		return question;
	}

	/**
	 * Access the count of questions.
	 * 
	 * @return The count of questions.
	 */
	public int getNumQuestions() {
		int count = 0;
		for (PartDetail detail : getDetails()) {
			count += detail.getNumQuestions();
		}

		return count;
	}

	/**
	 * Access one of the questions, by question id.
	 * 
	 * @param questionId
	 *            The question id.
	 * @return the question, or null if the question is not defined or not part of the Part.
	 */
	public Question getQuestion(String questionId) {
		// TODO:
		// // get the question
		// QuestionImpl question = (QuestionImpl) this.questionService.getQuestion(questionId);
		// if (question == null) {
		// M_log.warn("getQuestion: question not defined: " + questionId);
		// return null;
		// }
		//
		// // figure out what part detail it belongs to - picks first, then draws
		// // if not found in any, it is not in our part
		// boolean found = false;
		// for (PartDetail detail : getDetails()) {
		// if (detail instanceof QuestionPick) {
		// QuestionPick pick = (QuestionPick) detail;
		// if (questionId.equals(pick.getQuestionId())) {
		// // set some context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(detail);
		//
		// return question;
		// }
		// }
		// }
		//
		// if (!found) {
		// for (PartDetail detail : getDetails()) {
		// if (detail instanceof PoolDraw) {
		// PoolDraw draw = (PoolDraw) detail;
		// if (((QuestionImpl) question).poolId.equals(draw.getPoolId())) {
		// // set some context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(detail);
		//
		// return question;
		// }
		// }
		// }
		// }

		return null;
	}

	/**
	 * Access one of the questions, by question id, if it is in the part in a draw (not pick).
	 * 
	 * @param questionId
	 *            The question id.
	 * @return the question, or null if the question is not defined or not part of the Part as a draw.
	 */
	public Question getQuestionInDraw(String questionId) {
		// TODO:
		// // get the question
		// QuestionImpl question = (QuestionImpl) this.questionService.getQuestion(questionId);
		// if (question == null) {
		// M_log.warn("getQuestionInDraw: question not defined: " + questionId);
		// return null;
		// }
		//
		// // figure out what part detail it belongs to - draws only
		// for (PartDetail detail : getDetails()) {
		// if (detail instanceof PoolDraw) {
		// PoolDraw draw = (PoolDraw) detail;
		// if (((QuestionImpl) question).poolId.equals(draw.getPoolId())) {
		// // set some context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(detail);
		//
		// return question;
		// }
		// }
		// }

		return null;
	}

	/**
	 * Access one of the questions, by question id, if it is in the part in a pick (not draw).
	 * 
	 * @param questionId
	 *            The question id.
	 * @return the question, or null if the question is not defined or not part of the Part as a pick.
	 */
	public Question getQuestionInPick(String questionId) {
		// TODO:
		// // get the question
		// QuestionImpl question = (QuestionImpl) this.questionService.getQuestion(questionId);
		// if (question == null) {
		// M_log.warn("getQuestionInPick: question not defined: " + questionId);
		// return null;
		// }
		//
		// // figure out what part detail it belongs to - picks only
		// for (PartDetail detail : getDetails()) {
		// if (detail instanceof QuestionPick) {
		// QuestionPick pick = (QuestionPick) detail;
		// if (questionId.equals(pick.getQuestionId())) {
		// // set some context
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(detail);
		//
		// return question;
		// }
		// }
		// }

		return null;
	}

	/**
	 * Access the questions in delivery order.
	 * 
	 * @return The questions in delivery order.
	 */
	public List<Question> getQuestions() {
		List<Question> rv = new ArrayList<Question>();

		// TODO:
		// // if we are under a submission context which has answers, use the questions for the submission answers that are for this part
		// if ((getAssessment().getSubmissionContext() != null) && (!getAssessment().getSubmissionContext().getAnswers().isEmpty())) {
		// for (Answer answer : getAssessment().getSubmissionContext().getAnswers()) {
		// if (((AnswerImpl) answer).getPartId().equals(getId())) {
		// rv.add(getQuestion(((AnswerImpl) answer).questionId));
		// }
		// }
		// }
		//
		// // otherwise we need to pick questions
		// else {
		// List<QuestionPick> order = getQuestionPickOrder();
		// for (QuestionPick pick : order) {
		// QuestionImpl question = (QuestionImpl) this.questionService.getQuestion(pick.getQuestionId());
		// if (question != null) {
		// // set the question contexts
		// question.initSubmissionContext(this.assessment.getSubmissionContext());
		// question.initPartContext(this);
		// question.initPartDetailContext(((QuestionPick) pick).getRelatedDetail());
		//
		// rv.add(question);
		// }
		// }
		// }

		return rv;
	}

	/**
	 * Access the questions that have been used for this part in any submissions. <br />
	 * Order by question description.
	 * 
	 * @return The questions that have been used for this part in any submissions
	 */
	public List<Question> getQuestionsUsed() {
		// TODO:
		// return this.submissionService.findPartQuestions(this);
		return new ArrayList<>();
	}

	/**
	 * @return a non empty string describing the part; either the title, or text based on the part position.
	 */
	public String getTag() {
		// TODO:
		return "";
		// if ((this.title != null) && (this.title.length() > 0))
		// return this.title;
		// Object[] args = new Object[1];
		// args[0] = this.getOrdering().getPosition().toString();
		// return this.messages.getFormattedMessage("part-tag", args);
	}

	/**
	 * Access the sum of all possible points for all questions in the part.
	 * 
	 * @return The sum of all possible points for all questions in the part.
	 */
	public Float getTotalPoints() {
		// TODO:
		// // no point assessments have no points
		// if (!this.assessment.getHasPoints())
		// return Float.valueOf(0f);

		float total = 0f;
		// for (PartDetail detail : getDetails()) {
		// total += detail.getTotalPoints().floatValue();
		// }
		//
		// // round away bogus decimals
		// total = Math.round(total * 100.0f) / 100.0f;

		return Float.valueOf(total);
	}

	/**
	 * @return if the other has the same id as this.
	 */
	public boolean isSameId(Part other) {
		return this.getId().equals(other.getId());
	}

	/**
	 * @return validity.
	 */
	public boolean isValid() {
		// we must have details
		if (getDetails().isEmpty())
			return false;

		// each detail must be valid
		for (PartDetail detail : getDetails()) {
			if (!detail.isValid())
				return false;
		}

		return true;
	}

	/**
	 * Remove the detail with this detail id.
	 * 
	 * @param id
	 *            The detail id.
	 */
	public void removeDetail(String id) {
		if (id == null)
			return;

		for (Iterator<PartDetail> i = getDetails().iterator(); i.hasNext();) {
			PartDetail detail = i.next();
			if (id.equals(detail.getId())) {
				i.remove();
				// this.deletedDetails.add(detail);
			}
		}
	}

	// TODO: removeDrawDetail, removePickDetail, cannot be made to live tests

	/**
	 * Remove any draw detail that is for this pool.
	 * 
	 * @param pool
	 *            The pool to remove.
	 */
	public void removeDrawDetail(Pool pool) {
		for (Iterator<PartDetail> i = getDetails().iterator(); i.hasNext();) {
			PartDetail detail = i.next();
			if (detail instanceof PoolDraw) {
				PoolDraw draw = (PoolDraw) detail;

				if (draw.getPoolId() == pool.getId()) {
					i.remove();
					// this.deletedDetails.add(detail);
				}
			}
		}

	}

	/**
	 * Remove any pick detail that selects this question.
	 * 
	 * @param question
	 *            The question to remove.
	 */
	public void removePickDetail(Question question) {
		for (Iterator<PartDetail> i = getDetails().iterator(); i.hasNext();) {
			PartDetail detail = i.next();
			if (detail instanceof QuestionPick) {
				QuestionPick pick = (QuestionPick) detail;

				if (pick.getQuestionId() == question.getId()) {
					i.remove();
					// this.deletedDetails.add(detail);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String title) {
		// massage the title
		if (title != null) {
			title = title.trim();
			if (title.length() > 255)
				title = title.substring(0, 255);
			if (title.length() == 0)
				title = null;
		}

		this.title = title;
	}

	/**
	 * Get the list of possible question picks.
	 * 
	 * @return The list of possible question picks.
	 */
	protected List<QuestionPick> getPossibleQuestionPicks() {
		List<QuestionPick> rv = new ArrayList<QuestionPick>();
		for (PartDetail detail : getDetails()) {
			if (detail instanceof QuestionPick) {
				rv.add((QuestionPick) detail);
			} else if (detail instanceof PoolDraw) {
				PoolDraw draw = (PoolDraw) detail;

				List<Long> draws = draw.getAllQuestionIds();
				for (Long id : draws) {
					QuestionPick pick = new QuestionPick(id, id, draw);
					rv.add(pick);
				}
			}
		}

		return rv;
	}

	/**
	 * Get the list of question picks as they should be presented for the submission context.
	 * 
	 * @return The list of question picks as they should be presented for the submission context.
	 */
	protected List<QuestionPick> getQuestionPickOrder() {
		Shuffler shuffler = new ShufflerImpl(this);

		// Note: old DrawPart or ManualPart on conversion will be uniform draws (with shuffle) or picks (no shuffle).
		// Nothing special to do here with the shuffler to preserve question order -ggolden.

		// random draws from the pools
		List<QuestionPick> rv = new ArrayList<QuestionPick>();
		for (PartDetail detail : getDetails()) {
			if (detail instanceof PoolDraw) {
				PoolDraw draw = (PoolDraw) detail;

				List<Long> draws = draw.drawQuestionIds(shuffler);
				for (Long id : draws) {
					QuestionPick pick = new QuestionPick(id, id, draw);
					rv.add(pick);
				}
			} else if (detail instanceof QuestionPick) {
				rv.add((QuestionPick) detail);
			}
		}

		// randomize the questions if set
		if (isRandomize()) {
			shuffler.shuffle(rv, this.id);
		}

		return rv;
	}

	/**
	 * Reconstruct a draw.
	 * 
	 * @param id
	 *            The detail id.
	 * @param poolId
	 *            The poolId value.
	 * @param origPoolId
	 *            The origPoolId value
	 * @param numQuestions
	 *            The number of questions.
	 * @param points
	 *            The detail points value.
	 * @return the new part detail.
	 */
	// TODO:
	// protected PartDetail initDraw(String id, String poolId, String origPoolId, Integer numQuestions, Float points) {
	// PoolDraw draw = new PoolDrawImpl(this, this.poolService, id, poolId, origPoolId, numQuestions, points);
	// getDetails().add(draw);
	// return draw;
	// }

	/**
	 * Reconstruct a pick.
	 * 
	 * @param id
	 *            The detail id.
	 * @param questionId
	 *            The questionId value.
	 * @param origQuestionId
	 *            The origQuestionId value
	 * @param points
	 *            The detail points value.
	 * @return the new part detail.
	 */
	// TODO:
	// protected PartDetail initPick(String id, String questionId, String origQuestionId, Float points) {
	// QuestionPick pick = new QuestionPickImpl(this, this.questionService, id, questionId, origQuestionId, points);
	// getDetails().add(pick);
	// return pick;
	// }

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
		this.randomize = other.randomize;
		this.title = other.title;

		this.details = new ArrayList<PartDetail>(other.details.size());
		for (PartDetail detail : other.getDetails()) {
			if (detail instanceof PoolDraw) {
				PoolDraw newDraw = new PoolDraw();
				newDraw.set((PoolDraw) detail);
				getDetails().add(newDraw);
			} else if (detail instanceof QuestionPick) {
				QuestionPick newPick = new QuestionPick();
				newPick.set((QuestionPick) detail);
				getDetails().add(newPick);
			}
		}

		return this;
	}
}
