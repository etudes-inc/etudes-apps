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

package org.etudes.apps.authentication.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.data.AuthenticationData;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.model.User;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	final static private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	/** The max size for the user-agent database field, in characters. */
	protected static int MAX_USER_AGENT = 255;

	protected static byte[] noAddress = { 0, 0, 0, 0 };

	/** Data handler. */
	protected AuthenticationData data;

	protected UserService userService;

	/**
	 * Create the authentication service
	 * 
	 * @param config
	 *            The configuration
	 */
	@Inject
	public AuthenticationServiceImpl(AuthenticationData data, UserService userService) {
		this.data = data;
		this.userService = userService;
		logger.info("AuthenticationService");
	}

	@Override
	public Optional<Authentication> authenticateByToken(Long token, Long alternate, HttpServletRequest req) {

		// allow the token to be missing, use the alternate
		if (token == null)
			token = alternate;

		if (token == null) {
			logger.info("auth: no token: path: " + req.getPathInfo() + " ip: " + remoteAddr(req));
			return Optional.empty();
		}

		// find this authentication
		Optional<Authentication> rv = data.readAuthentication(token);
		if (!rv.isPresent()) {
			logger.info("auth: token invalid: " + token + " path: " + req.getPathInfo() + " ip: " + remoteAddr(req));
			return Optional.empty();
		}

		return rv;
	}

	@Override
	public Optional<Authentication> authenticateUser(User u, String ipAddress, String browserUserAgent, String context, String role, String placement) {

		// find the browser
		Optional<Long> browser = getBroswerUserAgent(browserUserAgent);
		if (!browser.isPresent())
			return Optional.empty();

		// create new authentication
		Optional<Authentication> rv = data.createAuthentication(u, ipAddress, browser.get(), context, role, placement, new Date());

		return rv;
	}

	@Override
	public List<Authentication> getAuthentications(Long subscriptionId) {

		return data.readAuthentications(subscriptionId);
	}

	@Override
	public Optional<String> getBroswerUserAgent(Long id) {

		Optional<String> rv = data.readBrowser(id);

		return rv;
	}

	@Override
	public Optional<Long> getBroswerUserAgent(String browser) {

		return data.createBrowser(browser);
	}

	@Override
	public void removeSubscriptionAuthentications(Long subscriptionId) {
		data.deleteAuthentications(subscriptionId);
	}

	/**
	 * Convert an IP address string into a numeric IP address
	 * 
	 * @param ipAddress
	 *            The IP address string.
	 * @return The numeric representation of the IP address as 16 (for IPv6) byte array - for IPv4, the address is in the first 4 bytes, the remainder are 0.
	 */
	protected byte[] parseIpAddress(String ipAddress) {
		byte[] rv = null;

		try {
			InetAddress addr = InetAddress.getByName(ipAddress);

			rv = addr.getAddress();
			if (rv.length == 4) {
				byte[] longrv = new byte[16];
				System.arraycopy(rv, 0, longrv, 0, 4);
				rv = longrv;
			}
		} catch (UnknownHostException e) {
			logger.warn("parseIpAddress: " + ipAddress + " : " + e.toString());
			rv = noAddress;
		}

		return rv;
	}

	/**
	 * Testing may not provide a request object... safe way to get the remote IP
	 * 
	 * @param req
	 *            The request.
	 * @return The remote IP, or null.
	 */
	protected String remoteAddr(HttpServletRequest req) {
		if (req == null)
			return null;
		return req.getRemoteAddr();
	}
}
