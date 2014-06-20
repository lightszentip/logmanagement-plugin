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

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Enumeration;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lightszentip.module.logmanagement.plugin.LogManagementPlugin;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Logger.class, LogManager.class })
public class LogManagementPluginLog4jImplTest {
	
	private static final String THIRD_LOGGER_FIXED_NAME = "thirdLogger";
	private static final String SECOND_LOGGER_FIXED_NAME = "secondLogger";
	private static final String FIRST_LOGGER_FIXED_NAME = "firstLogger";
	private final Level infoLevel = Level.INFO;
	private final Level warnLevel = Level.WARN;
	
	@Mock
	private Enumeration<Category> value;
	@Mock
	private Logger mockRootLogger;
	@Mock
	private Category firstLogger;
	@Mock
	private Category secondLogger;
	@Mock
	private Category thirdLogger;
	
	private LogManagementPlugin loggerUnderTest;

	@Before
	public void setUp() throws Exception {
		mockStatic(Logger.class);
		mockStatic(LogManager.class);
		when(Logger.getRootLogger()).thenReturn(mockRootLogger);
		loggerUnderTest = new LogManagementPluginLog4jImpl();

		when(LogManager.getCurrentLoggers()).thenReturn(value);
		when(value.hasMoreElements()).thenReturn(true, true, true,false);

		when(firstLogger.getLevel()).thenReturn(infoLevel);
		when(secondLogger.getLevel()).thenReturn(warnLevel);
		when(thirdLogger.getLevel()).thenReturn(null);
		
		when(firstLogger.getName()).thenReturn(FIRST_LOGGER_FIXED_NAME);
		when(secondLogger.getName()).thenReturn(SECOND_LOGGER_FIXED_NAME);
		when(thirdLogger.getName()).thenReturn(THIRD_LOGGER_FIXED_NAME);
		
		when(mockRootLogger.getLevel()).thenReturn(Level.DEBUG);
		
		when(value.nextElement()).thenReturn( firstLogger, secondLogger, thirdLogger);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void changeTheLogLevelForRootLogger() throws Exception {
		loggerUnderTest.changeRootLogLevel(LogLevel.INFO);
		verify(mockRootLogger).setLevel(Level.INFO);
	}

	@Test
	public void testSetLogLevelForLogger() throws Exception {
		String logger = "loggerNameForTest";
		Logger mockLogger = mock(Logger.class);
		when(LogManager.getLogger(logger)).thenReturn(mockLogger);
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
		when(LogManager.getLogger(logger)).thenReturn(mockLogger);
		for (LogLevel logLevel : LogLevel.values()) {
			loggerUnderTest.setLogLevelForLogger(logger, logLevel);
			verify(mockLogger).setLevel(Level.toLevel(logLevel.name()));
		}
	}

}
