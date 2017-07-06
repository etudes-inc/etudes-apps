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

package org.etudes.apps.authentication.wapi;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Path("/auth/")
public class AuthAPI {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class AuthInfo {
		String givenName;
		String familyName;
		String context;
		String placement;
	}

	final static private Logger logger = LoggerFactory.getLogger(AuthAPI.class);

	protected AuthenticationService authenticationService;

	@Inject
	public AuthAPI(AuthenticationService authService) {
		authenticationService = authService;
		logger.info("AuthAPI");
	}

	/**
	 */
	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public AuthInfo getAuthInfo( //
			@CookieParam(AuthenticationService.TOKEN) Long authenticationToken, @QueryParam(AuthenticationService.TOKEN_ALT) Long token, //
			@Context HttpServletRequest req) {

		// get the current authentication
		final Optional<Authentication> authentication = authenticationService.authenticateByToken(authenticationToken, token, req);
		if (!authentication.isPresent()) {
			return null;
		}

		// get the user
		final User user = authentication.get().getUser();

		AuthInfo rv = new AuthInfo();
		rv.givenName = user.getGivenName();
		rv.familyName = user.getFamilyName();

		rv.context = authentication.get().getContext();
		rv.placement = authentication.get().getPlacement();

		return rv;
	}
}
