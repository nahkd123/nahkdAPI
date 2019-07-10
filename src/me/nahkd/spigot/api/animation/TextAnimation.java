package me.nahkd.spigot.api.animation;

import java.util.HashMap;

public abstract class TextAnimation {
	
	public String tagName;
	public int currentTicks;
	public int maxTicks; // For animating cycle
	
	public TextAnimation(String tagName, int maxTicks) {
		this.tagName = tagName;
		this.maxTicks = maxTicks;
		this.currentTicks = 0;
	}
	
	public void register() {
		TextAnimation.registerAnimation(this);
	}
	
	public abstract String getString(String input);

	private static HashMap<String, TextAnimation> animations;
	public static HashMap<String, TextAnimation> getAnimations() {
		if (animations == null) animations = new HashMap<String, TextAnimation>();
		return animations;
	}
	public static void registerAnimation(TextAnimation animation) {
		getAnimations().put(animation.tagName, animation);
	}
	public static void tick() {
		for (String tag : getAnimations().keySet()) {
			TextAnimation animation = getAnimations().get(tag);
			animation.currentTicks++;
			if (animation.currentTicks >= animation.maxTicks) {
				animation.currentTicks = 0;
			}
		}
	}
	
}
