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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Parts models the collection of parts of an assessment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Parts {
	/** If numbering of questions across part boundaries should be continuous or not. */
	protected boolean continuousNumbering = true;

	/** The assessment parts. */
	protected List<Part> parts = new ArrayList<Part>();

	/** If we are to show part presentations. */
	// TODO: If not specifically set, the value will be FALSE if all the parts are defined with no title and no descriptions, and if the continuous numbering is
	// set to FALSE.
	protected boolean showPresentation = false;

	// TODO: addPart, clear, moveDetails, cannot be made to live assessments

	/**
	 * Add a hybrid (draw and manual) part.
	 * 
	 * @return The new part.
	 */
	public Part addPart() {
		// create the new part
		Part rv = new Part();

		// add it to the list
		this.parts.add(rv);
		// ((Part) rv).initContainer(this.parts);

		return rv;
	}

	/**
	 * Remove all parts.
	 */
	public void clear() {
		this.parts.clear();
	}

	/**
	 * Access the part detail with this id.
	 * 
	 * @return The part detail with this id, or null if not found.
	 */
	public PartDetail getDetailId(String id) {
		for (Part part : getParts()) {
			for (PartDetail detail : part.getDetails()) {
				if (id.equals(detail.getId()))
					return detail;
			}
		}

		return null;
	}

	/**
	 * Access all the part details in a single ordered (by part and sequence) list.
	 * 
	 * @return The list of all part details.
	 */
	public List<PartDetail> getDetails() {
		List<PartDetail> rv = new ArrayList<PartDetail>();

		for (Part part : getParts()) {
			rv.addAll(part.getDetails());
		}

		return rv;
	}

	/**
	 * Get all the pools drawn from in the assessment parts, and their counts, sorted.
	 * 
	 * @return The List of draws.
	 */
	public List<PoolDraw> getDraws(/*final PoolService.FindPoolsSort sort*/) {
		List<PoolDraw> rv = new ArrayList<PoolDraw>();
		for (PartDetail detail : getDetails()) {
			if (detail instanceof PoolDraw) {
				rv.add((PoolDraw) detail);
			}
		}

		// TODO:

		// // sort
		// if (sort != null)
		// {
		// Collections.sort(rv, new Comparator<PoolDraw>()
		// {
		// public int compare(PoolDraw arg0, PoolDraw arg1)
		// {
		// int rv = 0;
		// switch (sort)
		// {
		// case title_a:
		// {
		// String s0 = StringUtil.trimToZero(((PoolDraw) arg0).getPool().getTitle());
		// String s1 = StringUtil.trimToZero(((PoolDraw) arg1).getPool().getTitle());
		// rv = s0.compareToIgnoreCase(s1);
		// break;
		// }
		// case title_d:
		// {
		// String s0 = StringUtil.trimToZero(((PoolDraw) arg0).getPool().getTitle());
		// String s1 = StringUtil.trimToZero(((PoolDraw) arg1).getPool().getTitle());
		// rv = -1 * s0.compareToIgnoreCase(s1);
		// break;
		// }
		// case points_a:
		// {
		// Float f0 = ((PoolDraw) arg0).getPool().getPoints();
		// if (f0 == null) f0 = Float.valueOf(0f);
		// Float f1 = ((PoolDraw) arg1).getPool().getPoints();
		// if (f1 == null) f1 = Float.valueOf(0f);
		// rv = f0.compareTo(f1);
		// break;
		// }
		// case points_d:
		// {
		// Float f0 = ((PoolDraw) arg0).getPool().getPoints();
		// if (f0 == null) f0 = Float.valueOf(0f);
		// Float f1 = ((PoolDraw) arg1).getPool().getPoints();
		// if (f1 == null) f1 = Float.valueOf(0f);
		// rv = -1 * f0.compareTo(f1);
		// break;
		// }
		// }
		//
		// return rv;
		// }
		// });
		// }

		return rv;
	}

	/**
	 * Get a list of draws for each pool specified - for those that are already in this part, set the non-null numQuestions.<br />
	 * The draws are virtual, not in the Parts.
	 * 
	 * @param context
	 *            The context.
	 * @param sort
	 *            The sort criteria (from the PoolService).
	 * @param search
	 *            The search criteria.
	 * @return A list of draws for each pool.
	 */
	public List<PoolDraw> getDrawsForPools(String context, /*PoolService.FindPoolsSort sort, */ String search) {
		// TODO:
		return new ArrayList<>();
		// // get all the pools we need
		// List<Pool> allPools = this.poolService.findPools(context, sort, search);
		//
		// List<PoolDraw> rv = new ArrayList<PoolDraw>();
		//
		// for (Pool pool : allPools)
		// {
		// PoolDraw draw = new PoolDraw(this.assessment, null, this.poolService, pool, null, null);
		// for (PartDetail detail : getDetails())
		// {
		// if (detail instanceof PoolDraw)
		// {
		// PoolDraw myDraw = (PoolDraw) detail;
		//
		// if (myDraw.getPoolId().equals(pool.getId()))
		// {
		// draw.setNumQuestions(myDraw.getNumQuestions());
		// }
		// }
		// }
		//
		// rv.add(draw);
		// }
		//
		// return rv;
	}

	/**
	 * Access the first part
	 * 
	 * @return the first part, of null if there are none.
	 */
	public Part getFirst() {
		if (this.parts.isEmpty())
			return null;

		return this.parts.get(0);
	}

	/**
	 * Get a message describing what is wrong with the part.
	 * 
	 * @return A localized message describing what is wrong with the part, or null if the part is valid.
	 */
	public String getInvalidMessage() {
		// TODO:
		// // we must only draw from a pool once across all draw parts
		// List<String> poolIds = new ArrayList<String>();
		// for (Part part : this.parts)
		// {
		// for (PartDetail detail : part.getDetails())
		// {
		// if (detail instanceof PoolDraw)
		// {
		// PoolDraw draw = (PoolDraw) detail;
		//
		// if (poolIds.contains(draw.getPoolId()))
		// {
		// Object[] args = new Object[1];
		// args[0] = draw.getPool().getTitle();
		// return this.messages.getFormattedMessage("invalid-detail-multi-draw", args);
		// }
		//
		// poolIds.add(draw.getPoolId());
		// }
		// }
		// }
		//
		// // we must pick a question only once each across all manual parts
		// List<String> questionIds = new ArrayList<String>();
		// for (Part part : this.parts)
		// {
		// for (PartDetail detail : part.getDetails())
		// {
		// if (detail instanceof QuestionPick)
		// {
		// QuestionPick pick = (QuestionPick) detail;
		//
		// if (questionIds.contains(pick.getQuestionId()))
		// {
		// Object[] args = new Object[1];
		// args[0] = pick.getQuestion().getDescription();
		// return this.messages.getFormattedMessage("invalid-detail-multi-pick", args);
		// }
		//
		// questionIds.add(pick.getQuestionId());
		// }
		// }
		// }
		return null;
	}

	/**
	 * Check if the assessment parts are valid; i.e. exist and all have >0 questions
	 * 
	 * @return TRUE if the assessment parts are valid, FALSE if not.
	 */
	public boolean isValid() {
		// offlines need no parts
		// TODO:
		// if (this.assessment.getType() == AssessmentType.offline) return Boolean.TRUE;

		// we must have some parts defined
		if (this.parts.isEmpty())
			return false;

		// each part must be valid
		for (Part part : this.parts) {
			if (!part.isValid())
				return false;
		}

		// we must only draw from a pool once across all draw parts
		List<String> poolIds = new ArrayList<String>();
		for (Part part : this.parts) {
			for (PartDetail detail : part.getDetails()) {
				if (detail instanceof PoolDraw) {
					PoolDraw draw = (PoolDraw) detail;

					if (poolIds.contains(draw.getPoolId())) {
						return Boolean.FALSE;
					}

					poolIds.add(draw.getPoolId());
				}
			}
		}

		// we must pick a question only once each across all manual parts
		List<String> questionIds = new ArrayList<String>();
		for (Part part : this.parts) {
			for (PartDetail detail : part.getDetails()) {
				if (detail instanceof QuestionPick) {
					QuestionPick pick = (QuestionPick) detail;

					if (questionIds.contains(pick.getQuestionId())) {
						return Boolean.FALSE;
					}

					questionIds.add(pick.getQuestionId());
				}
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Access the count of questions randomly drawn from pools in all parts.
	 * 
	 * @return The count of questions randomly drawn in all parts.
	 */
	public int getNumDrawQuestions() {
		int rv = 0;
		for (PartDetail detail : getDetails()) {
			if (detail instanceof PoolDraw) {
				rv += ((PoolDraw) detail).getNumQuestions();
			}
		}

		return rv;
	}

	/**
	 * Access the number of parts.
	 * 
	 * @return The number of parts.
	 */
	public int getNumParts() {
		int rv = this.parts.size();
		return rv;
	}

	/**
	 * Access the count of questions in all parts.
	 * 
	 * @return The count of questions in all parts.
	 */
	public int getNumQuestions() {
		int rv = 0;
		for (Part part : this.parts) {
			rv += part.getNumQuestions();
		}

		return rv;
	}

	/**
	 * Access one of the part, by id.
	 * 
	 * @param id
	 *            The part id.
	 * @return the part, or null if not found.
	 */
	public Part getPart(String id) {
		if (id == null)
			throw new IllegalArgumentException();
		for (Part part : this.parts) {
			if (part.getId().equals(id))
				return part;
		}

		return null;
	}

	/**
	 * Access all the part details in a single ordered (by part and sequence) list.<br />
	 * If a part is empty, include a phantom detail for it.
	 * 
	 * @return The list of all part details.
	 */
	public List<PartDetail> getPhantomDetails() {
		List<PartDetail> rv = new ArrayList<PartDetail>();

		for (Part part : getParts()) {
			if (part.getDetails().isEmpty()) {
				// TODO:
				// rv.add(new EmptyPartDetailImpl(part));
			} else {
				rv.addAll(part.getDetails());
			}
		}

		return rv;
	}

	/**
	 * Access one of the questions, by question id.
	 * 
	 * @param questionId
	 *            The question id.
	 * @return the question, or null if not found.
	 */
	public Question getQuestion(String questionId) {
		if (questionId == null)
			throw new IllegalArgumentException();

		// look in all the parts - first considering only picks
		for (Part part : this.parts) {
			Question question = part.getQuestionInPick(questionId);
			if (question != null)
				return question;
		}

		// if we didn't find this question as a pick in some part, see if it might be part of a draw
		for (Part part : this.parts) {
			Question question = part.getQuestionInDraw(questionId);
			if (question != null)
				return question;
		}

		return null;
	}

	/**
	 * Access the questions across all parts in delivery order.
	 * 
	 * @return The questions across all parts in delivery order.
	 */
	public List<Question> getQuestions() {
		List<Question> rv = new ArrayList<Question>();
		for (Part part : this.parts) {
			rv.addAll(part.getQuestions());
		}

		return rv;
	}

	// TODO:
	// /**
	// * Access the show-presentation setting; this controls the display of each part's presentation.<br />
	// * If not specifically set, the value will be FALSE if all the parts are defined with no title and no<br />
	// * descriptions, and if the continuous numbering is set to FALSE.
	// *
	// * @return TRUE to show the part presentations, FALSE to not show them.
	// */
	// public Boolean getShowPresentation()
	// {
	// if (this.showPresentation != null) return this.showPresentation;
	//
	// // compute it by checking the continuous numbering and the parts titles and presentation text
	// if (!getContinuousNumbering()) return Boolean.TRUE;
	//
	// for (Part part : this.parts)
	// {
	// if ((part.getTitle() != null) || (part.getPresentation().getText() != null)) return Boolean.TRUE;
	// }
	//
	// return Boolean.FALSE;
	// }

	/**
	 * Access the sum of all possible points for this assessment.
	 * 
	 * @return The sum of all possible points for this assessment.
	 */
	public float getTotalPoints() {
		// TODO:
		// no point assessments have no points
		// if (!this.assessment.getHasPoints()) return Float.valueOf(0f);

		float rv = 0f;
		for (Part part : this.parts) {
			rv += part.getTotalPoints();
		}

		// round away bogus decimals
		rv = Math.round(rv * 100.0f) / 100.0f;

		return rv;
	}

	/**
	 * Get a virtual draw for this pool, set to the same count as one of our draws if we have one, else set to 0.<br />
	 * The draw is virtual, not a detail of a Part.
	 * 
	 * @return The virtual PoolDraw for this pool.
	 */
	public PoolDraw getVirtualDraw(Pool pool) {
		PoolDraw rv = new PoolDraw(0, null, pool.getId());
		for (PartDetail detail : getDetails()) {
			if (detail instanceof PoolDraw) {
				PoolDraw myDraw = (PoolDraw) detail;

				if (myDraw.getPoolId().equals(pool.getId())) {
					rv.setNumQuestions(myDraw.getNumQuestions());
				}
			}
		}

		return rv;
	}

	/**
	 * Move the details from wherever part they reside into the destination part.
	 * 
	 * @param detailIds
	 *            The detail ids array.
	 * @param destination
	 *            The destination part.
	 */
	public void moveDetails(String[] detailIds, Part destination) {
		if (detailIds == null)
			throw new IllegalArgumentException();
		if (destination == null)
			throw new IllegalArgumentException();
		// TODO:
		// if (destination.getAssessment() != this.assessment) throw new IllegalArgumentException();

		for (String detailId : detailIds) {
			// get the detail to move
			PartDetail detail = getDetailId(detailId);
			if (detail == null)
				continue;

			// TODO:
			// if it is in the desired part already
			// if (detail.getPart() == destination) continue;

			// remove it - but not so it gets deleted
			// ((Part) detail.getPart()).setChanged();
			// for (Iterator<PartDetail> i = detail.getPart().getDetails().iterator(); i.hasNext();)
			// {
			// PartDetail d = i.next();
			// if (d == detail)
			// {
			// i.remove();
			// break;
			// }
			// }
			//
			// // add it
			//// ((Part) destination).setChanged();
			// destination.getDetails().add(detail);

			// ((PartDetail) detail).setChanged();
		}
	}

	/**
	 * Remove the detail with this detail id, from any part.
	 * 
	 * @param id
	 *            The detail id.
	 */
	public void removeDetail(String id) {
		for (Part part : this.parts) {
			part.removeDetail(id);
		}
	}

	/**
	 * Remove any parts that have no title, no description, and no questions.
	 */
	public void removeEmptyParts() {
		for (Iterator<Part> i = this.parts.iterator(); i.hasNext();) {
			Part part = i.next();

			if ((part.getTitle() == null) && (part.getPresentation().getText() == null) && (part.getPresentation().getAttachments().isEmpty())) {
				if (part.getDetails().isEmpty()) {
					i.remove();
				}
			}
		}
	}

	/**
	 * Remove this part.
	 * 
	 * @param part
	 *            The part to remove.
	 */
	public void removePart(Part part) {
		this.parts.remove(part);
	}

	/**
	 * Reorder the existing parts to match this order.<br />
	 * Any parts not listed remain in their order following this list.<br />
	 * Any parts in the list not matching existing parts are ignored.
	 * 
	 * @param partIds
	 *            A list of the part ids in order.
	 */
	public void setOrder(String[] partIds) {
		if (partIds == null)
			return;
		List<String> ids = new ArrayList<String>(Arrays.asList(partIds));

		// remove anything from the new list not in our parts
		for (Iterator<String> i = ids.iterator(); i.hasNext();) {
			String id = i.next();
			Part part = getPart(id);
			if (part == null) {
				i.remove();
			}
		}

		// start with these
		List<Part> newParts = new ArrayList<Part>();
		for (String id : ids) {
			newParts.add(getPart(id));
		}

		// pick up the rest
		for (Part part : this.parts) {
			if (!ids.contains(part.getId())) {
				newParts.add(part);
			}
		}

		// see if the new list and the old line up - i.e. no changes
		boolean changed = false;
		for (int i = 0; i < newParts.size(); i++) {
			if (!this.parts.get(i).equals(newParts.get(i))) {
				changed = true;
				break;
			}
		}

		// ignore if no changes
		if (!changed)
			return;

		// take the new list
		this.parts = newParts;
	}

	/**
	 * Apply the set of draws in the list to the parts.<br />
	 * If the draw has no count, and the pool is in a part, remove it.<br />
	 * If the draw has a count, and the pool is already drawn in a part, make sure the count matches.<br />
	 * Any additional draws not in parts get added to the indicated part.
	 * 
	 * @param draws
	 *            The list of (virtual) draws.
	 * @param partForNewDraws
	 *            The part to get any new draws.
	 */
	public void updateDraws(List<PoolDraw> draws, Part partForNewDraws) {
		// TODO:
		// for (PoolDraw newDraw : draws)
		// {
		// // if it is empty, make sure it is removed from any part
		// if (newDraw.getNumQuestions() == 0)
		// {
		// for (Part part : this.parts)
		// {
		// part.removeDrawDetail(newDraw.getPool());
		// }
		// }
		//
		// // if it has count, see if we need to update its presence in a part
		// else
		// {
		// boolean used = false;
		// for (Part part : this.parts)
		// {
		// for (PartDetail detail : part.getDetails())
		// {
		// if (detail instanceof PoolDraw)
		// {
		// PoolDraw oldDraw = (PoolDraw) detail;
		//
		// // is this pool drawn in the part?
		// if (oldDraw.getPoolId().equals(newDraw.getPoolId()))
		// {
		// used = true;
		//
		// // is the count different?
		// if (oldDraw.getNumQuestions() != newDraw.getNumQuestions())
		// {
		// // update the part
		// part.addDrawDetail(newDraw.getPool(), newDraw.getNumQuestions());
		// }
		// }
		// }
		// }
		// }
		//
		// // if we didn't find that it was used, add it to part 1, creating the part if needed
		// if ((!used) && (partForNewDraws != null) && (partForNewDraws.getAssessment() == assessment))
		// {
		// if (getParts().isEmpty())
		// {
		// addPart();
		// }
		// partForNewDraws.addDrawDetail(newDraw.getPool(), newDraw.getNumQuestions());
		// }
		// }
		// }
	}

	/**
	 * Get the question ids that are manually selected from this pool, from all parts. Only include valid questions.
	 * 
	 * @param pool
	 *            The pool.
	 * @param survey
	 *            if null, consider all questions; else consider only questions that match survey in their survey setting.
	 * @return The question ids that are manually selected from this pool, from all parts.
	 */
	protected List<String> getQuestionPicksFromPool(Pool pool, Boolean survey) {
		List<String> rv = new ArrayList<String>();

		// TODO:
		// for (Part part : this.parts)
		// {
		// for (PartDetail detail : part.getDetails())
		// {
		// // only consider picks
		// if (detail instanceof QuestionPick)
		// {
		// QuestionPick pick = (QuestionPick) detail;
		// Question question = this.questionService.getQuestion(pick.getQuestionId());
		// if (question == null) continue;
		// if (!question.getIsValid().booleanValue()) continue;
		// if ((survey != null) && (!question.getIsSurvey().equals(survey))) continue;
		// if (!question.getPool().getId().equals(pool.getId())) continue;
		//
		// rv.add(pick.getQuestionId());
		// }
		// }
		// }

		return rv;
	}

	/**
	 * Set as a copy of another (deep copy).
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Parts set(Parts other) {
		this.continuousNumbering = other.continuousNumbering;
		this.parts = new ArrayList<Part>(other.parts.size());
		this.showPresentation = other.showPresentation;

		for (Part part : other.parts) {
			Part newPart = new Part();
			newPart.set(part);
			this.parts.add(newPart);
		}

		return this;
	}

	// /**
	// * Update the sequence numbers for all the parts' details.
	// */
	// protected void setDetailSeq()
	// {
	// for (Part part : this.parts)
	// {
	// int seq = 1;
	// for (PartDetail detail : part.getDetails())
	// {
	// if (((PartDetail) detail).getSeq() != seq)
	// {
	// ((PartDetail) detail).initSeq(seq);
	// ((PartDetail) detail).setChanged();
	// }
	// seq++;
	// }
	// }
	// }
}
