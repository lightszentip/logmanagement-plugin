package com.lightszentip.module.logmanagement.plugin;

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

import java.util.Arrays;
import java.util.List;

import com.lightszentip.module.logmanagement.plugin.log4j.LogManagementPluginLog4jImpl;
import com.lightszentip.module.logmanagement.plugin.logback.LogManagementPluginLogbackImpl;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

/**
 * Factory to get the plugin with logger implementation.
 *
 */
public class LogManagementPluginFactory {

	/**
	 * Get implementation for log management plugin.
	 * @return The implementation of log management plugin.
	 */
	public static LogManagementPlugin getLogManagementPlugin() {
		final String loggerImpl = org.slf4j.impl.StaticLoggerBinder
				.getSingleton().getLoggerFactoryClassStr();
		if ("Log4jLoggerFactory﻿".equals(loggerImpl.substring(loggerImpl
						.lastIndexOf(".") + 1))) {
			return new LogManagementPluginLog4jImpl();
		} else if (loggerImpl.indexOf("logback.") > 1
				&& "ContextSelectorStaticBinder".equals(loggerImpl
						.substring(loggerImpl.lastIndexOf(".") + 1))) {
			return new LogManagementPluginLogbackImpl();
		}
		throw new UnsupportedOperationException();
	}

    /**
     * All log levels for the logger.
     * @return A list of all log levels which can set to logger.
     */
    public static List<LogLevel> getAllLogLevels() {
		return Arrays.asList(LogLevel.values());
    }

}
