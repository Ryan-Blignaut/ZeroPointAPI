package me.thesilverecho.zeropoint.api.module;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ModuleSerialisation implements JsonDeserializer<BaseModule>, JsonSerializer<BaseModule>
{

	@Override
	public JsonElement serialize(BaseModule src, Type typeOfSrc, JsonSerializationContext context)
	{

		final JsonObject base = new JsonPrimitive(src.getName()).getAsJsonObject();
		base.add("enabled", new JsonPrimitive(src.isEnabled()));
		base.add("key", new JsonPrimitive(src.getKeybind().code()));
		return base;
	}

	@Override
	public BaseModule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject jsonObject = json.getAsJsonObject();
		final String name = jsonObject.getAsString();
		final JsonObject lev2 = jsonObject.get(name).getAsJsonObject();
		final boolean enabled = lev2.get("enabled").getAsBoolean();
		final int key = lev2.get("key").getAsInt();
		return new BaseModule().setEnabled(enabled).setKeybind(key);
	}
}
