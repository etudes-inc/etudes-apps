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

package org.etudes.mneme.wapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Path("/mneme/")
public class MnemeAPI {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class Assessment {
		Date due;
		Long id;
		Date open;
		boolean published;
		String title;
		String type;
		Date until;
		boolean valid;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class Message {
		String message = "";
	}

	final static private Logger logger = LoggerFactory.getLogger(MnemeAPI.class);

	@Inject
	public MnemeAPI() {
		logger.info("Test");
	}

	/**
	 */
	@GET
	@Path("/assessments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Assessment> getAssessments() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Assessment> rv = new ArrayList<>();
		rv.add(new Assessment(new Date(), 1L, new Date(), true, "A", "First Test", new Date(), true));
		rv.add(new Assessment(new Date(), 2L, new Date(), true, "A", "Second Test", new Date(), false));
		rv.add(new Assessment(new Date(), 3L, new Date(), false, "A", "Third Test", new Date(), true));

		return rv;
	}

	/**
	 */
	@GET
	@Path("/message")
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage() {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Message rv = new Message();
		rv.setMessage("Hello from the sever");

		return rv;
	}
}
