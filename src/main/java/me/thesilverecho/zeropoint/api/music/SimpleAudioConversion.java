package me.thesilverecho.zeropoint.api.music;

import javax.sound.sampled.AudioFormat;

/**
 * Full credit goes to Radiodef for this code. Some code for the encoding was removed as there was no real need for it.
 *
 * <p>Performs simple audio format conversion.</p>
 *
 * @author Radiodef
 * @see <a href="http://stackoverflow.com/a/26824664/2891664">Overview on Stack Overflow</a>
 */
public final class SimpleAudioConversion
{
	private SimpleAudioConversion() {}

	/**
	 * Converts from a byte array to an audio sample float array.
	 *
	 * @param bytes   the byte array, filled by the AudioInputStream
	 * @param samples an array to fill up with audio samples
	 * @param blen    the return value of AudioInputStream.read
	 * @param fmt     the source AudioFormat
	 * @return the number of valid audio samples converted
	 * @throws NullPointerException           if bytes, samples or fmt is null
	 * @throws ArrayIndexOutOfBoundsException if bytes.length is less than blen or
	 *                                        if samples.length is less than blen / bytesPerSample(fmt.getSampleSizeInBits())
	 */
	public static int decode(byte[] bytes,
			float[] samples,
			int blen,
			AudioFormat fmt)
	{
		int bitsPerSample = fmt.getSampleSizeInBits();
		int bytesPerSample = bytesPerSample(bitsPerSample);
		boolean isBigEndian = fmt.isBigEndian();
		double fullScale = fullScale(bitsPerSample);

		int i = 0;
		int s = 0;
		while (i < blen)
		{
			long temp = unpackBits(bytes, i, isBigEndian, bytesPerSample);
			float sample;
			temp = extendSign(temp, bitsPerSample);
			sample = (float) (temp / fullScale);
			samples[s] = sample;
			i += bytesPerSample;
			s++;
		}

		return s;
	}

	/**
	 * Computes the block-aligned bytes per sample of the audio format,
	 * using Math.ceil(bitsPerSample / 8.0).
	 * <p>
	 * Round towards the ceiling because formats that allow bit depths
	 * in non-integral multiples of 8 typically pad up to the nearest
	 * integral multiple of 8. So for example, a 31-bit AIFF file will
	 * actually store 32-bit blocks.
	 *
	 * @param bitsPerSample the return value of AudioFormat.getSampleSizeInBits
	 * @return The block-aligned bytes per sample of the audio format.
	 */
	public static int bytesPerSample(int bitsPerSample)
	{
		return (bitsPerSample + 7) >>> 3;
	}

	/**
	 * Computes the largest magnitude representable by the audio format,
	 * using Math.pow(2.0, bitsPerSample - 1). Note that for two's complement
	 * audio, the largest positive value is one less than the return value of
	 * this method.
	 * <p>
	 * The result is returned as a double because in the case that
	 * bitsPerSample is 64, a long would overflow.
	 *
	 * @param bitsPerSample the return value of AudioFormat.getBitsPerSample
	 * @return the largest magnitude representable by the audio format
	 */
	public static double fullScale(int bitsPerSample)
	{
		return 1L << (bitsPerSample - 1);
	}

	private static long unpackBits(byte[] bytes,
			int i,
			boolean isBigEndian,
			int bytesPerSample)
	{
		return switch (bytesPerSample)
				{
					case 1 -> unpack8Bit(bytes, i);
					case 2 -> unpack16Bit(bytes, i, isBigEndian);
					case 3 -> unpack24Bit(bytes, i, isBigEndian);
					default -> unpackAnyBit(bytes, i, isBigEndian, bytesPerSample);
				};
	}

	private static long unpack8Bit(byte[] bytes, int i)
	{
		return bytes[i] & 0xffL;
	}

	private static long unpack16Bit(byte[] bytes,
			int i,
			boolean isBigEndian)
	{
		if (isBigEndian)
		{
			return (
					((bytes[i] & 0xffL) << 8)
							| (bytes[i + 1] & 0xffL)
			);
		} else
		{
			return (
					(bytes[i] & 0xffL)
							| ((bytes[i + 1] & 0xffL) << 8)
			);
		}
	}

	private static long unpack24Bit(byte[] bytes,
			int i,
			boolean isBigEndian)
	{
		if (isBigEndian)
		{
			return (
					((bytes[i] & 0xffL) << 16)
							| ((bytes[i + 1] & 0xffL) << 8)
							| (bytes[i + 2] & 0xffL)
			);
		} else
		{
			return (
					(bytes[i] & 0xffL)
							| ((bytes[i + 1] & 0xffL) << 8)
							| ((bytes[i + 2] & 0xffL) << 16)
			);
		}
	}

	private static long unpackAnyBit(byte[] bytes,
			int i,
			boolean isBigEndian,
			int bytesPerSample)
	{
		long temp = 0;

		if (isBigEndian)
		{
			for (int b = 0; b < bytesPerSample; b++)
			{
				temp |= (bytes[i + b] & 0xffL) << (
						8 * (bytesPerSample - b - 1)
				);
			}
		} else
		{
			for (int b = 0; b < bytesPerSample; b++)
			{
				temp |= (bytes[i + b] & 0xffL) << (8 * b);
			}
		}

		return temp;
	}

	private static long extendSign(long temp, int bitsPerSample)
	{
		int bitsToExtend = Long.SIZE - bitsPerSample;
		return (temp << bitsToExtend) >> bitsToExtend;
	}

}