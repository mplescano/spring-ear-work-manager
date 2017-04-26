package com.mplescano.workmanager.config;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

@Configuration
@ComponentScan("com.mplescano.workmanager.service")
public class ServiceConfig {

	/**
	 * it's put here because logback cannot log for each webapp but as a overall app.
	 */
	@PostConstruct
	protected void setupLogback() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();
		// override default configuration
		// inject the name of the current application as "application-name"
		// property of the LoggerContext
		// context.putProperty("application-name",
		// getClass().getClassLoader().getClass().getName());
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("logback.xml");
			if (is == null) {

			} else {
				jc.doConfigure(is);
			}
		} catch (Exception ex) {

		}
	}

}