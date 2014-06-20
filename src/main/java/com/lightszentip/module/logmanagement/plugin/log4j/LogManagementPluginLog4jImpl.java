package com.lightszentip.module.logmanagement.plugin.log4j;

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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.lightszentip.module.logmanagement.plugin.LogManagementPlugin;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

/**
 * The plugin implementation for log4j logger.
 *
 */
public class LogManagementPluginLog4jImpl implements LogManagementPlugin {

	public LogManagementPluginLog4jImpl() {
	}

	@Override
	public void changeRootLogLevel(final LogLevel logLevel) {
		final Logger root = org.apache.log4j.Logger.getRootLogger();
		root.setLevel(this.mapLogLevel(logLevel));
	}

	@Override
	public void setLogLevelForLogger(final String logger,
			final LogLevel logLevel) {
		org.apache.log4j.LogManager.getLogger(logger).setLevel(
				this.mapLogLevel(logLevel));
	}

	@Override
	public Map<String, String> getLoggerWithLogLevel() {
		return this.getLogger(false);
	}

	private Map<String, String> getLogger(final boolean addNullLogLevelLogger) {
		final Map<String, String> map = new HashMap<>();
		final Enumeration<?> loggers = org.apache.log4j.LogManager
				.getCurrentLoggers();
		while (loggers.hasMoreElements()) {
			final Category tmpLogger = (Category) loggers.nextElement();
			if (tmpLogger.getLevel() != null) {
				map.put(tmpLogger.getName(), tmpLogger.getLevel().toString());
			} else if (addNullLogLevelLogger) {
				map.put(tmpLogger.getName(), null);
			}

		}
		return map;
	}

	/**
	 * Map the loglevel from plugin to log4j log level.
	 * 
	 * @param logLevel
	 *            The plugin loglevel.
	 * @return The log4j log level.
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

	@Override
	public String getRootLogLevel() {
		final Logger root = org.apache.log4j.Logger.getRootLogger();
		if (root != null && root.getLevel() != null) {
			return root.getLevel().toString();
		}
		return null;
	}

}
