package me.nahkd.spigot.api.debugger;

import me.nahkd.spigot.apimain.nahkdAPI;

public class Debug {
	
	public static boolean debugMode;
	public void init() {
		// Change this to true will let you to view what is happening, even nahkdAPI
		debugMode = false;
	}
	public static void initDebug() {
		debugMode = true;
		Debug.out("Debug mode turned on!");
	}
	
	public static void out(String string) {
		if (debugMode) nahkdAPI.console.sendMessage("§c(Debug)§4>> §r" + string);
	}
	public static void out(String label, double number) {
		Debug.out("§b" + label + "§7: §a" + number);
	}
	
}
