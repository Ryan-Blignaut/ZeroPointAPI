package me.thesilverecho.zeropoint.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZeroPointApiLogger
{
	private static final Logger LOGGER = LogManager.getLogger("Zero Point API");
	private static boolean displayErrors, displayInfo, displayDebug;

	/**
	 * Logs an error, but also includes the class method and line where the error occurred.
	 *
	 * @param message the message to be displayed.
	 */
	public static void error(Object message)
	{
		if (displayErrors)
		{
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			LOGGER.error(className + "." + methodName + "():" + lineNumber + " \t " + message);
		}
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
