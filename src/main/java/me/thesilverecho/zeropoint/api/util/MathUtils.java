package me.thesilverecho.zeropoint.api.util;

public class MathUtils
{


	public static float map(float index, float start1, float stop1, float start2, float stop2)
	{
		return ((index - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
	}

	public static float easeInOutCirc(float x)
	{
		return (float) (x < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2);
	}
}
