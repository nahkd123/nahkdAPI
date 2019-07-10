package me.nahkd.spigot.api.livetest;

import java.text.DecimalFormat;

import me.nahkd.spigot.api.animation.TextAnimation;
import me.nahkd.spigot.apimain.nahkdAPI;

public class ExecuteableMain {

	public static void main(String[] args) {
		System.out.println("Note: This is Spigot plugin, but if you're running without Spigot, it will switch to test mode instead. For more information, please read user manual.");
		nahkdAPI.initNoSpigot();
		System.out.println("[Test](..#1) Testing animations");
		for (String tag : TextAnimation.getAnimations().keySet()) {
			TextAnimation animation = TextAnimation.getAnimations().get(tag);
			System.out.println("[Test](..#1) Testing " + tag + "...");
			for (int i = 0; i < animation.maxTicks; i++) {
				animation.currentTicks = i;
				System.out.println("(" + new DecimalFormat("000").format(i) + ") " + animation.getString("This is a simple animation test"));
			}
		}
	}

}
