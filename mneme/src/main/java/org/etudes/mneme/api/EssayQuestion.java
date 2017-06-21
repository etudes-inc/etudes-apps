/**********************************************************************************
 *
 * Copyright (c) 2011, 2017 Etudes, Inc.
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

package org.etudes.mneme.api;

/**
 * EssayQuestion handles questions for the essay question type
 */
public interface EssayQuestion extends Question {
	/** An enumerate type that declares the types of submissions */
	public enum EssaySubmissionType {
		attachments, both, inline, none;
	}

	/**
	 * @return the modelAnswer (rich text)
	 */
	public String getModelAnswer();

	/**
	 * @return the essay submission type
	 */
	public EssaySubmissionType getSubmissionType();

	/**
	 * Set the model answer.
	 * 
	 * @param modelAnswer
	 *            The model answer. Must be well formed HTML or plain text.
	 */
	public void setModelAnswer(String modelAnswer);

	/**
	 * Set the essay submission type
	 * 
	 * @param setting
	 *            The essay submission type.
	 */
	public void setSubmissionType(EssaySubmissionType setting);
}
