package me.thesilverecho.zeropoint.api.render.font;

import me.thesilverecho.zeropoint.api.render.texture.Texture2D;
import me.thesilverecho.zeropoint.api.util.ApiIOUtils;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

public class SDFFont
{
	private static final int GLYPH_ATLAS_BASE_SIZE = 512;
	//Map to hold codepoint and texture info of point.
	private final HashMap<Integer, GlyphInfo> loadedCodePoints = new HashMap<>();

	private final float pixelsTall = 28;

	//	Texture to hold all the glyphs.
	public Texture2D glyphAtlas;


	private float pixelScale;
	private STBTTFontinfo fontInfo;


	public void loadFont(InputStream inputStream)
	{
//		Create the texture with the default size.
		this.glyphAtlas = new Texture2D(GLYPH_ATLAS_BASE_SIZE, GLYPH_ATLAS_BASE_SIZE, Texture2D.Format.A);
//		Initialise the font.
		this.fontInfo = STBTTFontinfo.create();
		stbtt_InitFont(fontInfo, ApiIOUtils.readBytesToBuffer(inputStream));

//		Calculate the pixel scale.
		this.pixelScale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, pixelsTall);

	}


	public void renderText(Matrix4f stack, String text, float x, float y, float scale, VertexConsumer consumer)
	{
		for (int index = 0; index < text.length(); index++)
			x += renderChar(stack, text.charAt(index), x, y, scale, consumer);
	}


	public float renderChar(Matrix4f stack, int c, float x, float y, float scale, VertexConsumer consumer)
	{
//		Make sure the glyph exists in the font.
//		final int index = stbtt_FindGlyphIndex(fontInfo, c);

		if (c == ' ') return 10;


		if (!loadedCodePoints.containsKey(c))
		{
			this.cacheChar(c);
//			RenderSystem.assertOnRenderThreadOrInit();
//			if (!RenderSystem.isOnRenderThread()) RenderSystem.recordRenderCall(() -> this.cacheChar(c));
//			else this.cacheChar(c);
		}
		final GlyphInfo glyphInfo = loadedCodePoints.get(c);
		if (glyphInfo == null) return 0;


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
		return /*glyphInfo.w() +*/  glyphInfo.xAdvance() / scale /** pixelScale*/;

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

		if (c == 731) return;

		ByteBuffer codepointSdf = stbtt_GetCodepointSDF(fontInfo, pixelScale, c, 7, (byte) 120, 64 / 8f, w, h, x, y);

		//Unable to load glyph.
		if (codepointSdf == null)
		{
			System.out.println("Unable to load glyph for " + c);
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
		System.out.println(maxHeight);
		if (positionGlx + cW > glyphAtlas.getWidth())
		{
			positionGly += maxHeight;
			maxHeight = 0;
			positionGlx = oldWidth;
			if (positionGly >= oldHeight) positionGlx = 0;
		}
		if (positionGly + cH >= glyphAtlas.getHeight())
		{
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
//		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glyphAtlas.bindTexture();
		GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, positionGlx, positionGly, cW, cH, GL_RED, GL_UNSIGNED_BYTE, codepointSdf);
		System.out.println(cW + " " + cH);
		System.out.println(x[0] + " " + y[0]);
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
			return glyphInfo.xAdvance() / 2;

		return 0;
	}


}
