package me.thesilverecho.zeropoint.api.render.texture;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GIFTexture
{

	// gif is made up of a series of images, so to animate it all we need to do is change the image rendered dependent on what frame we are on
	// This process should be framerate independent meaning that if we are getting 100 frames or 10 frames the image should switch at the same time

	/*
	*
		Byte Hexadecimal	Text or value	    Meaning
		0	    47          49 46 38 39 61	GIF89a	Header
		6	    03          00  3	        Logical screen width
		8	    05          00	5	        Logical screen height
		A	    F7		                    GCT follows for 256 colors with resolution 3 × 8 bits/primary, the lowest 3 bits represent the bit depth minus 1, the highest true bit means that the GCT is present
		B	    00	        0	            Background color: #000000, black
		C	    00	        0	            Default pixel aspect ratio, 0:0
		D	    00 00 00		            R (red)	G (green)	B (blue)
		0	    0	0                       Global Color Table, color #0: #000000, black
		10	    80 00 00            		r 128 g 0 b 0  		Global Color Table, color #1: transparent bit, not used in image
		...	...	...	Global Color Table extends to 30A
		30A	FF FF FF
		R (red)	G (green)	B (blue)
		255	255	255
		Global Color Table, color #255: #ffffff, white
		30D	21 F9		Graphic Control Extension (comment fields precede this in most files)
		30F	04	4	Amount of GCE data, 4 bytes
		310	01		Transparent background color; this is a bit field, the lowest bit signifies transparency
		311	00 00		Delay for animation in hundredths of a second; not used
		313	10	16	Color number of transparent pixel in GCT
		314	00		End of GCE block
		315	2C		Image descriptor
		316	00 00 00 00	(0, 0)	North-west corner position of image in logical screen
		31A	03 00 05 00	(3, 5)	Image width and height in pixels
		31E	00	0	Local color table bit, 0 means none
		31F	08	8	Start of image, LZW minimum code size
		320	0B	11	Amount of LZW encoded image follow, 11 bytes
		321	00 51 FC 1B 28 70 A0 C1 83 01 01	<image data>	11 bytes of image data, see field 320
		32C	00	0	End of image data block
		32D	3B		File termination
	* */

	public GIFTexture(BufferedInputStream stream)
	{
		this.stream = stream;
	}

	public static void main(String[] args) throws IOException
	{
		final BufferedInputStream stream = new BufferedInputStream(Objects.requireNonNull(GIFTexture.class.getResourceAsStream("/assets/zero-point/textures/test 2.gif")));
		final GIFTexture gifTexture = new GIFTexture(stream);
//		final byte[] bytes = gifTexture.readBytesFromStream(stream);
		gifTexture.extractHeader();


		//		gifTexture.readInputStream();
	}

	private void readInputStream()
	{
		readHead();
	}

	private final InputStream stream;

	private void readHead()
	{
		System.out.print("Header: ");
		for (int i = 0; i < 6; i++)
		{
//			System.out.print(Integer.toHexString(readByteFromStream(inputStream))+" ");
			System.out.print((char) (readByte()));
		}
		System.out.println();
		System.out.printf("Width %s pixels.\n", /*readByteFromStream() | readByteFromStream()*/readByte() & 0xFF | (readByte() & 0xFF) << 8);
		System.out.printf("Height %s pixels.\n", readByte() & 0xFF | (readByte() & 0xFF) << 8);

		readContents();
	}

	final byte[] b = new byte[256];

	private void readContents()
	{
		boolean done = false;
		while (!done)
		{
			final int byteRead = this.readByte();
			switch (byteRead)
			{
				//Application Extension
				case 0x21 -> {
					System.out.println("Application Extension Start");

					final int blockSize = readByte();
					System.out.println("Block size: " + blockSize);
					int n = 0;
					int count = 0;
					for (int i = 0; i < blockSize; i++)
					{
						while (n < blockSize)
						{
							try
							{
								count = stream.read(b, n, blockSize - n);
							} catch (IOException e)
							{
								e.printStackTrace();
							}
							if (count == -1)
							{
								break;
							}
							n += count;
						}
					}
					StringBuilder app = new StringBuilder();
					for (int i = 0; i < 11; i++)
					{
						app.append((char) b[i]);
					}
					System.out.println(app);
					System.out.println(Integer.toHexString(readByte()));
				}
				//Image Descriptor of frame n
				case 0x2C -> System.out.println("Start of block");
				//Graphic Control Extension for frame n
				case 0xF9 -> {
					System.out.println("Graphic Control Extension Start");
					System.out.println(Integer.toHexString(readByte()));
				}
				//Trailer: Last byte in the file, signaling EOF
				case 0x3b -> {
					System.out.println("End of the file reached");
					done = true;
				}
			}

		}

	}


	private int readByte()
	{
		int byteRead = -1;
		try
		{
			byteRead = this.stream.read();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return byteRead;
	}

	private byte[] readBytesFromStream(InputStream stream) throws IOException
	{
		return stream.readAllBytes();
	}

	private void extractHeader()
	{
		// Header
		//Signature must always be GIF
		StringBuilder headerSignature = new StringBuilder();
		//formatVersion must be 87a or 89a
		StringBuilder formatVersion = new StringBuilder();
		// Logical Screen Descriptor

		for (int i = 0; i < 6; i++)
		{
			if (i < 3)
				headerSignature.append((char) readByte());
			else
				formatVersion.append((char) readByte());
		}

		final String s = formatVersion.toString();
		if (!headerSignature.toString().equalsIgnoreCase("GIF") || (!s.equalsIgnoreCase("87a") && !s.equalsIgnoreCase("89a")))
			return;
		System.out.println(headerSignature);
		System.out.println(formatVersion);
		extractLogicalScreenDescriptor();

//		final int packed = readByte();
//		System.out.println(packed);

//		BYTE Packed;           /* Screen and Color Map Information */
//		BYTE BackgroundColor;  /* Background Color Index */
//		BYTE AspectRatio;      /* Pixel Aspect Ratio */

	}

	private void extractLogicalScreenDescriptor()
	{
		int screenWidthPixels;
		int screenHeightPixels;
		screenHeightPixels = readByte() | readByte() << 8;
		screenWidthPixels = readByte() | readByte() << 8;
		System.out.println(screenHeightPixels);
		System.out.println(screenWidthPixels);

		for (int i = 0; i < 10; i++)
		{
			final int packed = (readByte() >> i);
			System.out.println(packed);
//			System.out.println((1L << (packed + 1)));
		}


	}


}
