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

import static com.lightszentip.module.logmanagement.plugin.util.LogLevel.DEBUG;
import static com.lightszentip.module.logmanagement.plugin.util.LogLevel.ERROR;
import static com.lightszentip.module.logmanagement.plugin.util.LogLevel.INFO;
import static com.lightszentip.module.logmanagement.plugin.util.LogLevel.TRACE;
import static com.lightszentip.module.logmanagement.plugin.util.LogLevel.WARN;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.impl.StaticLoggerBinder;

import com.lightszentip.module.logmanagement.plugin.log4j.LogManagementPluginLog4jImpl;
import com.lightszentip.module.logmanagement.plugin.logback.LogManagementPluginLogbackImpl;
import com.lightszentip.module.logmanagement.plugin.util.LogLevel;

/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StaticLoggerBinder.class})
public class LogManagementPluginFactoryTest {

	private StaticLoggerBinder mockStaticLogger;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		PowerMockito.mockStatic(StaticLoggerBinder.class);
		mockStaticLogger = Mockito.mock(StaticLoggerBinder.class);
        when(StaticLoggerBinder.getSingleton()).thenReturn(mockStaticLogger);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.lightszentip.module.logmanagement.plugin.LogManagementPluginFactory#getLogger()}.
	 */
	@Test
	public void ifSlf4jImplLog4jThenGetLogManagementLog4jPluginImpl() throws Exception {
		when(mockStaticLogger.getLoggerFactoryClassStr()).thenReturn("org.slf4j.impl.Log4jLoggerFactory﻿");
		LogManagementPlugin logManagementPluginFactory = LogManagementPluginFactory.getLogManagementPlugin();
		Assert.assertThat(logManagementPluginFactory, org.hamcrest.Matchers.instanceOf(LogManagementPluginLog4jImpl.class));
	}
	
	@Test
	public void ifSlf4jImplLogbackThenGetLogManagementLogbackPluginImpl() throws Exception {
		when(mockStaticLogger.getLoggerFactoryClassStr()).thenReturn("ch.qos.logback.classic.util.ContextSelectorStaticBinder");
		LogManagementPlugin logManagementPluginFactory = LogManagementPluginFactory.getLogManagementPlugin();
		assertThat(logManagementPluginFactory, org.hamcrest.Matchers.instanceOf(LogManagementPluginLogbackImpl.class));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void ifSlf4JImplOtherThenGetUnsupportedException() {
		when(mockStaticLogger.getLoggerFactoryClassStr()).thenReturn("foo.bar.logger");
		LogManagementPluginFactory.getLogManagementPlugin();
	}
	
	@Test
	public void testGetAllLogLevels() {
		List<LogLevel> logLevels = LogManagementPluginFactory.getAllLogLevels();
		assertThat(logLevels, contains(DEBUG, TRACE, INFO, WARN, ERROR));
	}

}
