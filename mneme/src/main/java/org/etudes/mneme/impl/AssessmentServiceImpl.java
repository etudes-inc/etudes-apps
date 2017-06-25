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

import javax.inject.Inject;

import org.etudes.mneme.AssessmentService;
import org.etudes.mneme.model.Assessment;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssessmentServiceImpl implements AssessmentService {
	final static private Logger logger = LoggerFactory.getLogger(AssessmentServiceImpl.class);

	/**
	 * Create the assessment service
	 */
	@Inject
	public AssessmentServiceImpl() {
		logger.info("AuthenticationService");
	}

	@Override
	public List<Assessment> getContextAssessments(String context, Sort sort, Boolean publishedOnly) {
		// TODO Auto-generated method stub

		List<Assessment> rv = new ArrayList<>();

		Assessment a = new Assessment();
		a.setId("a1");
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
		a.setType(Assessment.Type.assignment);
		a.setTitle("First Test");
		a.getStatus().setPublished(true);
		rv.add(a);

		a = new Assessment();
		a.setId("a2");
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
		a.setType(Assessment.Type.assignment);
		a.setTitle("Second Test");
		a.getStatus().setPublished(false);
		rv.add(a);

		a = new Assessment();
		a.setId("a3");
		a.getSchedule().setOpen(new Date());
		a.getSchedule().setDue(new Date());
		a.getSchedule().setUntil(new Date());
		a.setType(Assessment.Type.assignment);
		a.setTitle("Third Test");
		a.getStatus().setPublished(true);
		rv.add(a);

		return rv;
	}
}
