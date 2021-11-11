package me.thesilverecho.zeropoint.api.module;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class CollectionModuleSerialisation extends TypeAdapter<ArrayList<BaseModule>>
{

	@Override
	public void write(JsonWriter out, ArrayList<BaseModule> value) throws IOException
	{
		out.beginObject();
		for (BaseModule baseModule : value)
		{
			out.name(baseModule.getName());
			out.beginObject();
			out.name("enabled");
			out.value(baseModule.isEnabled());
			out.name("key");
			out.value(baseModule.getKeybind().code());
			out.endObject();
		}
		out.endObject();
	}

	@Override
	public ArrayList<BaseModule> read(JsonReader in) throws IOException
	{
		final ArrayList<BaseModule> t = new ArrayList<>();
		in.beginObject();
		while (in.hasNext())
		{
			in.nextName();
			in.beginObject();
			in.nextName();
			final boolean b = in.nextBoolean();
			in.nextName();
			final int i = in.nextInt();
			t.add(new BaseModule().setKeybind(i).setEnabled(b));
			in.endObject();
		}
		in.endObject();
		return t;
	}
}
