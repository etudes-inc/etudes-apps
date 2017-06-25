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
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Presentation defines a rich text with attachments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presentation {

	/** The attachments for the presentation. */
	protected List<Reference> attachments = new ArrayList<Reference>();

	/** The rich text (html) part of the presentation. */
	protected String text = null;

	/**
	 * @return if the text and attachments are empty.
	 */
	public boolean isEmpty() {
		return ((text == null) && attachments.isEmpty());
	}

	/**
	 * Remove the attachment that matches this reference string.
	 * 
	 * @param reference
	 *            The attachment to remove.
	 */
	public void removeAttachment(Reference reference) {
		this.attachments.remove(reference);
	}

	/**
	 * Set the attachments to these references.
	 * 
	 * @param references
	 *            The list of attachment references.
	 */
	public void setAttachments(List<Reference> references) {
		this.attachments.clear();
		if (references != null) {
			this.attachments.addAll(references);
		}
	}

	// TODO: StringUtil.trimToNull(text) on setText()

	/**
	 * Set as a copy of the other.
	 * 
	 * @param other
	 *            The other to copy.
	 * @return this (for chaining).
	 */
	public Presentation set(Presentation other) {
		this.attachments = new ArrayList<Reference>(other.attachments.size());
		this.attachments.addAll(other.attachments);
		this.text = other.text;

		return this;
	}
}
