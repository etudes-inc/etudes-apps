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

package org.etudes.apps.dispatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.lti.Launch;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/lti/")
public class LTI {
	final static private Logger logger = LoggerFactory.getLogger(LTI.class);

	final String ADMIN_TOKEN_ALT = AuthenticationService.ADMIN_TOKEN;
	final String TOKEN = AuthenticationService.TOKEN;
	final String TOKEN_ALT = AuthenticationService.TOKEN_ALT;

	protected AuthenticationService authenticationService;
	protected UserService userService;

	@Inject
	public LTI(AuthenticationService auth, UserService users) {
		authenticationService = auth;
		userService = users;

		logger.info("LTI");
	}

	/**
	 * Respond to an LTI provider (i.e. us) launch from an LTI consumer (i.e. Sakai / Canvas).
	 *
	 * @param postBody
	 *            The full post body.
	 * @param oauth_consumer_key
	 *            Our subscription id.
	 * @param custom_canvas_course_id
	 *            The course, or ...
	 * @param custom_canvas_account_id
	 *            The account, from which the request is coming.
	 * @param custom_canvas_user_id
	 *            The canvas user id.
	 * @param roles
	 *            The canvas user's role in the course / account.
	 * @param custom_canvas_api_domain
	 *            The canvas domain.
	 * @param authenticationToken
	 *            The (possibly) current authentication token (Note: currently ignored)
	 * @param userAgent
	 *            The user's browser agent.
	 * @param req
	 *            The request.
	 * @return A Response with proper redirect or error code.
	 */
	@POST
	@Path("/launch/{product : \\d+}")
	public Response postLaunch(String postBody, //
			@PathParam("product") Integer product, //
			// @FormParam("oauth_version") String oauth_version, //
			@FormParam("oauth_consumer_key") String oauth_consumer_key, //
			// @FormParam("oauth_timestamp") String oauth_timestamp, //
			// @FormParam("oauth_nonce") String oauth_nonce, //
			// @FormParam("oauth_signature_method") String oauth_signature_method, //
			// @FormParam("oauth_signature") String oauth_signature, //
			@FormParam("custom_canvas_course_id") String custom_canvas_course_id, //
			@FormParam("custom_canvas_account_id") String custom_canvas_account_id, //
			// @FormParam("lis_course_offering_sourcedid") String lis_course_offering_sourcedid, //
			// @FormParam("context_label") String context_label, //
			// @FormParam("context_title") String context_title, //
			@FormParam("custom_canvas_user_id") String custom_canvas_user_id, //
			// @FormParam("lis_person_name_full") String lis_person_name_full, //
			@FormParam("custom_etudes_masquerading_user_id") String custom_etudes_masquerading_user_id, //
			@FormParam("lis_person_name_family") String lis_person_name_family, //
			@FormParam("lis_person_name_given") String lis_person_name_given, //
			@FormParam("lis_person_contact_email_primary") String lis_person_contact_email_primary, //
			@FormParam("lis_person_sourcedid") String lis_person_sourcedid, //
			// @FormParam("user_image") String user_image, //
			// @FormParam("custom_canvas_user_login_id") String custom_canvas_user_login_id, //
			// @FormParam("custom_canvas_enrollment_state") String custom_canvas_enrollment_state, //
			@FormParam("roles") String roles, //
			// @FormParam("ext_roles") String ext_roles, //
			@FormParam("custom_canvas_api_domain") String custom_canvas_api_domain, //
			@FormParam("context_id") String context_id, //
			@FormParam("resource_link_id") String resource_link_id, //
			@FormParam("user_id") String user_id, //
			@CookieParam(TOKEN) Long authenticationToken, @HeaderParam("user-agent") String userAgent, //
			@Context HttpServletRequest req) {

		String[] contexts = { context_id, custom_canvas_account_id, custom_canvas_course_id };
		String[] userIds = { user_id, custom_canvas_user_id };
		LTIRole role = LTIRole.fromLaunch(roles);

		// deal with bots
		if (isFromBot(userAgent)) {
			logInfo("LTI Launch from suspected BOT", product, oauth_consumer_key, String.join(":", contexts), resource_link_id, req.getRemoteAddr(), userAgent,
					String.join(":", userIds), roles, Optional.empty());
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// deal with invalid or missing launch data
		if (isMissingValidData(contexts, userIds, role, oauth_consumer_key, product)) {
			logInfo("Incomplete LTI Launch", product, oauth_consumer_key, String.join(":", contexts), resource_link_id, req.getRemoteAddr(), userAgent,
					String.join(":", userIds), roles, Optional.empty());
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// get the subscription TODO:
		final Long subscriptionId = Long.valueOf(oauth_consumer_key);
		final String key = "12345";
		final String secret = "SECRET";

		// deal with launch context / product not matching subscription TODO:

		// deal with bad LTI launch
		Optional<String> launchError = new Launch().validSignature(req, key, secret, postBody);
		if (launchError.isPresent()) {
			logInfo("LTI Launch from suspected BOT", product, oauth_consumer_key, String.join(":", contexts), resource_link_id, req.getRemoteAddr(), userAgent,
					String.join(":", userIds), roles, Optional.of(postBody));
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// all is well!

		// get the user
		Optional<User> user = userService.getUser(subscriptionId, first(userIds));
		if (!user.isPresent()) {
			logInfo("Failed to get or make user", product, oauth_consumer_key, String.join(":", contexts), resource_link_id, req.getRemoteAddr(), userAgent,
					String.join(":", userIds), roles, Optional.empty());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		// update user info
		user.get().setEmail(lis_person_contact_email_primary);
		user.get().setFamilyName(lis_person_name_family);
		user.get().setGivenName(lis_person_name_given);
		user.get().setSisId(lis_person_sourcedid);
		userService.saveUser(user.get()); // TODO: if changed

		// record the authentication
		// user, ip, agent, effective context, role, placement
		// expand with placement
		Optional<Authentication> auth = authenticationService.authenticateUser(user.get(), req.getRemoteAddr(), userAgent, first(contexts), role.toString(),
				resource_link_id);
		if (!auth.isPresent()) {
			logInfo("Failed to get new authentication", product, oauth_consumer_key, String.join(":", contexts), resource_link_id, req.getRemoteAddr(),
					userAgent, String.join(":", userIds), roles, Optional.empty());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		// the root URL for the product
		String productUrl = "/";

		// select a starting route for the user role
		String route = "Asmts";

		// use the authentication id as the token
		Long token = auth.get().get_id();

		// TODO: from config instead of from req?
		return redirect(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + productUrl + route, Optional.of(token));
	}

	/**
	 * Check an array of strings for any non-null value.
	 * 
	 * @param values
	 * @return
	 */
	protected boolean allNull(String[] values) {
		Optional<String> found = Arrays.stream(values).filter(s -> s != null).findFirst();
		return !found.isPresent();
	}

	/**
	 * Pick the first non-null value from the array.
	 * 
	 * @param values
	 * @return
	 */

	protected String first(String[] values) {
		Optional<String> found = Arrays.stream(values).filter(s -> s != null).findFirst();
		return found.orElse("");
	}

	/**
	 * Identify known bots from the user agent of the request.
	 * 
	 * @param userAgent
	 *            The request's user agent.
	 * @return true if from a known bot, false otherwise. see: https://www.keycdn.com/blog/web-crawlers/
	 */
	protected boolean isFromBot(String userAgent) {
		return ((userAgent != null) && ((userAgent.indexOf("Googlebot") != -1) //
				|| (userAgent.indexOf("Mediapartners-Google") != -1) //
				|| (userAgent.indexOf("AdsBot-Google") != -1) //
				|| (userAgent.indexOf("Bingbot") != -1) //
				|| (userAgent.indexOf("Yahoo! Slurp") != -1) //
				|| (userAgent.indexOf("DuckDuckBot") != -1) //
				|| (userAgent.indexOf("Baiduspider") != -1) //
				|| (userAgent.indexOf("YandexBot") != -1) //
				|| (userAgent.indexOf("Sogou") != -1) //
				|| (userAgent.indexOf("Exabot") != -1) //
				|| (userAgent.indexOf("ia_archiver") != -1) //
		));
	}

	protected boolean isMissingValidData(String[] context, String[] user, LTIRole role, String subscriptionId, Integer product) {

		// check for a context
		if (allNull(context)) {
			return true;
		}

		// check for a user
		if (allNull(user)) {
			return true;
		}

		// check for roles
		if (role == LTIRole.none) {
			return true;
		}

		// check the subscription
		if ((subscriptionId == null) /* TODO: || check this id with the subscription service, is it missing? */) {
			return true;
		}

		// subscription must be a number
		try {
			Long.valueOf(subscriptionId);
		} catch (NumberFormatException e) {
			return true;
		}

		// check product
		if (product == null) {
			return true;
		}

		return false;
	}

	/**
	 * Log request info.
	 * 
	 * @param msg
	 *            Why we are logging.
	 * @param product
	 *            The product code.
	 * @param key
	 *            The LTI launch key (subscription id).
	 * @param placement
	 *            The launch placement.
	 * @param context
	 *            The launch context.
	 * @param ip
	 *            The request remote address (IP).
	 * @param userAgent
	 *            The request user agent.
	 * @param user
	 *            The identified user.
	 * @param role
	 *            (Optional) The user's role in context.
	 * @param extra
	 *            Some extra data to
	 */
	protected void logInfo(String msg, Integer product, String key, String context, String placement, String ip, String userAgent, String user, String role,
			Optional<String> extra) {
		logger.info(msg + "  product: " + product + "  key: " + key + "  context: " + context + "  placement: " + placement + "  IP: " + ip + "  agent: "
				+ userAgent + "  user: " + user + "  role: " + role + (extra.isPresent() ? (" + " + extra.get()) : ""));
	}

	/**
	 * Process a redirect, with optional cookie setting
	 *
	 * @param redirect
	 *            The redirect URL
	 * @param tokenToSet
	 *            The optional Token cookie value to set.
	 * @return The Response.
	 */
	protected Response redirect(String redirect, Optional<Long> tokenToSet) {
		return redirect(redirect, tokenToSet, TOKEN, TOKEN_ALT, Optional.empty());
	}

	/**
	 * Process a redirect, with optional cookie setting
	 *
	 * @param redirect
	 *            The redirect URL
	 * @param tokenToSet
	 *            The optional Token cookie value to set.
	 * @param tokenName
	 *            The name of the token.
	 * @return The Response.
	 */
	protected Response redirect(String redirect, Optional<Long> tokenToSet, String tokenName, String tokenAlt, Optional<Long> adminToken) {
		try {
			// ? or &
			String first = (redirect.indexOf("?") == -1) ? "?" : "&";

			String redir = redirect;
			if (tokenToSet.isPresent()) {
				redir += first + tokenAlt + "=" + tokenToSet.get();
				if (adminToken.isPresent()) {
					redir += "&" + ADMIN_TOKEN_ALT + "=" + adminToken.get();
				}
			}

			// send a redirect, set the session cookie if needed
			URI targetURIForRedirection = new URI(redir);

			if (tokenToSet.isPresent()) {
				return Response.seeOther(targetURIForRedirection).cookie(new NewCookie(tokenName, Long.toString(tokenToSet.get()), "/", null, "", -1, false))
						.build();
			} else {
				return Response.seeOther(targetURIForRedirection).build();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return Response.serverError().build();
	}
}
