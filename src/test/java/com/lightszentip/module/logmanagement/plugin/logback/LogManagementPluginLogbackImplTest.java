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

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.google.common.collect.Lists;
import com.lightszentip.module.logmanagement.plugin.LogManagementPlugin;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggerFactory.class, Logger.class, LoggerContext.class})
public class LogManagementPluginLogbackImplTest {
	
	private static final String THIRD_LOGGER_FIXED_NAME = "thirdLogger";
	private static final String SECOND_LOGGER_FIXED_NAME = "secondLogger";
	private static final String FIRST_LOGGER_FIXED_NAME = "firstLogger";
	private final Level infoLevel = Level.INFO;
	private final Level warnLevel = Level.WARN;

	@Mock
	private LoggerContext mockLoggerContext;
	@Mock
	private Logger mockRootLogger;
	@Mock
	private Logger firstLogger;
	@Mock
	private Logger secondLogger;
	@Mock
	private Logger thirdLogger;
	
	private LogManagementPlugin loggerUnderTest;
	
	@Before
	public void setUp() throws Exception {
		
		mockStatic(LoggerFactory.class);
		
		when(LoggerFactory.getILoggerFactory()).thenReturn(mockLoggerContext);	
		
		when(mockLoggerContext.getLogger(Logger.ROOT_LOGGER_NAME)).thenReturn(mockRootLogger);

		loggerUnderTest = new LogManagementPluginLogbackImpl();
		
		when(firstLogger.getLevel()).thenReturn(infoLevel);
		when(secondLogger.getLevel()).thenReturn(warnLevel);
		when(thirdLogger.getLevel()).thenReturn(null);
		when(mockRootLogger.getLevel()).thenReturn(Level.DEBUG);
		
		when(firstLogger.getName()).thenReturn(FIRST_LOGGER_FIXED_NAME);
		when(secondLogger.getName()).thenReturn(SECOND_LOGGER_FIXED_NAME);
		when(thirdLogger.getName()).thenReturn(THIRD_LOGGER_FIXED_NAME);
		when(mockRootLogger.getName()).thenReturn(Logger.ROOT_LOGGER_NAME);
		
		when(mockLoggerContext.getLoggerList()).thenReturn(Lists.newArrayList(firstLogger, secondLogger, thirdLogger));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testChangeRootLogLevel() throws Exception {
		loggerUnderTest.changeRootLogLevel(LogLevel.INFO);
		verify(mockRootLogger).setLevel(Level.INFO);
	}

	@Test
	public void testSetLogLevelForLogger() throws Exception {
		String logger = "loggerNameForTest";
		Logger mockLogger = mock(Logger.class);
		when(mockLoggerContext.getLogger(logger)).thenReturn(mockLogger);
		loggerUnderTest.setLogLevelForLogger(logger, LogLevel.WARN);
		verify(mockLogger).setLevel(Level.WARN);
	}

	@Test
	public void testGetLoggerWithLogLevel() throws Exception {
		Map<String, String> loggerMap = loggerUnderTest.getLoggerWithLogLevel();
		assertThat(loggerMap, Matchers.allOf(hasEntry(FIRST_LOGGER_FIXED_NAME, infoLevel.toString()),hasEntry(SECOND_LOGGER_FIXED_NAME, warnLevel.toString())));
		assertThat(loggerMap, not(hasEntry(THIRD_LOGGER_FIXED_NAME, null)));
	}

	@Test
	public void testGetAllLogger() throws Exception {
		Map<String, String> loggerMap = loggerUnderTest.getAllLogger();
		assertThat(loggerMap, Matchers.allOf(
				hasEntry(FIRST_LOGGER_FIXED_NAME, infoLevel.toString()),
				hasEntry(SECOND_LOGGER_FIXED_NAME, warnLevel.toString()),
				hasEntry(THIRD_LOGGER_FIXED_NAME, null)));
	}
	
	@Test
	public void testGetRootLogLevel() {
		String rootLogLevel = loggerUnderTest.getRootLogLevel();
		assertThat(rootLogLevel, is(LogLevel.DEBUG.name()));
	}
	
	@Test
	public void testLogLevels() {
		String logger = "loggerNameForTest";
		Logger mockLogger = mock(Logger.class);
		when(mockLoggerContext.getLogger(logger)).thenReturn(mockLogger);
		for (LogLevel logLevel : LogLevel.values()) {
			loggerUnderTest.setLogLevelForLogger(logger, logLevel);
			verify(mockLogger).setLevel(Level.valueOf(logLevel.name()));
		}
	}

}
