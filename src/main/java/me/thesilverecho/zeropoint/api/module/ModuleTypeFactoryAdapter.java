package me.thesilverecho.zeropoint.api.module;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.thesilverecho.zeropoint.api.config.selector.SettingHolder;
import me.thesilverecho.zeropoint.api.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ModuleTypeFactoryAdapter implements TypeAdapterFactory
{
	private final ModuleTypeAdapter moduleTypeAdapter;

	public ModuleTypeFactoryAdapter(HashMap<String, BaseModule> baseModules)
	{
		this.moduleTypeAdapter = new ModuleTypeAdapter(baseModules);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
	{
		if (BaseModule.class.isAssignableFrom(type.getRawType()))
			return (TypeAdapter<T>) moduleTypeAdapter;
		return null;
	}

//	TODO: Error correction/handling.
	public static class ModuleTypeAdapter extends TypeAdapter<BaseModule>
	{

		private final HashMap<String, BaseModule> baseModules;

		public ModuleTypeAdapter(HashMap<String, BaseModule> baseModules)
		{
			this.baseModules = baseModules;
		}

		@Override
		public void write(JsonWriter out, BaseModule value) throws IOException
		{
			out.beginObject();
			out.name("name").value(value.getName());

//			Write the keybind.
			out.name("keybind").value(value.getKeybind().getKeyCode());
//			Write if enabled
			out.name("isEnabled").value(value.isEnabled());

			out.name("settings");
			out.beginObject();
			for (SettingHolder<?> settingHolder : value.settingHolders.getAllSettings())
			{
				out.name(settingHolder.getName());
				out.value(settingHolder.serialize());
			}
			out.endObject();

			out.endObject();
		}


		@Override
		public BaseModule read(JsonReader in) throws IOException
		{
			ArrayList<Pair<String, String>> tempSettings = new ArrayList<>();
			in.beginObject();
//			name
			in.nextName();
			final String name = in.nextString();
//          keybind
			in.nextName();
			final int key = in.nextInt();
//          isEnabled
			in.nextName();
			final boolean enabled = in.nextBoolean();
//          settings
			in.nextName();
			in.beginObject();
			while (in.peek() != JsonToken.END_OBJECT)
			{
				final String settingName = in.nextName();
				final String value = in.nextString();
				tempSettings.add(new Pair<>(settingName, value));
			}
			in.endObject();
			in.endObject();


			if (baseModules.containsKey(name))
			{
				final BaseModule baseModule = baseModules.get(name);
				tempSettings.forEach(pair -> baseModule.settingHolders.getSetting(pair.key()).ifPresent(settingHolder -> settingHolder.setValueFromSting(pair.value())));
				baseModule.setKeybind(key);
				baseModule.setEnabled(enabled);
				if (enabled) baseModule.silentRegister();

				return baseModule;
			}
			return null;
		}
	}
}
