package me.nahkd.spigot.api.animation.animations;

import me.nahkd.spigot.api.animation.TextAnimation;

public class TextRainbowAnimation extends TextAnimation {

	private static String rainbowString = "c6eab9d";
	
	public TextRainbowAnimation() {
		super("rainbow", rainbowString.length());
	}

	@Override
	public String getString(String input) {
		return "§" + rainbowString.charAt(currentTicks) + input;
	}

}
