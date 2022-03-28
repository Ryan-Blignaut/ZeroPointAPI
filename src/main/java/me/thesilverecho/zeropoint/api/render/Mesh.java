package me.thesilverecho.zeropoint.api.render;

import me.thesilverecho.zeropoint.api.util.ColourHolder;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh
{
	public enum Attrib
	{
		Float(1),
		Vec2(2),
		Vec3(3),
		Color(4);

		public final int size;

		Attrib(int size)
		{
			this.size = size;
		}
	}

	public boolean depthTest = false;
	public double alpha = 1;

	private final DrawMode drawMode;
	private final int primitiveVerticesSize;

	private final int vao, vbo, ibo;

	private ByteBuffer vertices;
	private long verticesPointer;
	private int vertexComponentCount;

	private ByteBuffer indices;
	private long indicesPointer;

	private int vertexI, indicesCount;

	private boolean building, rendering3D;
	private double cameraX, cameraZ;
	private boolean beganRendering;


	public Mesh(DrawMode drawMode, Attrib... attributes)
	{
		int stride = 0;
		for (Attrib attribute : attributes) stride += attribute.size * 4;

		this.drawMode = drawMode;
		this.primitiveVerticesSize = stride * drawMode.indicesCount;

		vertices = BufferUtils.createByteBuffer(primitiveVerticesSize * 256 * 4);
		verticesPointer = memAddress(vertices);

		indices = BufferUtils.createByteBuffer(drawMode.indicesCount * 512 * 4);
		indicesPointer = memAddress(indices);

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

		int offset = 0;
		for (int i = 0; i < attributes.length; i++)
		{
			int attribute = attributes[i].size;
			glEnableVertexAttribArray(i);
			glVertexAttribPointer(i, attribute, GL_FLOAT, false, stride, offset);
			offset += attribute * 4;
		}
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}


	public void begin()
	{
		if (building) throw new IllegalStateException("Mesh.end() called while already building.");

		vertexComponentCount = 0;
		vertexI = 0;
		indicesCount = 0;

		building = true;
		cameraX = 0;
		cameraZ = 0;
	}

	public Mesh vec3(double x, double y, double z)
	{
		long p = verticesPointer + vertexComponentCount * 4L;

		memPutFloat(p, (float) (x - cameraX));
		memPutFloat(p + 4, (float) y);
		memPutFloat(p + 8, (float) (z - cameraZ));

		vertexComponentCount += 3;
		return this;
	}

	public Mesh vec2(double x, double y)
	{
		long p = verticesPointer + vertexComponentCount * 4L;

		memPutFloat(p, (float) x);
		memPutFloat(p + 4, (float) y);

		vertexComponentCount += 2;
		return this;
	}

	public Mesh color(ColourHolder c)
	{
		long p = verticesPointer + vertexComponentCount * 4L;

		memPutFloat(p, c.red() / 255f);
		memPutFloat(p + 4, c.green() / 255f);
		memPutFloat(p + 8, c.blue() / 255f);
		memPutFloat(p + 12, c.alpha() / 255f * (float) alpha);

		vertexComponentCount += 4;
		return this;
	}

	public int next()
	{
		return vertexI++;
	}

	public void line(int i1, int i2)
	{
		long p = indicesPointer + indicesCount * 4L;

		memPutInt(p, i1);
		memPutInt(p + 4, i2);

		indicesCount += 2;
		growIfNeeded();
	}

	public void quad(int i1, int i2, int i3, int i4)
	{
		long p = indicesPointer + indicesCount * 4L;

		memPutInt(p, i1);
		memPutInt(p + 4, i2);
		memPutInt(p + 8, i3);

		memPutInt(p + 12, i3);
		memPutInt(p + 16, i4);
		memPutInt(p + 20, i1);

		indicesCount += 6;
		growIfNeeded();
	}

	public void growIfNeeded()
	{
		// Vertices
		if ((vertexI + 1) * primitiveVerticesSize >= vertices.capacity())
		{
			int newSize = vertices.capacity() * 2;
			if (newSize % primitiveVerticesSize != 0) newSize += newSize % primitiveVerticesSize;

			ByteBuffer newVertices = BufferUtils.createByteBuffer(newSize);
			memCopy(memAddress0(vertices), memAddress0(newVertices), vertexComponentCount * 4L);

			vertices = newVertices;
			verticesPointer = memAddress0(vertices);
			System.out.println("grow?");
		}

		// Indices
		if (indicesCount * 4 >= indices.capacity())
		{
			int newSize = indices.capacity() * 2;
			if (newSize % drawMode.indicesCount != 0) newSize += newSize % (drawMode.indicesCount * 4);

			ByteBuffer newIndices = BufferUtils.createByteBuffer(newSize);
			memCopy(memAddress0(indices), memAddress0(newIndices), indicesCount * 4L);

			indices = newIndices;
			indicesPointer = memAddress0(indices);
			System.out.println("grow i?");

		}
	}

	public void end()
	{
		if (!building) throw new IllegalStateException("Mesh.end() called while not building.");

		if (indicesCount > 0)
		{
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices.limit(vertexComponentCount * 4), GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindBuffer(GL_ARRAY_BUFFER, ibo);
			glBufferData(GL_ARRAY_BUFFER, indices.limit(indicesCount * 4), GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}

		building = false;
	}

	public void beginRender()
	{
//		GL.saveState();

	/*	if (depthTest) GL.enableDepth();
		else GL.disableDepth();
		GL.enableBlend();
		GL.disableCull();
		GL.enableLineSmooth();*/

//		glEnable(GL_BLEND);
//		glDisable(GL_CULL_FACE);
//		glEnable(GL_LINE_SMOOTH);

		beganRendering = true;
	}

	public void render()
	{
		if (building) end();

		if (indicesCount > 0)
		{
			// Setup opengl state and matrix stack
			boolean wasBeganRendering = beganRendering;
			if (!wasBeganRendering) beginRender();
			// Render
			beforeRender();

//			Shader.BOUND.setDefaults();
			glBindVertexArray(vao);
			glDrawElements(drawMode.getGL(), indicesCount, GL_UNSIGNED_INT, 0);
			// Cleanup opengl state and matrix stack
			glBindVertexArray(0);

			if (!wasBeganRendering) endRender();
		}
	}

	public void endRender()
	{

//		GL.restoreState();

		beganRendering = false;
	}

	protected void beforeRender() {}

}
