package me.thesilverecho.zeropoint.api.render.font;

import com.mojang.blaze3d.systems.RenderSystem;
import me.thesilverecho.zeropoint.api.render.GLWrapper;
import me.thesilverecho.zeropoint.api.render.RenderUtilV4;
import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class SDFFont2
{
	private static final int GLYPH_ATLAS_BASE_SIZE = 512;
	//Map to hold codepoint and texture info of point.
	private final HashMap<Integer, GlyphInfo> loadedCodePoints = new HashMap<>();

	private final float pixelsTall = 28;
	private ByteBuffer ttf;
	//	Texture to hold all the glyphs.
	public Texture2D glyphAtlas;
	private float pixelScale;
	private STBTTFontinfo fontInfo;
	private final float ascent, descent, lineGap;


	public static SDFFont2 createFont(Identifier location)
	{
		return new SDFFont2(location);
	}

	private SDFFont2(Identifier location)
	{

		ApiIOUtils.getResourceFromClientPack(location).ifPresent(inputStream ->
		{
			this.ttf = ApiIOUtils.readBytesToBuffer(inputStream);
		});

		this.fontInfo = STBTTFontinfo.create();
		if (ttf == null || !stbtt_InitFont(fontInfo, ttf))
			throw new IllegalStateException("Failed to initialize font information.");
		try (MemoryStack stack = stackPush())
		{
			IntBuffer pAscent = stack.mallocInt(1);
			IntBuffer pDescent = stack.mallocInt(1);
			IntBuffer pLineGap = stack.mallocInt(1);

			stbtt_GetFontVMetrics(this.fontInfo, pAscent, pDescent, pLineGap);

			ascent = pAscent.get(0);
			descent = pDescent.get(0);
			lineGap = pLineGap.get(0);

			this.pixelScale = stbtt_ScaleForPixelHeight(fontInfo, pixelsTall);
		}
		glyphAtlas = new Texture2D(GLYPH_ATLAS_BASE_SIZE, GLYPH_ATLAS_BASE_SIZE, Texture2D.Format.A);
//		c();
	}


	public void renderText(Matrix4f stack, String text, float x, float y, float scale, VertexConsumer consumer)
	{
		for (int index = 0; index < text.length(); index++)
			x += renderChar(stack, text.charAt(index), x, y, scale, consumer);
	}

	private static int getCP(String text, int to, int i, IntBuffer cpOut)
	{
		char c1 = text.charAt(i);
		if (Character.isHighSurrogate(c1) && i + 1 < to)
		{
			char c2 = text.charAt(i + 1);
			if (Character.isLowSurrogate(c2))
			{
				cpOut.put(0, Character.toCodePoint(c1, c2));
				return 2;
			}
		}
		cpOut.put(0, c1);
		return 1;
	}

	public float renderChar(Matrix4f matrix, int c, float x, float y, float scale, VertexConsumer consumer)
	{
//		final IntBuffer allocate = IntBuffer.allocate(1);
//		getCP(String.valueOf(c), 1, 0, allocate);
//		Make sure the glyph exists in the font.
//		final int index = stbtt_FindGlyphIndex(fontInfo, c);
//		c = allocate.get(0);
		if (c == ' ') return 3;
		if (c == '\t') return 40;

		float w = 0;
		final int i = stbtt_FindGlyphIndex(fontInfo, c);

		if (i == 0)
		{
//			if (fallbackFont != null) w += fallbackFont.renderChar(c, x, y, scale);
		} else
		{
			if (!loadedCodePoints.containsKey(c))
			{
				if (!RenderSystem.isOnRenderThread())
				{
//					stbtt_FindMatchingFont()
					/*RenderSystem.recordRenderCall(() ->
					{
						this.cacheChar(c);
					});*/
				} else
				{
					GLWrapper.activateTexture(0, glyphAtlas.getID());

					this.cacheChar(c);
				}
			}
			final GlyphInfo glyphInfo = loadedCodePoints.get(c);
			if (glyphInfo == null) return 0;


			RenderUtilV4.generateLayerQuad(consumer,
					matrix,
					x + glyphInfo.x() / scale,
					y + glyphInfo.y() / scale,
					x + (glyphInfo.x() + glyphInfo.w()) / scale,
					y + (glyphInfo.y() + glyphInfo.h()) / scale,
					glyphInfo.u0() / glyphAtlas.getWidth(),
					glyphInfo.v0() / glyphAtlas.getHeight(),
					glyphInfo.u1() / glyphAtlas.getWidth(),
					glyphInfo.v1() / glyphAtlas.getHeight());
			w += glyphInfo.xAdvance() / scale;
		}
		return w;


//		RenderUtilV2.setShader(APIShaders.SDF_FONT_MASK_TEXTURE.getShader());
//		RenderUtilV2.setTextureId(glyphAtlas.getID());
//		RenderUtilV2.setQuadColourHolder(APIColour.WHITE);
	/*	RenderUtilV4.generateLayerQuad(consumer,
				stack,
				x + glyphInfo.x() / scale,
				y + glyphInfo.y() / scale,
				x + (glyphInfo.x() + glyphInfo.w()) / scale,
				y + (glyphInfo.y() + glyphInfo.h()) / scale,
				glyphInfo.u0() / glyphAtlas.getWidth(),
				glyphInfo.v0() / glyphAtlas.getHeight(),
				glyphInfo.u1() / glyphAtlas.getWidth(),
				glyphInfo.v1() / glyphAtlas.getHeight());*/

		/*if (c == ' ')
			return 10;
		if (index == 0) return 0;
		if (index == 1) return 0;
		if (!loadedCodePoints.containsKey(index)) this.cacheChar(index);
		final GlyphInfo glyphInfo = loadedCodePoints.get(index);

		if (glyphInfo == null) return 0;


//		RenderUtilV2.setShader(APIShaders.SDF_FONT_MASK_TEXTURE.getShader());
//		RenderUtilV2.setTextureId(glyphAtlas.getID());
//		RenderUtilV2.setQuadColourHolder(APIColour.WHITE);
		RenderUtilV4.generateLayerQuad(consumer,
				stack,
				x + glyphInfo.x() / scale,
				y + glyphInfo.y() / scale,
				x + (glyphInfo.x() + glyphInfo.w()) / scale,
				y + (glyphInfo.y() + glyphInfo.h()) / scale,
				glyphInfo.u0() / glyphAtlas.getWidth(),
				glyphInfo.v0() / glyphAtlas.getHeight(),
				glyphInfo.u1() / glyphAtlas.getWidth(),
				glyphInfo.v1() / glyphAtlas.getHeight()
		);*/

	}

	public void c()
	{
		for (int i = 0; i < 300; i++)
		{
			cacheChar((char) i);
		}
	}


	private void cacheChar(int c)
	{

		final int[] w = new int[1];
		final int[] h = new int[1];
		final int[] x = new int[1];
		final int[] y = new int[1];
		final int[] adv = new int[1];
		final int[] advY = new int[1];
		final int[] dec = new int[1];


		ByteBuffer codepointSdf = stbtt_GetCodepointSDF(fontInfo, pixelScale, c, 7, (byte) 120, 64 / 8f, w, h, x, y);

		//Unable to load glyph.
		if (codepointSdf == null)
		{
			return;
		}
		if (w[0] == 0) w[0] = 1;
		if (h[0] == 0) h[0] = 1;

		//Calculate position of the glyph
		stbtt_GetFontVMetrics(fontInfo, advY, dec, null);
		stbtt_GetCodepointHMetrics(fontInfo, c, adv, null);

		//If the glyph is too big for the width of the texture move to next row.
		final int cW = w[0];
		final int cH = h[0];
		if (maxHeight < cH) maxHeight = cH;
//		System.out.println(maxHeight);
		if (positionGlx + cW > glyphAtlas.getWidth())
		{
			positionGly += maxHeight;
			maxHeight = 0;
			positionGlx = oldWidth;
			if (positionGly >= oldHeight) positionGlx = 0;
		}
		if (positionGly + cH >= glyphAtlas.getHeight())
		{
			System.out.println("attempting resize");
			final int i1 = glyphAtlas.getHeight();
			final int w1 = glyphAtlas.getWidth();
			glyphAtlas = Texture2D.resize(w1 * 2, i1 * 2, true, glyphAtlas);
			oldWidth = w1;
			oldHeight = i1;
			positionGlx = oldWidth;
			positionGly = 0;
		}
//		texture.bindTexture();
		//Add the glyph to the texture.
//		glActiveTexture(GL_TEXTURE0);
//		glBindTexture(GL_TEXTURE_2D, glyphAtlas.getID());
//		GLWrapper.activateTexture(0, glyphAtlas.getID());
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, positionGlx, positionGly, cW, cH, GL_RED, GL_UNSIGNED_BYTE, codepointSdf);
//		System.out.println(cW + " " + cH);
//		System.out.println(x[0] + " " + y[0]);
//		System.out.println("cacheChar: " + c + " " + positionGlx + " " + positionGly + " " + cW + " " + cH);
		final GlyphInfo gi = new GlyphInfo(x[0], y[0] + advY[0] * pixelScale, cW, cH, positionGlx, positionGly, positionGlx + cW, positionGly + cH, adv[0] * pixelScale);
		positionGlx += cW;
		loadedCodePoints.put(c, gi);
	}




/*	private void cacheChar(int c)
	{
//		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		try (MemoryStack memoryStack = MemoryStack.stackPush())
		{
			memoryStack.push();

			final IntBuffer w = memoryStack.mallocInt(1);
			final IntBuffer h = memoryStack.mallocInt(1);
			final IntBuffer x = memoryStack.mallocInt(1);
			final IntBuffer y = memoryStack.mallocInt(1);
			final IntBuffer adv = memoryStack.mallocInt(1);
			final IntBuffer advY = memoryStack.mallocInt(1);

			final ByteBuffer codepointSdf = stbtt_GetCodepointSDF(fontInfo, this.pixelScale, c, 7, (byte) 120, 64 / 8f, w, h, x, y);
			if (codepointSdf == null)
			{
				System.out.println("Unable to load glyph for " + c);
				loadedCodePoints.put(c, new GlyphInfo(0, 0, 0, 0, 0, 0, 0, 0, 0));
				memoryStack.pop();
				return;
			}

			if (w.get(0) <= 0 || h.get(0) <= 0)
			{
				System.out.println("Negative or 0 value for char w: " + w.get(0) + " or h" + h.get(0));
				loadedCodePoints.put(c, new GlyphInfo(0, 0, 0, 0, 0, 0, 0, 0, 0));
				stbtt_FreeSDF(codepointSdf);
				memoryStack.pop();
				return;
			}

			stbtt_GetFontVMetrics(fontInfo, advY, null, null);
			stbtt_GetCodepointHMetrics(fontInfo, c, adv, null);

			final int cW = w.get(0);
			final int cH = h.get(0);
			if (maxHeight < cH) maxHeight = cH;

			if (positionGlx + cW > glyphAtlas.getWidth())
			{
				System.out.println("Expand width");
				positionGly += maxHeight;
				maxHeight = 0;
				positionGlx = oldWidth;
				if (positionGly >= oldHeight) positionGlx = 0;
			}
			if (positionGly + cH >= glyphAtlas.getHeight())
			{
				System.out.println("Height increased");
				final int atlasHeight = glyphAtlas.getHeight();
				final int atlasWidth = glyphAtlas.getWidth();
				glyphAtlas = Texture2D.resize(atlasWidth * 2, atlasHeight * 2, true, glyphAtlas);
				oldWidth = atlasWidth;
				oldHeight = atlasHeight;
				positionGlx = oldWidth;
				positionGly = 0;
			}
			GLWrapper.activateTexture(0, this.glyphAtlas.getID());

//			final int oldPixelStore = glGetInteger(GL_UNPACK_ALIGNMENT);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, positionGlx, positionGly, cW, cH, GL_RED, GL_UNSIGNED_BYTE, codepointSdf);
			final GlyphInfo gi = new GlyphInfo(x.get(0), y.get(0) + advY.get(0) * pixelScale, cW, cH, positionGlx, positionGly, positionGlx + cW, positionGly + cH, adv.get(0) * pixelScale);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

			//		final GlyphInfo gi = new GlyphInfo(x[0], y[0] + advY[0] * pixelScale, w[0], h[0], positionGlx, positionGly + advY[0] * pixelScale, positionGlx + cW, positionGly + cH, adv[0] * pixelScale);
			positionGlx += cW;
			stbtt_FreeSDF(codepointSdf);

			loadedCodePoints.put(c, gi);
			memoryStack.pop();

		}


	*//*	final int[] w = new int[1];
		final int[] h = new int[1];
		final int[] x = new int[1];
		final int[] y = new int[1];
		final int[] adv = new int[1];
		final int[] advY = new int[1];

//		final int index = stbtt_FindGlyphIndex(fontInfo, c);
//		if (index == 0 || index == 1) return;


		final ByteBuffer codepointSdf = stbtt_GetCodepointSDF(fontInfo, this.pixelScale, c, 7, (byte) 120, 64 / 8f, w, h, x, y);
		//Unable to load glyph.
		if (codepointSdf == null)
		{
			System.out.println("Unable to load glyph for " + c);
			loadedCodePoints.put(c, new GlyphInfo(0, 0, 0, 0, 0, 0, 0, 0, 0));
			return;
		}
		if (w[0] <= 0)
			w[0] = 1;
		if (h[0] <= 0)
			h[0] = 1;

		//Calculate position of the glyph
		stbtt_GetFontVMetrics(fontInfo, advY, null, null);
		stbtt_GetCodepointHMetrics(fontInfo, c, adv, null);


		//If the glyph is too big for the width of the texture move to next row.
		final int cW = w[0];
		final int cH = h[0];
		if (maxHeight < cH) maxHeight = cH;


	*//**//*	if (positionGlx + cW > glyphAtlas.getWidth())
		{
			positionGly += maxHeight;
			maxHeight = 0;
			positionGlx = oldWidth;
			if (positionGly >= oldHeight) positionGlx = 0;
		}
*//**//*
		if (positionGlx + cW > glyphAtlas.getWidth())
		{
			positionGly += maxHeight;
			maxHeight = 0;
			positionGlx = oldWidth;
			if (positionGly >= oldHeight) positionGlx = 0;
		}
		if (positionGly + cH >= glyphAtlas.getHeight())
		{
			final int atlasHeight = glyphAtlas.getHeight();
			final int atlasWidth = glyphAtlas.getWidth();
			glyphAtlas = Texture2D.resize(atlasWidth * 2, atlasHeight * 2, true, glyphAtlas);
			oldWidth = atlasWidth;
			oldHeight = atlasHeight;
			positionGlx = oldWidth;
			positionGly = 0;
		}
		//Enable the texture.
		GLWrapper.activateTexture(0, this.glyphAtlas.getID());
		//Add the glyph to the texture.
		GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, positionGlx, positionGly, cW, cH, GL_RED, GL_UNSIGNED_BYTE, codepointSdf);


		final GlyphInfo gi = new GlyphInfo(x[0], y[0] + advY[0] * pixelScale, cW, cH, positionGlx, positionGly, positionGlx + cW, positionGly + cH, adv[0] * pixelScale);
//		final GlyphInfo gi = new GlyphInfo(x[0], y[0] + advY[0] * pixelScale, w[0], h[0], positionGlx, positionGly + advY[0] * pixelScale, positionGlx + cW, positionGly + cH, adv[0] * pixelScale);
		positionGlx += cW;
		stbtt_FreeSDF(codepointSdf);

		loadedCodePoints.put(c, gi);*//*

	}*/

	private int maxHeight = 0;
	private int oldHeight = 0, oldWidth = 0;
	int positionGlx = 0, positionGly = 0;


	public float width(String s)
	{
		float w = 0;
		for (int i = 0; i < s.length(); i++)
		{
			w += widthChar(s.charAt(i));
		}
		return w;
	}

	public float widthChar(int c)
	{
		final GlyphInfo glyphInfo = loadedCodePoints.get(c);


		if (glyphInfo != null)
			return glyphInfo.xAdvance() / 3.5f;
		return 3;

	}


}
