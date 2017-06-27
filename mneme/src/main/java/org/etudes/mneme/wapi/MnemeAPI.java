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

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.UserService;
import org.etudes.mneme.AssessmentService;
import org.etudes.mneme.model.Assessment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/mneme/")
public class MnemeAPI {

	final static private Logger logger = LoggerFactory.getLogger(MnemeAPI.class);

	final AssessmentService asmtService;
	final AuthenticationService authService;
	final UserService userService;

	@Inject
	public MnemeAPI(AssessmentService asmtService, AuthenticationService authService, UserService userService) {
		this.asmtService = asmtService;
		this.authService = authService;
		this.userService = userService;

		logger.info("Test");
	}

	/**
	 */
	@GET
	@Path("/assessments/{id : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Assessment getAssessmentById(@PathParam("id") long id, //
			@CookieParam(AuthenticationService.TOKEN) Long authenticationToken, @QueryParam(AuthenticationService.TOKEN_ALT) Long token, //
			@Context HttpServletRequest req) {

		// get the current authentication
		Optional<Authentication> authentication = authService.authenticateByToken(authenticationToken, token, req);
		if (!authentication.isPresent()) {

			// TODO: allowing through for testing
			// return null;
			authentication = Optional.of(new Authentication().setContext("TEST").setUser(userService.getUser(1l).get()));
		}

		// get the assessment
		Optional<Assessment> rv = asmtService.getAssessment(id);
		if (!rv.isPresent()) {
			return null;
		}

		// verify it belongs to the subscription and context
		final long subscriptionId = authentication.get().getUser().getSubscriptionId();
		final String context = authentication.get().getContext();
		if ((rv.get().getSubscription() != subscriptionId) || (!rv.get().getContext().equals(context))) {
			return null;
		}

		return rv.get();
	}

	/**
	 */
	@GET
	@Path("/assessments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Assessment> getAssessments( //
			@CookieParam(AuthenticationService.TOKEN) Long authenticationToken, @QueryParam(AuthenticationService.TOKEN_ALT) Long token, //
			@Context HttpServletRequest req) {

		// TODO: remove this fake delay!
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the current authentication
		Optional<Authentication> authentication = authService.authenticateByToken(authenticationToken, token, req);
		if (!authentication.isPresent()) {

			// TODO: allowing through for testing
			// return null;
			authentication = Optional.of(new Authentication().setContext("TEST").setUser(userService.getUser(1l).get()));
		}

		// get the assessments
		final long subscriptionId = authentication.get().getUser().getSubscriptionId();
		final String context = authentication.get().getContext();
		final boolean publishedOnly = false;
		final AssessmentService.Sort sort = AssessmentService.Sort.title_a;

		List<Assessment> rv = asmtService.getContextAssessments(subscriptionId, context, sort, publishedOnly);

		return rv;
	}

	/**
	 */
	@PUT
	@Path("/assessments/{id : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Assessment saveAssessment(@PathParam("id") long id, //
			@CookieParam(AuthenticationService.TOKEN) Long authenticationToken, @QueryParam(AuthenticationService.TOKEN_ALT) Long token, //
			@Context HttpServletRequest req, //
			Assessment updated) {

		// get the current authentication
		Optional<Authentication> authentication = authService.authenticateByToken(authenticationToken, token, req);
		if (!authentication.isPresent()) {

			// TODO: allowing through for testing
			// return null;
			authentication = Optional.of(new Authentication().setContext("TEST").setUser(userService.getUser(1l).get()));
		}

		final long subscriptionId = authentication.get().getUser().getSubscriptionId();
		final String context = authentication.get().getContext();
		final long userId = authentication.get().getUser().get_id();

		Optional<Assessment> rv = Optional.empty();

		if (id == 0) {
			rv = asmtService.createAssessment(subscriptionId, context, userId, updated);
		} else {
			Optional<Assessment> current = asmtService.getAssessment(id);
			if (!current.isPresent()) {
				return null;
			}

			// verify it belongs to the subscription and context
			if ((current.get().getSubscription() != subscriptionId) || (!current.get().getContext().equals(context))) {
				return null;
			}

			rv = asmtService.saveAssessment(userId, current.get(), updated);
		}

		return rv.orElse(null);
	}
}
