package me.nahkd.spigot.api.animation.animations;

import me.nahkd.spigot.api.animation.TextAnimation;

public class TextColorsAnimation extends TextAnimation {

	private static String colorCodes = "9abcdef";
	int widthPerColor;
	
	public TextColorsAnimation(int widthPerColor) {
		super("colors", 6 * widthPerColor);
		this.widthPerColor = widthPerColor;
	}

	@Override
	public String getString(String input) {
		int cycle = currentTicks / widthPerColor;
		int cycleCounter = currentTicks % widthPerColor;
		String out = "§" + colorCodes.charAt(cycle);
		for (char c : input.toCharArray()) {
			if (cycleCounter <= 0) {
				cycleCounter = widthPerColor;
				cycle++;
				if (cycle >= colorCodes.length()) cycle = 0;
				out += "§" + colorCodes.charAt(cycle);
			}
			out += c;
			cycleCounter--;
		}
		return out;
	}

}
