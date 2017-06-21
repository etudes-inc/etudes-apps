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

package org.etudes.apps.framework;

import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.etudes.apps.authentication.AuthenticationService;
import org.etudes.apps.authentication.api.AuthAPI;
import org.etudes.apps.authentication.data.AuthenticationData;
import org.etudes.apps.authentication.impl.AuthenticationDataJDBIImpl;
import org.etudes.apps.authentication.impl.AuthenticationServiceImpl;
import org.etudes.apps.db.DB;
import org.etudes.apps.dispatcher.LTI;
import org.etudes.apps.user.UserService;
import org.etudes.apps.user.data.UserData;
import org.etudes.apps.user.impl.UserDataJDBIImpl;
import org.etudes.apps.user.impl.UserServiceImpl;
import org.etudes.mneme.wapi.MnemeAPI;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Application extends io.dropwizard.Application<Configuration> {
	// protected class NotifyManager implements Managed {
	//
	// protected final String hostname;
	// protected final String version;
	//
	// public NotifyManager(String hostname, String version) {
	// this.hostname = hostname;
	// this.version = version;
	// }
	//
	// @Override
	// public void start() throws Exception {
	// logger.warn(version + " starting on " + hostname);
	// }
	//
	// @Override
	// public void stop() throws Exception {
	// logger.warn(version + " stopped on " + hostname);
	// }
	// }

	final static private Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		new Application().run(args);
	}

	@Override
	public String getName() {
		return "Etudes Apps Framework";
	}

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {

		// enable Java 8 object support in jackson json mapping
		// see: https://github.com/FasterXML/jackson-datatype-jdk8
		bootstrap.getObjectMapper().registerModule(new Jdk8Module());

		// allow values in the configuration file of the form ${ENV_VAR} and ${ENV_VAR:-default value}
		bootstrap.setConfigurationSourceProvider(
				new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

		// serve our static assets from /, serving "index.html" as the default
		// all possible route starting points needs to be listed
		bootstrap.addBundle(new AssetsBundle("/mneme/", "/Asmts", "index.html", "MnemeAsmts"));
		bootstrap.addBundle(new AssetsBundle("/mneme/", "/", "index.html", "MnemeRoot"));
	}

	@Override
	public void run(Configuration configuration, Environment environment) {
		// logger.warn(configuration.getVersion() + " starting on " + configuration.getHostname());

		logger.info("run: configuration:" + configuration);

		// environment.lifecycle().manage(new NotifyManager("host"/*configuration.getHostname()*/, "ver"/*configuration.getAuth().getVersion()*/));

		// create our db connection, and make it available for injection into services
		final DBIFactory factory = new DBIFactory();
		final DBI dbi = factory.build(environment, configuration.getDatabase(), "mysql");

		// our wrapper around the dbi
		DB db = new DB(dbi, configuration.isAutoDDL());

		// add our services, etc. to the component system
		environment.jersey().register(new AbstractBinder() {
			@Override
			protected void configure() {

				// make the configuration available
				bind(configuration).to(Configuration.class);

				// bind the DBI and DB
				bind(dbi).to(DBI.class);
				bind(db).to(DB.class);

				// make our services available for injection - as singletons

				// authentication
				bind(AuthenticationDataJDBIImpl.class).to(AuthenticationData.class).in(Singleton.class);
				bind(AuthenticationServiceImpl.class).to(AuthenticationService.class).in(Singleton.class);

				// user & data
				bind(UserDataJDBIImpl.class).to(UserData.class).in(Singleton.class);
				bind(UserServiceImpl.class).to(UserService.class).in(Singleton.class);

				// make our resources singleton
				bind(AuthAPI.class).to(AuthAPI.class).in(Singleton.class);
				bind(LTI.class).to(LTI.class).in(Singleton.class);
				bind(MnemeAPI.class).to(MnemeAPI.class).in(Singleton.class);
			}
		});

		// register a custom jetty request log factory
		// ((DefaultServerFactory) configuration.getServerFactory()).setRequestLogFactory(
		// new RequestLogJDBIFactory(db, configuration.isMock_data(), configuration.getHostname(), configuration.getAuth().getVersion()));

		// register our resources
		environment.jersey().register(AuthAPI.class);
		environment.jersey().register(LTI.class);
		environment.jersey().register(MnemeAPI.class);

		// Note: health checks are on the admin port at /healthcheck, but on AWS, the ELB does not talk to any port other than 80, so we create our own health
		// check as a resource
		// final GeneralHealthCheck healthCheck = new GeneralHealthCheck();
		// environment.healthChecks().register("general", healthCheck);

		environment.lifecycle().addServerLifecycleListener(new ServerLifecycleListener() {
			@Override
			public void serverStarted(Server server) {
				logger.info("ServerLifecycleListener.serverStarted()");

				ServiceLocator locator = ((ServletContainer) environment.getJerseyServletContainer()).getApplicationHandler().getServiceLocator();

				// start our singletons
				locator.getService(AuthenticationService.class);
				locator.getService(UserService.class);

				// start our resources
				locator.getService(AuthAPI.class);
				locator.getService(LTI.class);
				locator.getService(MnemeAPI.class);
			}
		});
	}
}
