package me.thesilverecho.zeropoint.api.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Various utils for dynamic class loading.
 * Most of this code was derived from : <renderer href="https://stackabuse.com/example-loading-a-java-class-at-runtime">loading renderer java class at runtime</renderer>
 */
public class DynamicClassUtil
{
	/**
	 * Initialises all classes in given directory
	 *
	 * @param packageName the full package name excluding the directory
	 * @param directory   the directory name where the files will be loaded from
	 * @param type        class type of methods that will be initialised
	 * @param <T>         type of the methods that will be initialised
	 * @return list of initialised classes
	 */
	public static <T> ArrayList<T> initClasses(String packageName, String directory, Class<T> type)
	{
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final ArrayList<T> initialisedClasses = new ArrayList<>();
		getClasses(packageName, directory).forEach(className ->
				register(classLoader, className, type).ifPresent(initialisedClasses::add));
		return initialisedClasses;
	}


	public static ArrayList<String> getClasses(String packageName)
	{
		final ArrayList<String> files = new ArrayList<>();
		try
		{
			final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			final URI uri = contextClassLoader.getResource(packageName).toURI();
			final Path locationPath = uri.getScheme().equals("jar") ? /*FileSystems.newFileSystem(uri, Collections.emptyMap())*/FileSystems.getFileSystem(uri).getPath(packageName) : Paths.get(uri);
			Files.walk(locationPath).parallel().filter(path -> path.toString().endsWith(".class")).forEach(path ->
			{
				final String a = packageName.replace('/', '.');
				final String pathName = path.toString().replace('/', '.').replace('\\','.');
				final String clazz = pathName.substring(pathName.indexOf(a), pathName.length() - 6);
				files.add(clazz);
			});
		} catch (Exception e)
		{
			ZeroPointApiLogger.error("Error loading :", e);
			e.printStackTrace();
		}

		return files;
	}

	/**
	 * Gets all classes in class path's directory.
	 *
	 * @param packageName the full package name excluding the directory
	 * @param directory   the directory name where the files will be loaded from
	 * @return list of class locations
	 */
	public static ArrayList<String> getClasses(String packageName, String directory)
	{
		final ArrayList<String> files = new ArrayList<>();
		getDirs(packageName, directory, Thread.currentThread().getContextClassLoader()).forEach(file -> recursiveRetrieveFiles(files, packageName, file));
		return files;
	}

	/**
	 * Gets all directories from path.
	 *
	 * @param packageName the full package name excluding the directory
	 * @param directory   the directory name where the files will be loaded from
	 * @param classLoader used for loading of classes
	 * @return list of directories
	 */
	private static List<File> getDirs(String packageName, String directory, ClassLoader classLoader)
	{
		List<File> dirs = new ArrayList<>();
		try
		{
			final String realPath = packageName + "." + directory;
			String path = realPath.replace('.', '/');
			final Enumeration<URL> resources = classLoader.getResources(path);
			resources.asIterator().forEachRemaining(url -> dirs.add(new File(url.getFile())));
		} catch (IOException e)
		{
			ZeroPointApiLogger.error("Error getting directories", e);
		}
		return dirs;
	}

	/**
	 * Recursively searches through directories and adds any files found to files list.
	 *
	 * @param files         list that files will be added to
	 * @param packageName   the full package name
	 * @param potentialFile the potential file or directory
	 */
	private static void recursiveRetrieveFiles(ArrayList<String> files, String packageName, File potentialFile)
	{
		if (potentialFile.isDirectory())
			for (File listFile : Objects.requireNonNull(potentialFile.listFiles()))
				recursiveRetrieveFiles(files, packageName + "." + potentialFile.getName(), listFile);
		else
		{
			final String fileNoClassExt = potentialFile.getName().substring(0, potentialFile.getName().length() - 6);
			files.add(packageName + "." + fileNoClassExt);
		}
	}


	/**
	 * Helper method to load class or provide an empty
	 *
	 * @param classLoader used for loading of classes
	 * @param file        file to be initialised
	 * @param parentClass parent class type of methods that will be initialised
	 * @param <T>         type of the methods that will be initialised
	 * @return an optional of new instance of class or empty if error occurs
	 */
	private static <T> Optional<T> register(ClassLoader classLoader, String file, Class<T> parentClass)
	{
		try
		{
			final Class<?> clazz = Class.forName(file, true, classLoader);
			if (clazz.getSuperclass() == parentClass)
			{
				Constructor<? extends T> constructor = clazz.asSubclass(parentClass).getConstructor();
				return Optional.of(constructor.newInstance());
			}

		} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e)
		{
			ZeroPointApiLogger.error("Error registering class: " + file, e);
		}
		return Optional.empty();
	}
}
