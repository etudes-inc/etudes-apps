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

package org.etudes.apps.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.assertj.core.api.Assertions;
import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.data.AuthenticationData;
import org.etudes.apps.authentication.impl.AuthenticationServiceImpl;
import org.etudes.apps.authentication.model.Authentication;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test the AuthenticationService impl
 * 
 * Developer Note: do NOT use Eclipse's "source > sort members" feature, as it messes up the order of mock objects
 */
public class AuthenticationServiceTest {

	class Req implements HttpServletRequest {

		protected String ip;

		public Req(String ip) {
			this.ip = ip;
		}

		@Override
		public Object getAttribute(String name) {
			return null;
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return null;
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

		}

		@Override
		public int getContentLength() {
			return 0;
		}

		@Override
		public long getContentLengthLong() {
			return 0;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public String getParameter(String name) {
			return null;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			return null;
		}

		@Override
		public String[] getParameterValues(String name) {
			return null;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return null;
		}

		@Override
		public String getProtocol() {
			return null;
		}

		@Override
		public String getScheme() {
			return null;
		}

		@Override
		public String getServerName() {
			return null;
		}

		@Override
		public int getServerPort() {
			return 0;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return null;
		}

		@Override
		public String getRemoteAddr() {
			return ip;
		}

		@Override
		public String getRemoteHost() {
			return null;
		}

		@Override
		public void setAttribute(String name, Object o) {

		}

		@Override
		public void removeAttribute(String name) {

		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public Enumeration<Locale> getLocales() {
			return null;
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String path) {
			return null;
		}

		@Override
		public String getRealPath(String path) {
			return null;
		}

		@Override
		public int getRemotePort() {
			return 0;
		}

		@Override
		public String getLocalName() {
			return null;
		}

		@Override
		public String getLocalAddr() {
			return null;
		}

		@Override
		public int getLocalPort() {
			return 0;
		}

		@Override
		public ServletContext getServletContext() {
			return null;
		}

		@Override
		public AsyncContext startAsync() throws IllegalStateException {
			return null;
		}

		@Override
		public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
			return null;
		}

		@Override
		public boolean isAsyncStarted() {
			return false;
		}

		@Override
		public boolean isAsyncSupported() {
			return false;
		}

		@Override
		public AsyncContext getAsyncContext() {
			return null;
		}

		@Override
		public DispatcherType getDispatcherType() {
			return null;
		}

		@Override
		public String getAuthType() {
			return null;
		}

		@Override
		public Cookie[] getCookies() {
			return null;
		}

		@Override
		public long getDateHeader(String name) {
			return 0;
		}

		@Override
		public String getHeader(String name) {
			return null;
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			return null;
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			return null;
		}

		@Override
		public int getIntHeader(String name) {
			return 0;
		}

		@Override
		public String getMethod() {
			return null;
		}

		@Override
		public String getPathInfo() {
			return null;
		}

		@Override
		public String getPathTranslated() {
			return null;
		}

		@Override
		public String getContextPath() {
			return null;
		}

		@Override
		public String getQueryString() {
			return null;
		}

		@Override
		public String getRemoteUser() {
			return null;
		}

		@Override
		public boolean isUserInRole(String role) {
			return false;
		}

		@Override
		public Principal getUserPrincipal() {
			return null;
		}

		@Override
		public String getRequestedSessionId() {
			return null;
		}

		@Override
		public String getRequestURI() {
			return null;
		}

		@Override
		public StringBuffer getRequestURL() {
			return null;
		}

		@Override
		public String getServletPath() {
			return null;
		}

		@Override
		public HttpSession getSession(boolean create) {
			return null;
		}

		@Override
		public HttpSession getSession() {
			return null;
		}

		@Override
		public String changeSessionId() {
			return null;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromUrl() {
			return false;
		}

		@Override
		public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
			return false;
		}

		@Override
		public void login(String username, String password) throws ServletException {

		}

		@Override
		public void logout() throws ServletException {

		}

		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			return null;
		}

		@Override
		public Part getPart(String name) throws IOException, ServletException {
			return null;
		}

		@Override
		public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
			return null;
		}

	}

	protected static DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	protected static Date parse(String value) {
		try {
			return fmt.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	// mock the data access
	private static final AuthenticationData authenticationData = Mockito.mock(AuthenticationData.class);

	private static final UserService userService = Mockito.mock(UserService.class);

	// create the service to test
	private static final AuthenticationService authenticationService = new AuthenticationServiceImpl(authenticationData, userService);

	private static Authentication AUTH_1 = new Authentication(1l, 101L, "context", parse("2016-03-10T18:30:00.000-0000"), "127.0.0.1", "placement", "role",
			new User(1l, "email", "family", "given", "lms-1", false, "sis", 2L, false));
	private static Authentication AUTH_2 = new Authentication(2l, 101L, "context", parse("2016-03-10T18:30:00.000-0000"), "127.0.0.1", "placement", "role",
			new User(1l, "email", "family", "given", "lms-1", false, "sis", 2L, false));
	private static Authentication AUTH_3 = new Authentication(3l, 101L, "context", parse("2016-03-10T18:30:00.000-0000"), "127.0.0.1", "placement", "role",
			new User(1l, "email", "family", "given", "lms-1", false, "sis", 2L, false));

	private static User USER = new User(104l, "email", "family", "given", "lms-4", false, "sis", 200L, false);

	/**
	 * Before each test, setup the mock services to return the mock models, if asked for nicely ;-)
	 */
	@Before
	public void setup() {

		Mockito.when(authenticationData.readAuthentication(1L)).thenReturn(Optional.of(AUTH_1));
		Mockito.when(authenticationData.readAuthentication(2L)).thenReturn(Optional.of(AUTH_2));
		Mockito.when(authenticationData.readAuthentication(3L)).thenReturn(Optional.of(AUTH_3));
		Mockito.when(authenticationData.readAuthentication(0L)).thenReturn(Optional.empty());

		Mockito.when(authenticationData.createAuthentication(Mockito.eq(USER), Mockito.eq("127.0.0.1"), Mockito.eq(102L), Mockito.eq("ctx"), Mockito.eq("pl"),
				Mockito.eq("rl"), Mockito.any(Date.class) /* parse("2016-03-11T18:30:00.000-0000") */))
				.thenReturn(Optional.of(new Authentication(4l, 102L, "ctx", parse("2016-03-10T18:30:00.000-0000"), "127.0.0.1", "pl", "rl", USER)));

		Mockito.when(authenticationData.createBrowser("junit/test")).thenReturn(Optional.of(102L));
		Mockito.when(authenticationData.readBrowser(102L)).thenReturn(Optional.of("junit/test"));
	}

	/**
	 * Cleanup after the test, resetting the mocked services.
	 */
	@After
	public void tearDown() {

		Mockito.reset(authenticationData);
	}

	/**
	 * Test the /profile path to get the authentication's profile
	 */
	@Test
	public void testFetch() {

		HttpServletRequest req_1 = new Req("127.0.0.1");
		// HttpServletRequest req_2 = new Req("127.0.0.2");
		HttpServletRequest req_0 = new Req("0.0.0.0");

		// verify we can find our authentications
		Assertions.assertThat(authenticationService.authenticateByToken(1L, null, req_1)).isEqualTo(Optional.of(AUTH_1));
		Assertions.assertThat(authenticationService.authenticateByToken(2L, null, req_1)).isEqualTo(Optional.of(AUTH_2));
		Assertions.assertThat(authenticationService.authenticateByToken(null, 3L, req_1)).isEqualTo(Optional.of(AUTH_3));

		// verify that the IP address is being checked - feature removed for EVAL-333
		// Assertions.assertThat(authenticationService.authenticateByToken(3L, null, req_2)).isEqualTo(Optional.empty());

		// verify that we don't find an undefined authentication
		Assertions.assertThat(authenticationService.authenticateByToken(0L, null, req_0)).isEqualTo(Optional.empty());

		// create and check the fields of a new LTI authentication
		Optional<Authentication> auth = authenticationService.authenticateUser(USER, "127.0.0.1", "junit/test", "ctx", "pl", "rl");
		Assertions.assertThat(auth.isPresent()).isTrue();
		Assertions.assertThat(auth.get().getUser()).isEqualTo(USER);
		Assertions.assertThat(auth.get().getIpAddress()).isEqualTo("127.0.0.1");
		Assertions.assertThat(auth.get().getBrowser()).isEqualTo(102L);
		Assertions.assertThat(auth.get().getContext()).isEqualTo("ctx");
		Assertions.assertThat(auth.get().getRole()).isEqualTo("rl");
		Assertions.assertThat(auth.get().get_id()).isEqualTo(4L);
	}
}
