package me.thesilverecho.zeropoint.impl.module.render2;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
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

	@EventListener
	public void renderEvent(RenderWorldEvent.Post event)
	{

	}

	@Override
	public void onEnable()
	{
		super.onEnable();
	}

	private String songName = "", songAuthor = "";
	private float currentProgress = 0;

	@EventListener
	public void renderEvent(Render2dEvent.Pre event)
	{
		final MatrixStack matrixStack = event.matrixStack();

		final CustomFont font = APIFonts.REGULAR.getFont();
		final float height = FontRenderer.getHeight(font, 0.5f);
		FontRenderer.renderText(font, 0.5f, matrixStack, songName, 0, 0);
		final float width = FontRenderer.renderText(font, 0.5f, matrixStack, songAuthor, 0, height);

		RenderUtilV2.roundRect(matrixStack, 0, height * 2, width, 5, 2f, ColourHolder.decode("#2b2b2b"));
		RenderUtilV2.roundRect(matrixStack, 0, height * 2, width * currentProgress, 5, 2f, ColourHolder.decode("#00FF2b"));

		FontRenderer.renderText(font, 0.5f, matrixStack, currentProgress + "%", 0, height * 3);
	}

	@EventListener
	public void onTick(TickEvent.StartTickEvent event)
	{
		final AudioTrack currentSong = MusicPlayer.getPlayer().getCurrentSong();
		if (currentSong != null)
		{
			final AudioTrackInfo info = currentSong.getInfo();
			this.songName = valueOrDefault(info.title);
			this.songAuthor = info.author;
			this.currentProgress = currentSong.getPosition() * 1f / currentSong.getDuration();
		}
	}

	private String valueOrDefault(String s)
	{
		return s == null ? "No info available" : s;
	}

}