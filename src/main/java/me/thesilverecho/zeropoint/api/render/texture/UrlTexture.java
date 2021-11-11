package me.thesilverecho.zeropoint.api.render.texture;

import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlTexture
{
	private final String location;
	private Texture2D texture;
	private boolean isLoaded;

	public UrlTexture(String location)
	{
		this.location = location;
	}

	public void loadTexture()
	{
		try
		{
			URL url = new URL(location);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
			httpCon.connect();
			if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				ZeroPointApiLogger.error("Url texture failed to connect to: " + location);
			} else
			{
				final InputStream conStream = httpCon.getInputStream();
				texture = Texture2D.read(conStream);
				isLoaded = true;
				conStream.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			isLoaded = false;
		}

	}

	public boolean isLoaded()
	{
		return isLoaded;
	}

	public Texture2D getTexture()
	{
//		if (!isLoaded)
//			loadTexture();
		return texture;
	}
}
