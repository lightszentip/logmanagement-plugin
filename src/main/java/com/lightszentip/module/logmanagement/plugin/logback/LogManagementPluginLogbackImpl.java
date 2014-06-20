package com.lightszentip.module.logmanagement.plugin.logback;

/*
 * --start-license-header--
 * Project: logmanagement-plugin
 * ---------
 * Copyright (C) 2014 Tobias Gafner
 * ---------
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
 * --end-license-header--
 */

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.lightszentip.module.logmanagement.plugin.LogManagementPlugin;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

/**
 * The plugin implementation for logback.
 *
 */
public class LogManagementPluginLogbackImpl implements LogManagementPlugin {

	public LogManagementPluginLogbackImpl() {
	}

	@Override
	public void changeRootLogLevel(final LogLevel logLevel) {
		final LoggerContext lc = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		lc.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(this.mapLogLevel(logLevel));
	}

	@Override
	public void setLogLevelForLogger(final String logger,
			final LogLevel logLevel) {
		final LoggerContext lc = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		lc.getLogger(logger).setLevel(this.mapLogLevel(logLevel));
	}

	@Override
	public Map<String, String> getLoggerWithLogLevel() {
		return this.getLogger(false);
	}

	/**
	 * Map the plugin log level to logback log level.
	 * 
	 * @param logLevel
	 *            The plugin log level.
	 * @return The logback log level.
	 */
	private Level mapLogLevel(final LogLevel logLevel) {
		if (LogLevel.DEBUG.equals(logLevel)) {
			return Level.DEBUG;
		} else if (LogLevel.TRACE.equals(logLevel)) {
			return Level.TRACE;
		} else if (LogLevel.INFO.equals(logLevel)) {
			return Level.INFO;
		} else if (LogLevel.WARN.equals(logLevel)) {
			return Level.WARN;
		} else if (LogLevel.ERROR.equals(logLevel)) {
			return Level.ERROR;
		}
		return null;
	}

	@Override
	public Map<String, String> getAllLogger() {
		return this.getLogger(true);
	}

	private Map<String, String> getLogger(final boolean addNullLogLevelLogger) {
		final Map<String, String> map = new HashMap<>();
		final LoggerContext lc = (LoggerContext) LoggerFactory
				.getILoggerFactory();

		for (final Logger log : lc.getLoggerList()) {
			final Level logLevel = log.getLevel();
			if (logLevel != null) {
				map.put(log.getName(), logLevel.toString());
			} else if (addNullLogLevelLogger) {
				map.put(log.getName(), null);
			}
		}
		return map;
	}

	@Override
	public String getRootLogLevel() {
		final LoggerContext lc = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		final Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
		if (logger != null && logger.getLevel() != null) {
			return logger.getLevel().toString();
		}
		return null;
	}
}
