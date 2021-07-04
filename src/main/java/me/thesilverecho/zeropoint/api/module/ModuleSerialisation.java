package me.thesilverecho.zeropoint.api.module;

import com.google.gson.*;
import me.thesilverecho.zeropoint.api.util.Pair;

import java.lang.reflect.Type;

public class ModuleSerialisation implements JsonDeserializer<Pair<String, BaseModule>>, JsonSerializer<Pair<String, BaseModule>>
{

	@Override
	public JsonElement serialize(Pair<String,  BaseModule> src, Type typeOfSrc, JsonSerializationContext context)
	{

		final JsonObject base = new JsonPrimitive(src.key()).getAsJsonObject();
		base.add("enabled", new JsonPrimitive(src.value().isEnabled()));
		base.add("key", new JsonPrimitive(src.value().getKeybind().code()));
		return base;
	}

	@Override
	public Pair<String,  BaseModule> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject jsonObject = json.getAsJsonObject();
		final String name = jsonObject.getAsString();
		final JsonObject lev2 = jsonObject.get(name).getAsJsonObject();
		final boolean enabled = lev2.get("enabled").getAsBoolean();
		final int key = lev2.get("key").getAsInt();
		return new Pair<>(name, new BaseModule(enabled, key));
	}
}
