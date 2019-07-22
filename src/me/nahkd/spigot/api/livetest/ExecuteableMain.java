package me.nahkd.spigot.api.livetest;

import me.nahkd.spigot.apimain.nahkdAPI;

public class ExecuteableMain {

	public static void main(String[] args) {
		System.out.println("Note: This is Spigot plugin, but if you're running without Spigot, it will switch to test mode instead. For more information, please read user manual.");
		nahkdAPI.initNoSpigot();
	}

}
