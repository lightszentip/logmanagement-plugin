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

import java.util.Map;

import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

/**
 * Interface for implementation of logger.
 *
 */
public interface LogManagementPlugin {

	/**
	 * Change the log level from root logger.
	 * @param logLevel The log level to set for root logger.
	 */
    void changeRootLogLevel(final LogLevel logLevel);

    /**
     * Change the log level for the parameter logger.
     * @param logger The logger to change the log level.
     * @param logLevel The log level to set for logger.
     */
    void setLogLevelForLogger(final String logger, final LogLevel logLevel);

    /**
     * All logger that have a log level.
     * @return A map with logger and the log level.
     */
    Map<String, String> getLoggerWithLogLevel();

    /**
     * All logger.
     * @return A map with logger an the log level -
     *  if no log level set then the value is null.
     */
    Map<String, String> getAllLogger();

    /**
     * Log level for root logger.
     * @return The log level of root logger.
     */
    String getRootLogLevel();

}
