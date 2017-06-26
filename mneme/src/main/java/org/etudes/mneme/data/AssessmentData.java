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

package org.etudes.mneme.data;

import java.util.List;
import java.util.Optional;

import org.etudes.mneme.model.Assessment;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AssessmentData {

	/**
	 * Create an assessment.
	 * 
	 * @param asmt
	 *            The new assessment.
	 * @return the created assessment, or not.
	 */
	Optional<Assessment> create(Assessment asmt);

	/**
	 * Delete the assessment with this id.
	 * 
	 * @param eval
	 *            The assessment to delete.
	 * @return success.
	 */
	boolean delete(Assessment asmt);

	/**
	 * Delete the assessments for this subscription.
	 * 
	 * @param subscriptionId
	 *            The subscription ID.
	 * @return success.
	 */
	boolean deleteAssessments(long subscriptionId);

	/**
	 * Get a assessment by id.
	 * 
	 * @param id
	 *            The assessment id.
	 * @return the assessment, or not.
	 */
	Optional<Assessment> readAssessment(long id);

	/**
	 * Get the assessments for a subscription. Ignore deleted assessments.
	 * 
	 * @param subscriptionId
	 *            The subscription ID
	 * @return the assessments for this subscription.
	 */
	List<Assessment> readAssessments(long subscriptionId);

	/**
	 * Get the assessment for a subscription in a context. Ignore deleted assessments.
	 * 
	 * @param subscriptionId
	 *            The subscription ID
	 * @param context
	 *            the context (LMS location such as course).
	 * @return the assessments for this subscription in this context.
	 */
	List<Assessment> readAssessments(long subscriptionId, String context);

	/**
	 * Update the assessment.
	 * 
	 * @param old
	 *            The current assessment.
	 * @param updated
	 *            The updated assessment.
	 * @return the updated assessment, or not if the update failed.
	 */
	Optional<Assessment> updateAssessment(Assessment old, Assessment updated);
}
