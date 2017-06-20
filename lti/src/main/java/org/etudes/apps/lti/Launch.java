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

package org.etudes.apps.lti;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

public class Launch {
	// final static private Logger logger = LoggerFactory.getLogger(Launch.class);

	/**
	 * Validate the signature. Return empty if GOOD, otherwise an error message about why it was bad.
	 * 
	 * @param req
	 * @param key
	 * @param secret
	 * @param postBody
	 * @return empty if GOOD, otherwise an error message about why it was bad.
	 */
	public Optional<String> validSignature(HttpServletRequest req, String key, String secret, String postBody) {

		String URL = req.getRequestURL().toString();

		OAuthMessage oam = OAuthServlet.getMessage(req, URL);

		// put all the body 'form fields' into the parameters
		oam.addParameters(loadBody(postBody));

		OAuthValidator oav = new SimpleOAuthValidator();
		OAuthConsumer cons = new OAuthConsumer("about:blank#OAuth+CallBack+NotUsed", key, secret, null);

		OAuthAccessor acc = new OAuthAccessor(cons);

		try {
			oav.validateMessage(oam, acc);
			return Optional.empty();
		} catch (Exception e) {
			// logger.info("validSignature: " + e.getMessage() + " " + ((OAuthProblemException) e).getParameters());
			return Optional.of(e.getMessage() + " " + ((OAuthProblemException) e).getParameters());
		}
	}

	protected List<Map.Entry<String, String>> loadBody(String postBody) {
		final List<Map.Entry<String, String>> parameters = new ArrayList<>();
		if (postBody == null)
			return parameters;

		String[] fields = postBody.split("&");
		Arrays.stream(fields).forEach(f -> {

			// only split on the first = found - some values may have an = character inside
			String[] nameValue = f.split("=", 2);

			// only if we have a full name=value
			if (nameValue.length > 1) {

				// decode the value
				String value = nameValue[1];
				try {
					value = URLDecoder.decode(value, OAuth.ENCODING);
				} catch (java.io.UnsupportedEncodingException e) {
				}

				parameters.add(new OAuth.Parameter(nameValue[0], value));
			}
		});

		return parameters;
	}
}
