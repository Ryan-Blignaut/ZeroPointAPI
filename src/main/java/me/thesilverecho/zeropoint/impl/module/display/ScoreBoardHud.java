package me.thesilverecho.zeropoint.impl.module.display;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import me.thesilverecho.zeropoint.api.event.EventListener;
import me.thesilverecho.zeropoint.api.event.events.RenderWorldEvent;
import me.thesilverecho.zeropoint.api.event.events.render.Render2dEvent;
import me.thesilverecho.zeropoint.api.module.BaseModule;
import me.thesilverecho.zeropoint.api.module.ClientModule;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV2;
import me.thesilverecho.zeropoint.api.render.font.APIFonts;
import me.thesilverecho.zeropoint.api.render.font.CustomFont;
import me.thesilverecho.zeropoint.api.render.font.FontRenderer;
import me.thesilverecho.zeropoint.api.render.shader.APIShaders;
import me.thesilverecho.zeropoint.api.render.texture.Framebuffer;
import me.thesilverecho.zeropoint.api.util.ColourHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

@ClientModule(name = "ScoreBoard tweaks", active = true, keyBinding = GLFW.GLFW_KEY_U)
public class ScoreBoardHud extends BaseModule
{

	private APIFonts font = APIFonts.FREE_SANS;
	private boolean showNumbers = false;
	private ColourHolder customColour = ColourHolder.FULL;

	private String seperator = ": ";

	private float x = 150, y = 150;

	private int xPadding = 5, yPadding = 2;

	//positioning will be a ting at some stage.

	//TODO: find a good way to blur "ui" elements.
	private boolean blurBackground;
	private boolean useRoundedCorners;

	private Framebuffer fbo, fbo2;
	private float radius = 2.0f;
	private float quality = 1.0f;

/*
*
* 	private void renderScoreboardSidebar(MatrixStack matrices, ScoreboardObjective objective) {
		Scoreboard scoreboard = objective.getScoreboard();
		Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
		List<ScoreboardPlayerScore> list = (List)collection.stream().filter((score) -> {
			return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
		}).collect(Collectors.toList());
		Object collection;
		if (list.size() > 15) {
			collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
		} else {
			collection = list;
		}

		List<Pair<ScoreboardPlayerScore, Text>> list2 = Lists.newArrayListWithCapacity(((Collection)collection).size());
		Text text = objective.getDisplayName();
		int i = this.getTextRenderer().getWidth((StringVisitable)text);
		int j = i;
		int k = this.getTextRenderer().getWidth(": ");

		ScoreboardPlayerScore scoreboardPlayerScore;
		MutableText text2;
		for(Iterator var11 = ((Collection)collection).iterator(); var11.hasNext(); j = Math.max(j, this.getTextRenderer().getWidth((StringVisitable)text2) + k + this.getTextRenderer().getWidth(Integer.toString(scoreboardPlayerScore.getScore())))) {
			scoreboardPlayerScore = (ScoreboardPlayerScore)var11.next();
			Team team = scoreboard.getPlayerTeam(scoreboardPlayerScore.getPlayerName());
			text2 = Team.decorateName(team, new LiteralText(scoreboardPlayerScore.getPlayerName()));
			list2.add(Pair.of(scoreboardPlayerScore, text2));
		}

		int var10000 = ((Collection)collection).size();
		Objects.requireNonNull(this.getTextRenderer());
		int l = var10000 * 9;
		int scoreboardPlayerScore = this.scaledHeight / 2 + l / 3;
		int team = true;
		int text2 = this.scaledWidth - j - 3;
		int m = 0;
		int n = this.client.options.getTextBackgroundColor(0.3F);
		int o = this.client.options.getTextBackgroundColor(0.4F);
		Iterator var18 = list2.iterator();

		while(var18.hasNext()) {
			Pair<ScoreboardPlayerScore, Text> pair = (Pair)var18.next();
			++m;
			ScoreboardPlayerScore scoreboardPlayerScore2 = (ScoreboardPlayerScore)pair.getFirst();
			Text text3 = (Text)pair.getSecond();
			Formatting var31 = Formatting.RED;
			String string = var31 + scoreboardPlayerScore2.getScore();
			Objects.requireNonNull(this.getTextRenderer());
			int q = scoreboardPlayerScore - m * 9;
			int r = this.scaledWidth - 3 + 2;
			int var10001 = text2 - 2;
			Objects.requireNonNull(this.getTextRenderer());
			fill(matrices, var10001, q, r, q + 9, n);
			this.getTextRenderer().draw(matrices, (Text)text3, (float)text2, (float)q, -1);
			this.getTextRenderer().draw(matrices, (String)string, (float)(r - this.getTextRenderer().getWidth(string)), (float)q, -1);
			if (m == ((Collection)collection).size()) {
				var10001 = text2 - 2;
				Objects.requireNonNull(this.getTextRenderer());
				fill(matrices, var10001, q - 9 - 1, r, q - 1, o);
				fill(matrices, text2 - 2, q - 1, r, q, n);
				TextRenderer var32 = this.getTextRenderer();
				float var10003 = (float)(text2 + j / 2 - i / 2);
				Objects.requireNonNull(this.getTextRenderer());
				var32.draw(matrices, (Text)text, var10003, (float)(q - 9), -1);
			}
		}

	}

* */

	public static Framebuffer blurFBO;

	@EventListener(priority = 2)
	public void render(Render2dEvent.ScoreBoard event)
	{
		final CustomFont font = this.font.getFont();

		event.ci().cancel();
		RenderUtilV2.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
		RenderUtilV2.setTextureId(fbo2.texture.getID());
//		RenderUtilV2.setTextureId(font.texture.getID());
		final Window w = MinecraftClient.getInstance().getWindow();

//	    This is working
//		RenderUtilV2.quadTexture(event.matrixStack(), 0, 0, w.getScaledWidth() / 2f, w.getScaledHeight() / 2f, 0.f, 1, 0.5f, 0.5f, ColourHolder.FULL);

//		RenderUtilV2.quadTexture(event.matrixStack(), 0, 0, w.getScaledWidth() / 2f, w.getScaledHeight() / 2f, 0.f, 1, 0.5f, 0.5f, ColourHolder.FULL);


		final double sf = w.getScaleFactor();

//		final int x = 120;
//		final int y = 120;
//		final int w1 = 160;
//		final int h1 = 160;
//		RenderUtilV2.quadTexture(event.matrixStack(), x, y, w1, h1, 1f / w.getScaledWidth() * x, 1 - 1f / w.getScaledHeight() * y, 1f / w.getScaledWidth() * w1, 1 - 1f / w.getScaledHeight() * h1, ColourHolder.FULL);


//		fbo2.clear();
		/*RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(framebuffer.textureWidth, framebuffer.textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 10f);
		RenderUtilV2.setTextureId(fbo2.texture.getID());
		RenderUtilV2.postProcessRect(12, 12, 0, 0, 0.5f, 0.5f);*/
//		fbo.clear();

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
		float rectY = this.y - yPadding;
		final float rectW = (float) maxWidth.get() + xPadding * 2;
		final int r = 4;
		final float rectH = (collection.size()) * FontRenderer.getHeight(font, fontSize) + yPadding * 2;

//		if (ZeroPointClient.BLUR)
//			RenderUtilV2.roundRectTexture(matrixStack, rectX, rectY, rectW, fontHeight, 0, 1 - (float) (fontHeight) / MinecraftClient.getInstance().getFramebuffer().textureWidth, 1, 1, rectX + r, rectY + r, rectX + rectW - r, rectY + fontHeight, r, BlurModule.blurFramebuffer.texture.getID(), ColourHolder.decode("#ff5159").setAlpha(120));

//		RenderUtilV2.setShader(APIShaders.RECTANGLE_TEXTURE_SHADER.getShader());
//		RenderUtilV2.setTextureId(fbo2.texture.getID());
//		RenderUtilV2.quadTexture(event.matrixStack(), rectX, rectY, rectX + rectW, rectY + rectH + fontHeight, 1f / w.getScaledWidth() * rectX, 1 - 1f / w.getScaledHeight() * rectY, 1f / w.getScaledWidth() * (rectX + rectW), 1 - 1f / w.getScaledHeight() * (rectY + rectH + fontHeight), ColourHolder.FULL);


	/*	glEnable(GL_STENCIL_TEST);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glStencilFunc(GL_EQUAL, 1, 0xFF);
		glStencilMask(0x00);
		RenderUtilV2.rectangle(matrixStack, 0, 0, 1000, 1000, ColourHolder.FULL);
		glDisable(GL_STENCIL_TEST);

		glEnable(GL_STENCIL_TEST);
		glStencilFunc(GL_ALWAYS, 1, 0xFF);
		glStencilMask(0xff);
		//this will be in the blur class
		RenderUtilV2.circle(matrixStack, 0, 190, 80, 80, 80 / 2f, ColourHolder.decode("#469150"));

		glDisable(GL_STENCIL_TEST);



		RenderUtilV2.circle(matrixStack, 0, 190, 80, 80, 80 / 2f, ColourHolder.FULL);*/


//		blurFBO.clear();

		if (blurFBO == null) blurFBO = new Framebuffer();
		blurFBO.bind();
		RenderUtilV2.roundRect(matrixStack, rectX, rectY, rectW, fontHeight, rectX + r, rectY + r, rectX + rectW - r, rectY + fontHeight, r, ColourHolder.decode("#ff5159").setAlpha(120));
		rectY += fontHeight;
		RenderUtilV2.roundRect(matrixStack, rectX, rectY, rectW, rectH, rectX + r, rectY, rectX + rectW - r, rectY + rectH - r, r, ColourHolder.decode("#2b2b2b").setAlpha(120));
		blurFBO.unbind();

		rectY -= fontHeight;
		RenderUtilV2.roundRect(matrixStack, rectX, rectY, rectW, fontHeight, rectX + r, rectY + r, rectX + rectW - r, rectY + fontHeight, r, ColourHolder.decode("#ff5159").setAlpha(120));
		rectY += fontHeight;
		RenderUtilV2.roundRect(matrixStack, rectX, rectY, rectW, rectH, rectX + r, rectY, rectX + rectW - r, rectY + rectH - r, r, ColourHolder.decode("#2b2b2b").setAlpha(120));
//		RenderUtilV2.circleTexture(matrixStack, rectX, rectY, rectW, rectH, 1f / w.getScaledWidth() * rectX, 1 - 1f / w.getScaledHeight() * rectY, 1f / w.getScaledWidth() * (rectX + rectW), 1 - 1f / w.getScaledHeight() * (rectY + rectH), r, fbo2.texture.getID(), ColourHolder.decode("#2b2b2b").setAlpha(120));


//		RenderUtilV2.roundRect(matrixStack, rectX, rectY, rectW, rectH, 4, ColourHolder.decode("#0f111a").setAlpha(120));

		FontRenderer.renderText(font, fontSize, matrixStack, heading, this.x + (maxWidth.floatValue() / 2f) - FontRenderer.getWidth(font, fontSize, heading) / 2, this.y - 1);
		for (int i = 0, stringsSize = strings.size(); i < stringsSize; i++)
		{
			String data = strings.get(i);
			FontRenderer.renderText(font, fontSize, matrixStack, data, this.x, this.y + FontRenderer.getHeight(font, fontSize) * (i + 1));
		}
//		framebuffer.bind();
//		framebuffer.unbind();

	/*	RenderUtilV2.setShader(APIShaders.ITEM.getShader());
		RenderUtilV2.setTextureId(framebuffer.texture.getID());

		final net.minecraft.client.gl.Framebuffer fbo = MinecraftClient.getInstance().getFramebuffer();

		RenderUtilV2.setShaderUniform("TexelSize", new Vec2f(1.0f / fbo.textureWidth * (this.radius * this.quality), 1.0f / fbo.textureHeight * (this.radius * this.quality)));
		RenderUtilV2.setShaderUniform("Divider", 140.0f);
		RenderUtilV2.setShaderUniform("Radius", this.radius);
		RenderUtilV2.setShaderUniform("MaxSample", 10.0f);
		RenderUtilV2.setShaderUniform("Dimensions", new Vec2f(fbo.textureWidth, (float) fbo.textureHeight));
		RenderUtilV2.setShaderUniform("Blur", 1);
		RenderUtilV2.setShaderUniform("MixFactor", 0f);
		RenderUtilV2.setShaderUniform("MinAlpha", 1f);

		RenderUtilV2.postProcessRect(fbo.textureWidth, fbo.viewportHeight, 0, 1, 1, 0);*/


	}


	@EventListener
	public void render(RenderWorldEvent.Post event)
	{

//		RenderSystem.enableBlend();
//		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
//		RenderSystem.disableDepthTest();
		GL11.glDisable(GL11.GL_BLEND);

//		GL11.glEnable(GL11.GL_BLEND);
		glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (fbo == null)
		{
			fbo = new Framebuffer();
			fbo2 = new Framebuffer();
			blurFBO = new Framebuffer();

		}
		final net.minecraft.client.gl.Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();

		final int textureWidth = framebuffer.textureWidth;
		final int textureHeight = framebuffer.textureHeight;


		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		fbo.bind();
		RenderUtilV2.setShader(APIShaders.BLURV3.getShader());
		RenderUtilV2.setShaderUniform("TextureSize", new Vec2f(textureWidth, textureHeight));
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(1, 0));
		RenderUtilV2.setShaderUniform("Radius", 10f);
		final int colorAttachment = framebuffer.getColorAttachment();
		RenderUtilV2.setTextureId(colorAttachment);
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		fbo.unbind();

		fbo2.bind();
		RenderUtilV2.setShaderUniform("BlurDir", new Vec2f(0, 1));
		RenderUtilV2.setTextureId(fbo.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);
		fbo2.unbind();
		fbo.clear();

		RenderUtilV2.setShader(APIShaders.COMPOSITE.getShader());
		GLWrapper.activateTexture(1, blurFBO.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.setTextureId(fbo2.texture.getID());
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);

//		fbo.bind();
	/*	RenderUtilV2.setShader(APIShaders.BLOOM.getShader());
		GLWrapper.activateTexture(1, blurFBO.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.setTextureId(blurFBO.texture.getID());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(4, 0));
		RenderUtilV2.setShaderUniform("Radius", 6f);
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
		for (int i = 1; i <= 6; i++) buffer.put(calculateGaussianValue(i, 6f));
		buffer.rewind();
		RenderUtilV2.setShaderUniform("Weights", buffer);
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);*/
//		fbo.unbind();

	/*	GLWrapper.activateTexture(1, blurFBO.texture.getID());
		RenderUtilV2.setShaderUniform("Sampler1", 1);
		RenderUtilV2.setTextureId(fbo.texture.getID());
		RenderUtilV2.setShaderUniform("Direction", new Vec2f(11, 0));
		RenderUtilV2.setShaderUniform("Radius", 16f);
		RenderUtilV2.setShaderUniform("Weights", buffer);
		RenderUtilV2.postProcessRect(framebuffer.viewportWidth, framebuffer.viewportHeight, 0, 0, 1, 1);*/
//		glDisable(GL_BLEND);

		fbo.clear();
		fbo2.clear();
		blurFBO.clear();

	}


}
