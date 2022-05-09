package me.thesilverecho.zeropoint.impl.module.display;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import me.thesilverecho.zeropoint.api.config.ConfigSetting;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.util.APIColour;
import me.thesilverecho.zeropoint.impl.module.render2.BlurBackground;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ClientModule(name = "ScoreBoard tweaks", keyBinding = GLFW.GLFW_KEY_U)
public class ScoreBoardHud extends BaseModule
{

	@ConfigSetting
	private APIFonts font = APIFonts.FREE_SANS;
	@ConfigSetting
	private boolean showNumbers = false;
	@ConfigSetting
	private APIColour customColour = APIColour.WHITE;
	@ConfigSetting
	private String seperator = ": ";
	@ConfigSetting
	private float x = 150, y = 150;
	@ConfigSetting
	private int xPadding = 5, yPadding = 2;

	@Override
	public void onEnable()
	{
		System.out.println(x + " " + y);
		super.onEnable();
	}

	//positioning will be a ting at some stage.

	//TODO: find a good way to blur "ui" elements.
	private boolean blurBackground;
	private boolean useRoundedCorners;

	@EventListener
	public void render(Render2dEvent.ScoreBoard event)
	{
		final CustomFont font = this.font.getFont();

		event.ci().cancel();

		final ScoreboardObjective objective = event.scoreboardObjective();
		final Scoreboard scoreboard = objective.getScoreboard();
		Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
		final String heading = objective.getDisplayName().asString();
		List<ScoreboardPlayerScore> list = collection.stream().filter((score) -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
		if (list.size() > 15) collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
		else collection = list;

		AtomicDouble maxWidth = new AtomicDouble(-1);
		final MatrixStack matrixStack = event.matrixStack();

		ArrayList<String> strings = new ArrayList<>();

		final float fontSize = 0.5f;
		collection.forEach(scoreboardPlayerScore ->
		{
			Team team = scoreboard.getPlayerTeam(scoreboardPlayerScore.getPlayerName());
			final String teamName = Team.decorateName(team, new LiteralText(scoreboardPlayerScore.getPlayerName())).asString();
			final String customisedTeamName = teamName + seperator + scoreboardPlayerScore.getScore();
			strings.add(customisedTeamName);
			maxWidth.set(Math.max(maxWidth.get(), FontRenderer.getWidth(font, fontSize, customisedTeamName)));
		});


		final float fontHeight = FontRenderer.getHeight(font, fontSize);
		final float rectX = this.x - xPadding;
		AtomicReference<Float> rectY = new AtomicReference<>(this.y - yPadding);
		final float rectW = (float) maxWidth.get() + xPadding * 2;
		final int r = 4;
		final float rectH = (collection.size()) * FontRenderer.getHeight(font, fontSize) + yPadding * 2;

		BlurBackground.renderToBlur(() ->
		{
			RenderUtilV2.zIndex = -32;
			RenderUtilV2.roundRect(matrixStack, rectX, rectY.get(), rectW, fontHeight, rectX + r, rectY.get() + r, rectX + rectW - r, rectY.get() + fontHeight, r, APIColour.decode("#ff5159").setAlpha(120));
			rectY.updateAndGet(v -> v + fontHeight);
			RenderUtilV2.roundRect(matrixStack, rectX, rectY.get(), rectW, rectH, rectX + r, rectY.get(), rectX + rectW - r, rectY.get() + rectH - r, r, new APIColour(10, 10, 10, 90)/*ColourHolder.decode("#2b2b2b").setAlpha(120)*/);
			rectY.getAndUpdate(v -> v - fontHeight);
			RenderUtilV2.zIndex = 0;
		});


		FontRenderer.renderText(font, fontSize, matrixStack, heading, this.x + (maxWidth.floatValue() / 2f) - FontRenderer.getWidth(font, fontSize, heading) / 2, this.y - 1);
		for (int i = 0, stringsSize = strings.size(); i < stringsSize; i++)
		{
			String data = strings.get(i);
			FontRenderer.renderText(font, fontSize, matrixStack, data, this.x, this.y + FontRenderer.getHeight(font, fontSize) * (i + 1));
		}

	}

}
