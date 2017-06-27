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

	final protected AssessmentData data;

	/**
	 * Create the assessment service
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

		// clean up values
		clean(asmt);

		// create
		Optional<Assessment> rv = data.create(asmt);

		return rv;
	}

	@Override
	public Optional<Assessment> getAssessment(long id) {
		Optional<Assessment> rv = data.readAssessment(id);

		return rv;
	}

	@Override
	public List<Assessment> getContextAssessments(long subscriptionId, String context, Sort sort, Boolean publishedOnly) {
		// TODO Auto-generated method stub

		List<Assessment> rv = new ArrayList<>();

		Assessment a = new Assessment();
		a.setId(1l);
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
		a.setType(Assessment.Type.assignment);
		a.setTitle("First Test");
		a.getStatus().setPublished(true);
		rv.add(a);

		a = new Assessment();
		a.setId(2l);
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
		a.setType(Assessment.Type.assignment);
		a.setTitle("Second Test");
		a.getStatus().setPublished(false);
		rv.add(a);

		a = new Assessment();
		a.setId(3l);
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
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

		clean(updated);

		Optional<Assessment> rv = data.updateAssessment(current, updated);
		return rv;
	}

	/**
	 * Enforce any rules about values by forcing the values.
	 * 
	 * @param asmt
	 *            The assessment to clean
	 */
	protected void clean(Assessment asmt) {

		// title: must be 0 .. 255 characters if defined
		if (asmt.getTitle() != null) {
			String title = asmt.getTitle();
			title = title.trim();
			if (title.length() > 255) {
				title = title.substring(0, 255);
			} else if (title.isEmpty()) {
				title = null;
			}
			asmt.setTitle(title);
		}

		// TODO:
	}
}
