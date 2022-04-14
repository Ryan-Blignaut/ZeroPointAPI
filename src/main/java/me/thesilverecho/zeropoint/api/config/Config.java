package me.thesilverecho.zeropoint.api.config;

import com.google.gson.*;
import me.thesilverecho.zeropoint.api.util.Pair;
import me.thesilverecho.zeropoint.api.util.ReflectionUtil;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;


/**
 * The {@code Config} class provides renderer mechanism that allows for serialization and deserialization of {@link ConfigSetting} annotated fields.
 */
public class Config
{
	/**
	 * List will hold all the instances of class registered and the config setting fields in them, useful to get all settings.
	 */
	private static final CopyOnWriteArrayList<Pair<Field, Object>> INSTANCES = new CopyOnWriteArrayList<>();

	private final CopyOnWriteArrayList<Object> configObjects = new CopyOnWriteArrayList<>();
	private final Gson gson;
	private final File configFile;
	private final JsonObject config;

	public static CopyOnWriteArrayList<Pair<Field, Object>> getAllFields()
	{
		return INSTANCES;
	}

	/**
	 * Creates Config with default Gson format, using the file name provided.
	 *
	 * @param fileName name for file without file extension
	 */
	public Config(String fileName)
	{
		this(new GsonBuilder().setExclusionStrategies(new ExclusionStrategy()
		{
			@Override
			public boolean shouldSkipField(FieldAttributes f)
			{

				return f.getAnnotation(ConfigSetting.class) == null;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz)
			{
				return false;//clazz.getAnnotation(ExcludedSelector.class) != null;
			}
		}).setPrettyPrinting().create(), fileName);
	}

	/**
	 * Creates Config  using the file name provided.
	 *
	 * @param fileName name for file without file extension
	 * @param gson     custom gson format
	 */
	public Config(Gson gson, String fileName)
	{
		this.gson = gson;
		this.configFile = new File(MinecraftClient.getInstance().runDirectory, "Config" + File.separator + fileName + ".json");

		if (!this.configFile.exists())
		{
			this.config = new JsonObject();
			saveConfig();
		} else
		{
			this.config = readFileToJson();
		}
	}

	/**
	 * Saves config.
	 */
	public void save()
	{
		configObjects.forEach(this::loadFieldToJson);
		saveConfig();
	}

	/**
	 * Registers renderer class.
	 *
	 * @param instance class to register
	 */
	public void register(Object instance)
	{
		if (Arrays.stream(instance.getClass().getDeclaredFields()).noneMatch(f -> f.isAnnotationPresent(ConfigSetting.class)))
			return;
		loadFieldToClass(instance);
		configObjects.add(instance);
	}

	/**
	 * Saves config.
	 */
	private void saveConfig()
	{

		ZeroPointApiLogger.debug("Zero-point api has created new directories: " + configFile.getParentFile().mkdirs());
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile)))
		{
			ZeroPointApiLogger.debug("Has created new file: " + configFile.createNewFile());
			bufferedWriter.write(gson.toJson(config));
		} catch (IOException e)
		{
			ZeroPointApiLogger.error("Error saving config", e);
		}

	}

	/**
	 * Reads renderer file to renderer json object.
	 *
	 * @return returns renderer json object of the files contents or an empty json object
	 */
	private JsonObject readFileToJson()
	{
		try (final FileInputStream inputStream = new FileInputStream(this.configFile))
		{
			final String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			return JsonParser.parseString(json.equals("") ? "{}" : json).getAsJsonObject();
		} catch (IOException e)
		{
			ZeroPointApiLogger.error("Error reading settings file", e);
			return new JsonObject();
		}
	}

	/**
	 * Loads all fields from json to instance.
	 *
	 * @param instance the instance of the class
	 */
	private void loadFieldToClass(Object instance)
	{
		Class<?> clazz = instance.getClass();
		if (!config.has(clazz.getSimpleName()))
			config.add(clazz.getSimpleName(), new JsonObject());
		applyStream(field -> loadFieldToClass(field, instance), instance);
		applyStream(field -> INSTANCES.add(new Pair<>(field, instance)), instance);
	}

	/**
	 * Loads renderer fields from json to instance.
	 *
	 * @param instance the instance of the class
	 */
	private void loadFieldToClass(Field field, Object instance)
	{
		Class<?> clazz = instance.getClass();
		if (config.get(clazz.getSimpleName()).getAsJsonObject().has(field.getName()))
			ReflectionUtil.setObjValueSafe(field, instance, gson.fromJson(config.getAsJsonObject(clazz.getSimpleName()).get(field.getName()), field.getGenericType()));
	}

	/**
	 * Loads all fields from instance to json.
	 *
	 * @param instance the instance of the class
	 */
	private void loadFieldToJson(Object instance)
	{
		applyStream(field -> loadFieldToJson(field, instance), instance);
	}

	/**
	 * Loads renderer fields from instance to json.
	 *
	 * @param field    the filed to be saved
	 * @param instance the instance of the class
	 */
	private void loadFieldToJson(Field field, Object instance)
	{

		final Class<?> clazz = instance.getClass();
		JsonObject classObject = config.get(clazz.getSimpleName()).getAsJsonObject();
		classObject.add(field.getName(), gson.toJsonTree(ReflectionUtil.getObjValueSafe(field, instance), field.getType()));

/*
		TODO: add post save handling
		if (field.isAnnotationPresent(PostSaveProcess.class))
		{
			final PostSaveProcess annotation = field.getAnnotation(PostSaveProcess.class);
			annotation.method().run();
		}
*/

	}

	/**
	 * Applies the consumer for all the {@link ConfigSetting} in the instances class.
	 *
	 * @param consumer what code will run with the provided field
	 * @param instance what class will be searched for all the {@link ConfigSetting}
	 */
	private void applyStream(Consumer<Field> consumer, Object instance)
	{
		final Class<?> clazz = instance.getClass();
		Arrays.stream(clazz.getDeclaredFields())
		      .filter(field -> field.isAnnotationPresent(ConfigSetting.class))
		      .filter(field -> config.has(field.getDeclaringClass().getSimpleName()))
		      .forEach(consumer);
	}


}
