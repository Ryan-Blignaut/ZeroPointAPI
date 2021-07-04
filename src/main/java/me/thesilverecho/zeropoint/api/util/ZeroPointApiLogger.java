package me.thesilverecho.zeropoint.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZeroPointApiLogger
{
	private static final Logger LOGGER = LogManager.getLogger("Zero Point API");
	private static boolean displayErrors, displayInfo, displayDebug;

	public static void error(Object message)
	{
		if (displayErrors)
			LOGGER.error(message);
	}

	public static void error(String message, Object e)
	{
		if (displayErrors)
			LOGGER.error(message, e);
	}

	public static void info(Object message)
	{
		if (displayInfo)
			LOGGER.info(message);
	}

	public static void debug(Object message)
	{
		if (displayDebug)
			LOGGER.debug(message);
	}

	public static void setUp(boolean displayErrors, boolean displayInfo, boolean displayDebug)
	{
		ZeroPointApiLogger.displayErrors = displayErrors;
		ZeroPointApiLogger.displayInfo = displayInfo;
		ZeroPointApiLogger.displayDebug = displayDebug;
	}
}
