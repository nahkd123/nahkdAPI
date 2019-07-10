package me.nahkd.spigot.apimain;

import java.io.File;
import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.nahkd.spigot.api.animation.animations.TextColorsAnimation;
import me.nahkd.spigot.api.animation.animations.TextRainbowAnimation;
import me.nahkd.spigot.api.animation.animations.TextTypingAnimation;
import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.placeholder.placeholders.CalendarPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.PlayerPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.ProgressbarPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.VariablePlaceholders;
import me.nahkd.spigot.api.scripting.actions.CommandsScriptActions;
import me.nahkd.spigot.api.scripting.actions.ConditionScriptActions;
import me.nahkd.spigot.api.scripting.actions.PlayerScriptActions;
import me.nahkd.spigot.api.scripting.actions.VariableScriptActions;
import me.nahkd.spigot.api.timings.TimingsReport;

public class nahkdAPI {
	
	public static void initNoSpigot() {
		System.out.println("[nahkdAPI] Registering text animations");
		TextTypingAnimation typing = new TextTypingAnimation(20, 15, 20, 5);
		typing.register();
		TextColorsAnimation colors = new TextColorsAnimation(2);
		colors.register();
		TextRainbowAnimation rainbow = new TextRainbowAnimation();
		rainbow.register();

		PlaceholderManager.register(new PlayerPlaceholders());
		PlaceholderManager.register(new ProgressbarPlaceholders());
		PlaceholderManager.register(new CalendarPlaceholders());
		PlaceholderManager.register(new VariablePlaceholders());

		// Scripting - Load actions
		new PlayerScriptActions();
		new ConditionScriptActions();
		new CommandsScriptActions();
		new VariableScriptActions();
	}
	
	// Spigot section
	public static Server server;
	public static File dataFolder;
	public static ConsoleCommandSender console;
	public static FileConfiguration config;
	public static FileConfiguration variables;
	
	// Timings
	public static TimingsReport timingsReport;
	
	public static void initWithSpigot(JavaPlugin plugin) {
		System.out.println("[nahkdAPI] Loading other stuffs...");
		server = plugin.getServer();
		dataFolder = plugin.getDataFolder();
		console = server.getConsoleSender();
		config = plugin.getConfig();
		
		timingsReport = null;
		
		// Debug.init();
		Debug.initDebug();
	}
	
	// Files and folders section
	public static File scoreboards;
	public static File configFile;
	public static File variablesFile;
	
	public static void initFiles(File dataFolder) {
		scoreboards = new File(dataFolder, "scoreboards");
		configFile = new File(dataFolder, "config.yml");
		variablesFile = new File(dataFolder, "variables.yml");
		
		variables = YamlConfiguration.loadConfiguration(variablesFile);
	}
	
	public static void saveVariables() {
		try {
			variables.save(variablesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
