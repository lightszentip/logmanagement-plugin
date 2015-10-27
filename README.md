# README #

[![Build Status](https://travis-ci.org/lightszentip/logmanagement-plugin.svg?branch=master)](https://travis-ci.org/lightszentip/logmanagement-plugin)

The plugin can change the log level for slf4j logging on the fly or get informations about logger and log levels.

You can use it with:
```
LogManagementPluginFactory.getLogManagementPlugin()
```

Change the log level for example:
```
LogManagementPluginFactory.getLogManagementPlugin().changeRootLogLevel(logLevel)
```

Example: https://github.com/lightszentip/logmanagement-plugin-example-app

To get the artifact from Repository

```
<dependency>
  <groupId>com.lightszentip.module</groupId>
  <artifactId>logmanagement-plugin</artifactId>
  <version>0.9.1</version>
</dependency>
```
