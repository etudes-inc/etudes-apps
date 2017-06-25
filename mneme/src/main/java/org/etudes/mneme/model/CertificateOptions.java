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

/**
 * CertificateOptions contains the details of issuing certificates based on submissions to the assessment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateOptions {

	/** The score threshold needed to pass to issue a certificate. */
	protected float certificateScoreThreshold = 0;

	/** If the assessment issues a certificate if the certificateScoreThreshold is met by a submission. */
	protected boolean issuingCertificates = false;

	/**
	 * Set from another.
	 * 
	 * @param other
	 *            The other.
	 * @return this (for chaining).
	 */
	public CertificateOptions set(CertificateOptions other) {
		this.certificateScoreThreshold = other.certificateScoreThreshold;
		this.issuingCertificates = other.issuingCertificates;

		return this;
	}
}
