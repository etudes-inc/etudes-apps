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

package org.etudes.mneme;

import java.util.List;
import java.util.Optional;

import org.etudes.mneme.model.Assessment;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AssessmentService {

	/**
	 * Sort options for getContextAssessments()
	 */
	enum Sort {
		cdate_a, cdate_d, ddate_a, ddate_d, odate_a, odate_d, published_a, published_d, title_a, title_d, type_a, type_d
	}

	/**
	 * Create an assessment
	 * 
	 * @param subscriptionId
	 *            The subscription to own the assessment.
	 * @param context
	 *            The context to associate with the assessment.
	 * @param userId
	 *            The creator of the assessment.
	 * @param asmt
	 *            The assessment to create (minus id).
	 * @return The created assessment, or not if the create failed.
	 */
	Optional<Assessment> createAssessment(long subscriptionId, String context, long userId, Assessment asmt);

	/**
	 * @param context
	 *            The context.
	 * @param sort
	 *            The sort specification.
	 * @param publishedOnly
	 *            if only published & valid assessments should be returned, else return unpublished and invalid, too.
	 * @return All the assessments for the context, sorted. Does not include archived assessments. Unpublished & invalid are optionally included.
	 */
	List<Assessment> getContextAssessments(String context, Sort sort, Boolean publishedOnly);
}
