package me.thesilverecho.zeropoint.api.render.font;

import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;

public class StyledTextLookup
{
	private final List<Character> charterList = new ArrayList<>();
	private final List<Style> styleChangeList = new ArrayList<>();

	private void load() {}


	public void apply(OrderedText text)
	{
		text.accept((index, style, codePoint) ->
		{
			charterList.add((char) codePoint);
			styleChangeList.add(style);
			return true;
		});
	}

	public List<Character> getCharterList()
	{
		return charterList;
	}

	public List<Style> getStyleChangeList()
	{
		return styleChangeList;
	}
}
