package me.thesilverecho.zeropoint.impl.module.render2;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.TickEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.music.MusicPlayer;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

@ClientModule(name = "Music hud", active = true, keyBinding = GLFW.GLFW_KEY_P)
public class MusicHud extends BaseModule
{
	private float volume;
	private String songName = "", songAuthor = "";
	private float currentProgress = 0;

	private float offsetX = 32, offsetY = 32;


	@EventListener
	public void renderEvent(Render2dEvent.Pre event)
	{


		final float paddingX = 3;
		final float paddingY = 3;
		offsetX = 23;
		offsetY = 23;

		final MatrixStack matrixStack = event.matrixStack();

		RenderUtilV2.roundRect(matrixStack, offsetX - paddingX, offsetY - paddingY, 100 + paddingX * 2, 25 + paddingY * 2, 2f, ColourHolder.decode("#2b2b2b").setAlpha(90));
		final CustomFont font = APIFonts.REGULAR.getFont();
		final float height = FontRenderer.getHeight(font, 0.5f);
		FontRenderer.renderText(font, 0.5f, matrixStack, songName, offsetX, offsetY);
		FontRenderer.renderText(font, 0.5f, matrixStack, songAuthor, offsetX, offsetY + height);
		RenderUtilV2.roundRect(matrixStack, offsetX, offsetY + height * 2, 100, 5, 2f, ColourHolder.decode("#2b2b2b"));
		RenderUtilV2.roundRect(matrixStack, offsetX, offsetY + height * 2, 100 * currentProgress, 5, 2f, ColourHolder.decode("#00FF2b"));
		FontRenderer.renderText(font, 0.5f, matrixStack, volume + "", offsetX, offsetY + height * 3);
		for (int i = 0; i < 600; i++)
		{

//			RenderUtilV2.rectangle(matrixStack, 0, 0, 120, height, new ColourHolder(50, 50, 50, 190));
		}

		final float[] samples = MusicPlayer.INSTANCE.getSamples();
		for (int j = -1; j <= 1; j++)
		{
			if (j == 0)
				continue;
	/*		for (int i = 0; i < 180; i++)
			{
				final int index = (int) MathUtils.map(i, 0, 180, 0, samples.length);
				final float rad = MathUtils.map(samples[index], -1, 1, 110, 300);
				float x = (float) (rad * Math.sin(Math.toRadians(i))) * j;
				float y = (float) (rad * Math.cos(Math.toRadians(i)));
				RenderUtilV2.circle(matrixStack, event.scaledWidth() / 2 + x, event.scaledHeight() / 2 + y, 10, 10, 5, new ColourHolder(Color.HSBtoRGB(i, 0.8f, 0.8f)));

			}*/
		}


	}

	private static class MusicBar
	{
		public float x, y;
		float width, height;

		public MusicBar(float width, float height)
		{
			this.width = width;
			this.height = height;
		}

		public void render(MatrixStack matrixStack, float y)
		{
			RenderUtilV2.rectangle(matrixStack, x, y, 4, height, ColourHolder.decode("#1bFF2b"));
		}
	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final MusicPlayer player = MusicPlayer.INSTANCE;
		final AudioTrack currentSong = player.getCurrentSong();
		if (currentSong != null)
		{
			final AudioTrackInfo info = currentSong.getInfo();
			this.songName = valueOrDefault(info.title);
			this.songAuthor = valueOrDefault(info.author);
			this.currentProgress = currentSong.getPosition() * 1f / currentSong.getDuration();
			float sum = 0;
			float[] samples = player.getSamples();
			for (float sample : samples)
				sum += sample * sample;
			this.volume = (float) Math.sqrt(sum / samples.length);
		}
	}

	private String valueOrDefault(String s)
	{
		return s == null ? "No info available." : s;
	}

}