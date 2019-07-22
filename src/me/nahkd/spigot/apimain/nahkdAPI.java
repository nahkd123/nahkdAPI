package me.nahkd.spigot.apimain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.nahkd.spigot.api.animation.animations.TextColorsAnimation;
import me.nahkd.spigot.api.animation.animations.TextRainbowAnimation;
import me.nahkd.spigot.api.animation.animations.TextTypingAnimation;
import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.multithreads.RepeatingThreadsManager;
import me.nahkd.spigot.api.multithreads.ScheduledTask;
import me.nahkd.spigot.api.multithreads.TaskWorker;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.placeholder.placeholders.CalendarPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.MathPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.PlayerPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.ProgressbarPlaceholders;
import me.nahkd.spigot.api.placeholder.placeholders.VariablePlaceholders;
import me.nahkd.spigot.api.scripting.Script;
import me.nahkd.spigot.api.scripting.ScriptPack;
import me.nahkd.spigot.api.scripting.ScriptPackCommand;
import me.nahkd.spigot.api.scripting.actions.CommandsScriptActions;
import me.nahkd.spigot.api.scripting.actions.ConditionScriptActions;
import me.nahkd.spigot.api.scripting.actions.EntityScriptActions;
import me.nahkd.spigot.api.scripting.actions.LoopsScriptActions;
import me.nahkd.spigot.api.scripting.actions.PlayerScriptActions;
import me.nahkd.spigot.api.scripting.actions.ScriptingActions;
import me.nahkd.spigot.api.scripting.actions.VariableScriptActions;
import me.nahkd.spigot.api.timings.TimingsReport;
import me.nahkd.spigot.api.updater.PluginUpdater;
import me.nahkd.spigot.api.updater.PluginUpdaterInfo;
import me.nahkd.spigot.api.updater.UpdaterParameter;
import me.nahkd.spigot.api.updater.drivers.MCVNUpdaterDriver;

public class nahkdAPI {
	
	/**
	 * The update information for nahkdAPI
	 */
	public static PluginUpdaterInfo _info;
	public static ArrayList<PluginUpdaterInfo> pluginInfos;
	public static ArrayList<String> avaliableUpdates;
	
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
		PlaceholderManager.register(new MathPlaceholders());

		// Scripting - Load actions
		new PlayerScriptActions();
		new ConditionScriptActions();
		new CommandsScriptActions();
		new VariableScriptActions();
		
		new ScriptingActions();
		new LoopsScriptActions();
		new EntityScriptActions();
		
		// Plugin Updater Drivers
		PluginUpdater.addDriver("mcvn", new MCVNUpdaterDriver());
		
		// Plugin info
		pluginInfos = new ArrayList<PluginUpdaterInfo>();
		avaliableUpdates = new ArrayList<String>();
	}
	
	// Spigot section
	public static Server server;
	public static File dataFolder;
	public static ConsoleCommandSender console;
	public static FileConfiguration config;
	public static FileConfiguration variables;
	
	// Timings
	public static TimingsReport timingsReport;
	
	// Multithreading
	public static RepeatingThreadsManager threadsManager;
	public static ArrayList<TaskWorker> taskWorker;
	public static ArrayList<Script> loadedScripts;
	public static ArrayList<ScriptPack> loadedScriptsPacks;
	
	public static void initWithSpigot(JavaPlugin plugin) {
		System.out.println("[nahkdAPI] Loading other stuffs...");
		server = plugin.getServer();
		dataFolder = plugin.getDataFolder();
		console = server.getConsoleSender();
		config = plugin.getConfig();
		
		timingsReport = null;
		threadsManager = new RepeatingThreadsManager();
		
		// Debug.init();
		Debug.initDebug();
		
		// Add nahkdAPI
		_info = new PluginUpdaterInfo("nahkdAPI", "1.0.2");
		_info.addParameter(
				new UpdaterParameter("mcvn-update", "true"),
				new UpdaterParameter("mcvn-id", "1801")
		);
		pluginInfos.add(_info);
	}
	
	// Files and folders section
	public static File scoreboards;
	public static File configFile;
	public static File variablesFile;
	public static File scripts;
	public static File scriptPacks;
	
	public static void initFiles(File dataFolder) {
		scoreboards = new File(dataFolder, "scoreboards");
		scripts = new File(dataFolder, "scripts");
		scriptPacks = new File(scripts, "packs");
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
	
	/**
	 * @deprecated Load file when run script instead
	 */
	public static void loadScripts() {
		loadedScripts = new ArrayList<Script>();
		for (File f : scripts.listFiles()) {
			if (!f.isDirectory()) {
				YamlConfiguration scriptRaw = YamlConfiguration.loadConfiguration(f);
				Script scr = new Script(scriptRaw);
				loadedScripts.add(scr);
				Debug.out("§aLoaded script: " + f.getName());
			}
		}
	}
	
	public static void loadScriptsPacks() {
		loadedScriptsPacks = new ArrayList<ScriptPack>();
		for (File f : scriptPacks.listFiles()) {
			if (f.isDirectory()) {
				YamlConfiguration packInfo = YamlConfiguration.loadConfiguration(new File(f, "_info.yml"));
				ScriptPack pack = new ScriptPack(f.getName(), packInfo.getString("displayname", "§7§oNo display name"), packInfo.getString("author", "§7§oNo author"), packInfo.getString("version", "§7§oNo version info"));
				// Events
				if (packInfo.contains("eventslisteners")) for (String eventName : packInfo.getConfigurationSection("eventslisteners").getKeys(false)) {
					ArrayList<Script> eventScripts = new ArrayList<Script>();
					for (String scriptName : packInfo.getStringList("eventslisteners." + eventName)) {
						Script subScript = new Script(YamlConfiguration.loadConfiguration(new File(f, scriptName)));
						eventScripts.add(subScript);
					}
					pack.eventsListeners.put(eventName, eventScripts);
				}
				
				// Commands
				if (packInfo.contains("commands")) for (String command : packInfo.getConfigurationSection("commands").getKeys(false)) {
					String scriptName = packInfo.getString("commands." + command + ".script", "default.yml");
					String permission = packInfo.getString("commands." + command + ".permission", "defaultpermission");
					String usage = packInfo.getString("commands." + command + ".usage", "/" + command);
					String denyMessage = packInfo.getString("commands." + command + ".denymessage", "§cYou can use this command");
					Script subScript = new Script(YamlConfiguration.loadConfiguration(new File(f, scriptName)));
					ScriptPackCommand cmd = new ScriptPackCommand(subScript, permission, usage, denyMessage);
					pack.commands.put(command, cmd);
				}
				loadedScriptsPacks.add(pack);
				Debug.out("§aLoaded scriptpack: " + f.getName());
			}
		}
	}
	
	public static ArrayList<TaskWorker> initTaskWorkers(int limit) {
		ArrayList<TaskWorker> workers = new ArrayList<TaskWorker>();
		for (int i = 0; i < limit; i++) {
			TaskWorker worker = new TaskWorker();
			threadsManager.startThread(worker);
			workers.add(worker);
		}
		taskWorker = workers;
		return workers;
	}
	
	/**
	 * Schedule task
	 * @param task The task to schedule
	 */
	public static void scheduleTask(ScheduledTask task) {
		TaskWorker targetedWorker = null;
		int minTasks = Integer.MAX_VALUE;
		for (int i = 0; i < taskWorker.size(); i++) {
			TaskWorker worker = taskWorker.get(i);
			if (worker.tasks.size() < minTasks) {
				targetedWorker = worker;
				minTasks = worker.tasks.size();
			}
		}
		// Add task to most idle worker
		if (targetedWorker == null) {
			Debug.out("§cSomething gone wrong that targetedWorker is null. Append task to first worker...");
			taskWorker.get(0).tasks.add(task);
		} else targetedWorker.tasks.add(task);
	}
	
}
