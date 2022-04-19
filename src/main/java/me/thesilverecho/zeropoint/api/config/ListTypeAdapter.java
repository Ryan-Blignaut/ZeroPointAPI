package me.thesilverecho.zeropoint.api.config;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListTypeAdapter<E> extends TypeAdapter<List<E>>
{

	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
	{
		@SuppressWarnings({"unchecked", "rawtypes"})
		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			final Class<T> rawType = (Class<T>) type.getRawType();
			if (rawType != List.class)
				return null;
			final ParameterizedType parameterizedType = (ParameterizedType) type.getType();
			final Type actualType = parameterizedType.getActualTypeArguments()[0];
			final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(actualType));
			return new ListTypeAdapter(adapter);
		}
	};
	private final TypeAdapter<E> adapter;

	public ListTypeAdapter(TypeAdapter<E> adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public List<E> read(JsonReader in) throws IOException
	{
		in.beginArray();
		ArrayList<E> ls = new ArrayList<>();
		while (in.peek() != JsonToken.END_ARRAY)
		{
			ls.add(adapter.read(in));
		}
		in.endArray();
		return List./*ofAll*/copyOf(ls);
	}

	@Override
	public void write(JsonWriter out, List<E> value) throws IOException
	{
		out.beginArray();
		for (E e : value)
		{
			adapter.write(out, e);
		}
		out.endArray();
	}
}