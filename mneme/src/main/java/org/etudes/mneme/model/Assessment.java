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
 * Models a Mneme assessment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Assessment {

	/**
	 * Type enumerates the different assessment types.
	 */
	public enum Type {
		assignment("A", 1), offline("O", 3), survey("S", 2), test("T", 0);

		public static Type fromCode(String code) {
			for (Type t : Type.values()) {
				if (t.code.equalsIgnoreCase(code)) {
					return t;
				}
			}

			return test;
		}

		/** Defines the codes to use to reconstruct the types (for example, from a db). */
		private final String code;

		/** Defines the sort order. */
		private final int sortOrder;

		private Type(String code, int sortOrder) {
			this.code = code;
			this.sortOrder = sortOrder;
		}

		public String getCode() {
			return code;
		}

		public Integer getSortValue() {
			return sortOrder;
		}
	}

	/** Options related to issuing certificates. */
	// protected CertificateOptions certificateOptions = new CertificateOptions();

	/** The assessment context - where it belongs in the LMS. */
	protected String context = null;

	/** Who and when created. */
//	@JsonProperty(access = Access.READ_ONLY)
//	@JsonIgnore
	protected Attribution created = new Attribution();

	/** Options related to delivery. */
	// protected DeliveryOptions deliveryOptions = new DeliveryOptions();

	/** Options related to grading. */
	// protected GradingOptions gradingOptions = new GradingOptions();

	/** Internal ID. 0 indicates not yet set. */
	// @JsonProperty(access = Access.READ_ONLY)
	protected long id = 0l;

	/** Last changed by and when. */
//	@JsonProperty(access = Access.READ_ONLY)
//	@JsonIgnore
	protected Attribution modified = new Attribution();

	/** Options related to notifications at assessment close. */
	// protected NotificationOptions notificationOptions = new NotificationOptions();

	/** The assessment parts. */
	// protected Parts parts = new Parts();

	/** Options related to assessment level points. */
	// protected PointsOptions pointsOptions = new PointsOptions();

	/** The auto-pool for this assessment. */
	// protected String poolId = null;

	/** The presentation for the assessment. */
	// protected Presentation presentation = new Presentation();

	/** Assessment review options. */
	// protected ReviewOptions reviewOptions = new ReviewOptions();

	/** The schedule. */
	protected Schedule schedule = new Schedule();

	/** Special access to the assessment. */
	// protected SpecialAccess specialAccess = new SpecialAccess();

	/** Details about the assessment status that change over time. */
	protected AssessmentStatus status = new AssessmentStatus();

	/** Subscription ID. 0 indicates not yet set. */
	protected long subscription = 0l;

	/** Some ??? other ??? presentation ??? */
	// protected Presentation submitPresentation = null;

	/** Assessment title. */
	protected String title = "";

	/** Assessment type. */
	protected Type type = Type.test;

	// /**
	// * Get the submit-able status for the assessment - this indicates if we are expecting submissions now.
	// *
	// * @return The submit-able status
	// */
	// public AcceptSubmitStatus getAcceptSubmitStatus() {
	// if (this.getStatus().isArchived())
	// return AcceptSubmitStatus.closed;
	// if (!this.getStatus().isPublished())
	// return AcceptSubmitStatus.closed;
	// if (isFrozen())
	// return AcceptSubmitStatus.closed;
	//
	// final Date now = new Date();
	//
	// // before open date, we are future (not yet open)
	// if ((this.getSchedule().getOpen() != null) && (now.before(this.getSchedule().getOpen()))) {
	// return AcceptSubmitStatus.future;
	// }
	//
	// // closed if we are after a defined getSubmitUntilDate
	// if ((this.getSchedule().getSubmitUntil() != null) && (now.after(this.getSchedule().getSubmitUntil()))) {
	// return AcceptSubmitStatus.closed;
	// }
	//
	// // after due date, we are late
	// if ((this.getSchedule().getDue() != null) && (now.after(this.getSchedule().getDue()))) {
	// return AcceptSubmitStatus.late;
	// }
	//
	// // otherwise, we are open
	// return AcceptSubmitStatus.open;
	// }

	// /**
	// * @return If the assessment, based on type, allows points.
	// */
	// public boolean isAllowedPoints() {
	// return this.type != AssessmentType.survey;
	// }

	// /**
	// * Check if student identities are invisible to the grader when grading.<br />
	// * Use this instead of the AssessmentGrading version for logic, use the other for editing settings.
	// *
	// * @return TRUE if student identities are invisible to the grader when grading, FALSE if not.
	// */
	// public boolean isAnonymous() {
	//
	// // surveys are always anon.
	// if (this.type == AssessmentType.survey)
	// return true;
	//
	// // otherwise use setting
	// return getGrading().isAnonymous();
	// }

	// /**
	// * @return If the assessment is frozen, adding in special rules concerning formal course evaluations
	// */
	// public boolean isFrozen() {
	//
	// // formal evals are automatically considered frozen once their due date is passed
	// if (this.getStatus().isFormalCourseEval() && (getSchedule().getSubmitUntil() != null) && getStatus().isPublished()) {
	//
	// final Date now = new Date();
	// if (now.after(getSchedule().getSubmitUntil()))
	// return true;
	// }
	//
	// return this.getStatus().isFrozen();
	// }

	// /**
	// * Check if grades are to be sent to the Gradebook application, considering type, points, hasPoints, and the grades setting.
	// *
	// * @return TRUE if the assessment's grades are to be placed into the Gradebook, FALSE if not.
	// */
	// public boolean isForGradebook() {
	//
	// // if not set for gb
	// if (!this.getGrading().isForGradebook())
	// return false;
	//
	// // set for gb, but...
	//
	// // not if we don't support points
	// if (!isAllowedPoints())
	// return false;
	//
	// // or don't have points
	// if (getPoints() <= 0f)
	// return false;
	//
	// return true;
	// }

	// /**
	// * Are multiple submissions allowed?
	// *
	// * @return TRUE if multiple submissions are allowed, FALSE if not.
	// */
	// public boolean isTriesMultiple() {
	// return ((this.getDeliveryOptions().getTries() == null) || (this.getDeliveryOptions().getTries().intValue() > 1));
	// }

	// /**
	// * Check if the assessment supports points, or if it does not. Survey type assessments, for example, do not support points.
	// *
	// * @return TRUE if the question supports points, FALSE if it does not.
	// */
	// public boolean isForPoints() {
	//
	// // not for points if points are not allowed
	// if (!isAllowedPoints()) {
	// return false;
	// }
	//
	// // not for points if set to not need points
	// if (!isPointsReqired()) {
	// return false;
	// }
	//
	// // TODO: do we need to check if there are actually points?
	//
	// return true;
	// }

	// /**
	// * @return If the time limit is set.
	// */
	// public boolean isTimeLimited() {
	// return this.getDeliveryOptions().getTimeLimit() != null;
	// }

	// /**
	// * @return If the tries limit is set.
	// */
	// public Boolean getHasTriesLimit() {
	// return this.getDeliveryOptions().getTries() != null;
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public Boolean isCurrentlyHiddenUntilOpen() {
	// final Date now = new Date();
	//
	// // it is currently hidden (until open) if so set, and the open date is in the future
	// return (this.getSchedule().isHideUntilOpen() && (this.getSchedule().getOpen() != null) && now.before(this.getSchedule().getOpen()));
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public Boolean getIsPointsValid() {
	// // only if we require points
	// if (this.isForPoints()) {
	// if (getType() == AssessmentType.offline) {
	// if (getPoints().floatValue() <= 0) {
	// return Boolean.FALSE;
	// }
	// } else {
	// // if we have questions
	// if (this.getParts().getNumQuestions() > 0) {
	// if (this.getParts().getTotalPoints().floatValue() <= 0) {
	// return Boolean.FALSE;
	// }
	// }
	// }
	// }
	//
	// return Boolean.TRUE;
	// }

	// /**
	// * @return If the assessment has just one question.
	// */
	// public boolean isSingleQuestion() {
	// return this.getParts().getNumQuestions() == 1;
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public Boolean getIsValid() {
	// // must have a title
	// if (getTitle().length() == 0)
	// return Boolean.FALSE;
	//
	// // dates valid
	// if (!this.dates.getIsValid())
	// return Boolean.FALSE;
	//
	// // parts valid
	// if (!this.parts.getIsValid())
	// return Boolean.FALSE;
	//
	// // grading valid
	// if (!this.grading.getIsValid())
	// return Boolean.FALSE;
	//
	// // points if needed
	// if (!this.getIsPointsValid())
	// return Boolean.FALSE;
	//
	// // formal course evaluations must have the results email set
	// if (this.getFormalCourseEval()) {
	// if (this.getResultsEmail() == null)
	// return Boolean.FALSE;
	// }
	// if (this.getResultsEmail() != null && this.getResultsEmail().length() > 255) {
	// setResultsEmail(this.getResultsEmail().substring(0, 255));
	// return Boolean.FALSE;
	// }
	// if (this.getResultsEmail() != null && !isEmailValid(this.getResultsEmail()))
	// return Boolean.FALSE;
	//
	// // results email feature needs a due or accept until date
	// if (this.getResultsEmail() != null && isEmailValid(this.getResultsEmail())) {
	// if ((this.dates.getDueDate() == null) && (this.dates.getAcceptUntilDate() == null))
	// return Boolean.FALSE;
	// }
	//
	// // FCE's notify-on-open feature needs an open date
	// if (this.getFormalCourseEval() && this.notifyEval && this.dates.getOpenDate() == null) {
	// return Boolean.FALSE;
	// }
	//
	// return Boolean.TRUE;
	// }

	// /**
	// * @return The assessment's total points (returns 0 if the assessment doesn't do points).
	// */
	// public float getPoints() {
	// if (!isForPoints()) {
	// return 0f;
	// }
	//
	// // offline assessments use the assessment points override value
	// if (this.type == AssessmentType.offline) {
	// return this.points;
	// }
	//
	// // otherwise we use the parts points
	// return this.parts.getTotalPoints();
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public Pool getPool() {
	// // see if the pool has been deleted - if so we will make a new one
	// if (this.poolId != null) {
	// Pool pool = this.poolService.getPool(this.poolId);
	// if (pool == null) {
	// this.poolId = null;
	// }
	// }
	//
	// if (this.poolId == null) {
	// try {
	// Pool pool = this.poolService.newPool(this.context);
	// this.poolId = pool.getId();
	// if (this.title.length() > 0) {
	// pool.setTitle(this.title);
	// }
	// // Note: if we don't set a >0 length title, the pool will have no changes, remain mint and disappear
	// else {
	// pool.setTitle(this.messages.getFormattedMessage("assessment-pool", null));
	// }
	// this.poolService.savePool(pool);
	// this.changed.setChanged();
	// } catch (AssessmentPermissionException e) {
	// M_log.warn("getPool: " + e.toString());
	// }
	// }
	//
	// return this.poolService.getPool(this.poolId);
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public boolean isEmailValid(String emailAddr) {
	// Pattern pattern;
	// Matcher matcher;
	//
	// String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	//
	// pattern = Pattern.compile(EMAIL_PATTERN);
	//
	// if (emailAddr == null || emailAddr.trim().length() == 0)
	// return false;
	// if (!emailAddr.contains(",")) {
	// matcher = pattern.matcher(emailAddr.trim());
	// return matcher.matches();
	// } else {
	// List<String> emailList = Arrays.asList(emailAddr.split(","));
	// for (String emailStr : emailList) {
	// matcher = pattern.matcher(emailStr.trim());
	// if (!matcher.matches())
	// return false;
	// }
	// }
	// return true;
	// }

	// /**
	// * Set the archived flag, and related information.
	// *
	// * @param archived
	// * The archived flag.
	// */
	// public void setArchived(boolean archived) {
	//
	// this.getStatus().setArchived(archived);
	//
	// // if now archived, set the date, and un-publish
	// if (this.getStatus().isArchived()) {
	// final Date now = new Date();
	//
	// this.getSchedule().setArchived(now);
	// this.getStatus().setPublished(false);
	// }
	//
	// // else clear the archived date
	// else {
	// this.getSchedule().setArchived(null);
	// }
	// }

	// TODO: setFormalCourseEval cannot be made to locked assessments
	// TODO: special permission? assessmentService.allowSetFormalCourseEvaluation(getContext()))
	// TODO: cannot change tries limit for FCE

	// /**
	// * {@inheritDoc}
	// */
	// public void setHasTimeLimit(Boolean hasTimeLimit) {
	// if (hasTimeLimit == null)
	// throw new IllegalArgumentException();
	//
	// if ((!hasTimeLimit) && (this.timeLimit != null)) {
	// this.timeLimit = null;
	//
	// this.changed.setChanged();
	// }
	// }
	//
	// /**
	// * {@inheritDoc}
	// */
	// public void setHasTriesLimit(Boolean hasTriesLimit) {
	// if (hasTriesLimit == null)
	// throw new IllegalArgumentException();
	//
	// if ((!hasTriesLimit) && (this.tries != null)) {
	// this.tries = null;
	//
	// this.changed.setChanged();
	//
	// // this is a change that cannot be made to locked assessments if set to a formal course evaluation
	// if (this.formalCourseEval)
	// this.lockedChanged = Boolean.TRUE;
	// }
	// }

	// /**
	// * Set the points, after adjusting the parameter.
	// *
	// * @param points
	// * The points for the assessment, overriding the parts points (only used by offline assessments).
	// */
	// public void setPoints(float points) {
	//
	// // round away bogus decimals
	// points = Math.round(points * 100.0f) / 100.0f;
	//
	// this.points = points;
	// }

	// /**
	// * Set the random access delivery option, enforcing related settings.
	// *
	// * @param settings
	// * The random access setting.
	// */
	// // TODO: move to DeliveryOptions
	// public void setRandomAccess(boolean setting) {
	//
	// this.getDeliveryOptions().setRandomAccess(setting);
	//
	// // strict order needs by question
	// if (!this.getDeliveryOptions().isRandomAccess()) {
	// this.getDeliveryOptions().setQuestionGrouping(DeliveryOptions.QuestionGrouping.question);
	// }
	// }

	// TODO: setResultsEmail cannot be made to locked assessments if set to a formal course evaluation

	// /**
	// * Set the time limit, after adjusting the parameter.
	// *
	// * @param limit
	// * The time limit.
	// */
	// // TODO: move to DeliveryOptions
	// public void setTimeLimit(Long limit) {
	//
	// // minimum of one minute
	// if ((limit != null) && (limit.longValue() < 60000l))
	// limit = new Long(60000l);
	//
	// this.getDeliveryOptions().setTimeLimit(limit);
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public void setTitle(String title) {
	// // massage the title
	// if (title != null) {
	// title = title.trim();
	// if (title.length() > 255)
	// title = title.substring(0, 255);
	// } else {
	// title = "";
	// }
	//
	// if (this.title.equals(title))
	// return;
	//
	// this.title = title;
	//
	// this.changed.setChanged();
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public void setTries(Integer count) {
	// // if < 1, set to 1
	// if ((count != null) && (count.intValue() < 1)) {
	// count = Integer.valueOf(1);
	// }
	//
	// if (!Different.different(count, this.tries))
	// return;
	//
	// this.tries = count;
	//
	// // this is a change that cannot be made to locked assessments if set to a formal course evaluation
	// if (this.formalCourseEval)
	// this.lockedChanged = Boolean.TRUE;
	//
	// this.changed.setChanged();
	// }

	// /**
	// * {@inheritDoc}
	// */
	// public void setType(AssessmentType type) {
	// if (type == null)
	// throw new IllegalArgumentException();
	// if (this.type.equals(type))
	// return;
	//
	// if (this.type == AssessmentType.survey) {
	// // this is a change that cannot be made to live tests
	// this.lockedChanged = Boolean.TRUE;
	// }
	//
	// this.type = type;
	// this.changed.setChanged();
	//
	// // Note: for the settings that automatically get set when the type changes, see the AsessmentServiceImpl.java's saveAssessment() method -ggolden
	// }

	// /**
	// * Lock the assessment, locking down the dependencies (pools and questions).
	// */
	// protected void lock() {
	// if (this.locked)
	// return;
	// initLocked(Boolean.TRUE);
	//
	// Map<String, Pool> histories = new HashMap<String, Pool>();
	// Map<String, Map<String, String>> oldToNews = new HashMap<String, Map<String, String>>();
	//
	// // make a history copy of all used pools and questions
	// // switch over the parts
	// // make sure questions from the same pool end up in the same pool
	//
	// for (Part part : this.parts.parts) {
	// ((Part) part).changed = true;
	//
	// for (PartDetail detail : part.getDetails()) {
	// if (detail instanceof PoolDraw) {
	// PoolDraw draw = (PoolDraw) detail;
	//
	// // if we have not yet made a history for this pool, do so
	// Pool history = histories.get(draw.getPoolId());
	// if (history == null) {
	// Map<String, String> oldToNew = new HashMap<String, String>();
	// history = this.poolService.makePoolHistory(draw.getPool(), oldToNew);
	// histories.put(draw.getPoolId(), history);
	// oldToNews.put(draw.getPoolId(), oldToNew);
	// }
	// draw.setPool(history);
	// } else if (detail instanceof QuestionPick) {
	// QuestionPick pick = (QuestionPick) detail;
	//
	// Question q = this.questionService.getQuestion(pick.getQuestionId());
	// if (q != null) {
	// // make sure we have this question's complete pool
	// Pool history = histories.get(q.getPool().getId());
	// if (history == null) {
	// Map<String, String> oldToNew = new HashMap<String, String>();
	// history = this.poolService.makePoolHistory(q.getPool(), oldToNew);
	// histories.put(q.getPool().getId(), history);
	// oldToNews.put(q.getPool().getId(), oldToNew);
	// }
	//
	// // get the mapping for this pool
	// Map<String, String> oldToNew = oldToNews.get(q.getPool().getId());
	// String historicalQid = oldToNew.get(q.getId());
	// if (historicalQid != null) {
	// pick.setQuestionId(historicalQid);
	// }
	// }
	// }
	// }
	// }
	// }

	// /**
	// * Set as a copy of another.
	// *
	// * @param other
	// * The other to copy.
	// * @return this (for chaining).
	// */
	// public Assessment set(Assessment other) {
	// this.certificateOptions = new CertificateOptions().set(other.getCertificateOptions());
	// this.context = other.getContext();
	// this.created = new Attribution().set(other.getCreated());
	// this.deliveryOptions = new DeliveryOptions().set(other.getDeliveryOptions());
	// this.gradingOptions = new GradingOptions().set(other.getGradingOptions());
	// this.id = other.getId();
	// this.modified = new Attribution().set(other.getModified());
	// this.notificationOptions = new NotificationOptions().set(other.getNotificationOptions());
	// this.parts = new Parts().set(other.getParts());
	// this.pointsOptions = new PointsOptions().set(other.getPointsOptions());
	// this.poolId = other.getPoolId();
	// this.presentation = new Presentation().set(other.getPresentation());
	// this.reviewOptions = new ReviewOptions().set(other.getReviewOptions());
	// this.schedule = new Schedule().set(other.getSchedule());
	// this.specialAccess = new SpecialAccess().set(other.getSpecialAccess());
	// this.status = new AssessmentStatus().set(other.getStatus());
	// this.title = other.title;
	// this.type = other.type;
	//
	// return this;
	// }
}
