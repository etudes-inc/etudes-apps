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

package org.etudes.mneme.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.etudes.mneme.AssessmentService;
import org.etudes.mneme.data.AssessmentData;
import org.etudes.mneme.model.Assessment;
import org.etudes.mneme.model.Attribution;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssessmentServiceImpl implements AssessmentService {
	final static private Logger logger = LoggerFactory.getLogger(AssessmentServiceImpl.class);

	@NotNull
	final protected AssessmentData data;

	/**
	 * Create the assessment service
	 * 
	 * @param data
	 *            An assessmentData implementation to give access to the persisted data.
	 */
	@Inject
	public AssessmentServiceImpl(AssessmentData data) {
		this.data = data;
		logger.info("AuthenticationService");
	}

	@Override
	public Optional<Assessment> createAssessment(long subscriptionId, String context, long userId, Assessment asmt) {
		final Date now = new Date();

		asmt.setSubscription(subscriptionId);
		asmt.setContext(context);
		asmt.setCreated(new Attribution(now, userId));
		asmt.setModified(new Attribution(now, userId));

		this.clean(asmt);

		return this.data.create(asmt);
	}

	@Override
	public Optional<Assessment> getAssessment(long id) {
		return this.data.readAssessment(id);
	}

	@Override
	public List<Assessment> getContextAssessments(long subscriptionId, String context, boolean includeOnlyPublishedAndValid) {

		// TODO Auto-generated method stub

		List<Assessment> rv = new ArrayList<>();

		Assessment a = new Assessment();
		a.setId(1l);
		a.getSchedule().setOpen(Optional.of(new Date()));
		a.getSchedule().setDue(Optional.of(new Date()));
		a.getSchedule().setUntil(Optional.of(new Date()));
		a.setType(Assessment.Type.assignment);
		a.setTitle("First Test");
		a.getStatus().setPublished(true);
		rv.add(a);

		a = new Assessment();
		a.setId(2l);
		a.getSchedule().setOpen(Optional.of(new Date()));
		a.getSchedule().setDue(Optional.of(new Date()));
		a.getSchedule().setUntil(Optional.of(new Date()));
		a.setType(Assessment.Type.assignment);
		a.setTitle("Second Test");
		a.getStatus().setPublished(false);
		rv.add(a);

		a = new Assessment();
		a.setId(3l);
		a.getSchedule().setOpen(Optional.of(new Date()));
		a.getSchedule().setDue(Optional.of(new Date()));
		a.getSchedule().setUntil(Optional.of(new Date()));
		a.setType(Assessment.Type.assignment);
		a.setTitle("Third Test");
		a.getStatus().setPublished(true);
		rv.add(a);

		return rv;
	}

	@Override
	public Optional<Assessment> saveAssessment(long userId, Assessment current, Assessment updated) {

		final Date now = new Date();
		updated.setModified(new Attribution(now, userId));

		this.clean(updated);

		return this.data.updateAssessment(current, updated);
	}

	/**
	 * Enforce any rules about assessment properties, adjusting the properties as needed.
	 * 
	 * @param asmt
	 *            The assessment to clean
	 */
	protected void clean(Assessment asmt) {

		// rule: title must be 0 .. 255 characters if defined
		if (asmt.getTitle() != null) {
			String title = asmt.getTitle();
			title = title.trim();
			if (title.length() > 255) {
				title = title.substring(0, 255);
			}
			asmt.setTitle(title);
		}

		// TODO:
		// part titles to 255
		// we want points rounded to 2 decimals, and we want it in a reasonable range 0 .. 10000
		// PartDetailProvidingPoolDraw must have enough question (that are not specifically included) to draw
	}
}
