package me.nahkd.spigot.apimain;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.nahkd.spigot.api.animation.TextAnimation;
import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.multithreads.RepeatingThread;
import me.nahkd.spigot.api.multithreads.ScheduledTask;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.placeholder.placeholders.PluginVaultPlaceholders;
import me.nahkd.spigot.api.scoreboard.DisplayScoreboard;
import me.nahkd.spigot.api.scoreboard.PerPlayerScoreboard;
import me.nahkd.spigot.api.scripting.ScriptPack;
import me.nahkd.spigot.api.scripting.ScriptPackCommand;
import me.nahkd.spigot.api.scripting.actions.PluginVaultScriptActions;
import me.nahkd.spigot.api.timings.TickingTimingsEntry;
import me.nahkd.spigot.api.updater.PluginUpdater;
import me.nahkd.spigot.api.updater.PluginUpdaterInfo;
import me.nahkd.spigot.apimain.commands.MainCommand;
import me.nahkd.spigot.apimain.commands.ScriptingCommand;
import me.nahkd.spigot.apimain.commands.TimingsCommand;
import me.nahkd.spigot.apimain.commands.VariablesCommand;
import me.nahkd.spigot.apimain.listeners.PlayerLoggingListener;

public class nahkdAPIPlugin extends JavaPlugin {
	
	public static nahkdAPIPlugin instance;
	
	@Override
	public void onLoad() {
		// Instance prepare
		instance = this;
		
		nahkdAPI.initNoSpigot();
		nahkdAPI.initWithSpigot(this);
		
		// Load up default files
		nahkdAPI.initFiles(nahkdAPI.dataFolder);
		if (!nahkdAPI.dataFolder.exists()) {
			Debug.out("Creating default configurations...");
			nahkdAPI.dataFolder.mkdir();
			InputStream defaultConfig = this.getClassLoader().getResourceAsStream("default_config.yml");
			try {
				FileOutputStream outputConfig = new FileOutputStream(nahkdAPI.configFile);
				int fileSize = defaultConfig.available();
				byte[] content = new byte[fileSize];
				defaultConfig.read(content, 0, fileSize);
				outputConfig.write(content, 0, fileSize);
				outputConfig.close();
				
				getConfig().load(nahkdAPI.configFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		if (!nahkdAPI.scoreboards.exists()) {
			Debug.out("Creating default scoreboard...");
			nahkdAPI.scoreboards.mkdir();
			InputStream defaultConfig = this.getClassLoader().getResourceAsStream("default_scoreboard.yml");
			try {
				FileOutputStream outputConfig = new FileOutputStream(new File(nahkdAPI.scoreboards, "scoreboard.yml"));
				int fileSize = defaultConfig.available();
				byte[] content = new byte[fileSize];
				defaultConfig.read(content, 0, fileSize);
				outputConfig.write(content, 0, fileSize);
				outputConfig.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!nahkdAPI.scripts.exists()) {
			Debug.out("Creating default script...");
			nahkdAPI.scripts.mkdir();
			nahkdAPI.scriptPacks.mkdir();
			InputStream defaultConfig = this.getClassLoader().getResourceAsStream("default_script.yml");
			try {
				FileOutputStream outputConfig = new FileOutputStream(new File(nahkdAPI.scripts, "welcome.yml"));
				int fileSize = defaultConfig.available();
				byte[] content = new byte[fileSize];
				defaultConfig.read(content, 0, fileSize);
				outputConfig.write(content, 0, fileSize);
				outputConfig.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onEnable() {
		// Some plugin may avaliable when it's enabled instead of loaded
		// Other plugins
		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			Debug.out("§aPlugin found: Vault");
			PlaceholderManager.register(new PluginVaultPlaceholders());
			new PluginVaultScriptActions().register();
		}
		
		// Events
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoggingListener(), this);
		
		// Task Worker
		Debug.out("§aAdding " + getConfig().getInt("multithreads.taskworkers.limit", 5) + " task workers...");
		nahkdAPI.initTaskWorkers(getConfig().getInt("multithreads.taskworkers.limit", 5));
		
		// Check if scoreboard is enabled or not
		if (getConfig().getBoolean("scoreboard.enabled", false)) {
			// Get default scoreboard
			YamlConfiguration defaultScoreboardConfig = YamlConfiguration.loadConfiguration(new File(nahkdAPI.scoreboards, getConfig().getString("scoreboard.default", "scoreboard.yml")));
			DisplayScoreboard defaultScoreboard = DisplayScoreboard.fromConfig(defaultScoreboardConfig);
			
			// Display scoreboard to everyone
			if (this.getServer().getOnlinePlayers().size() > 0) for (Player player : this.getServer().getOnlinePlayers()) {
				PerPlayerScoreboard sco = new PerPlayerScoreboard(player, defaultScoreboard);
				sco.initScoreboard();
				PerPlayerScoreboard.getMap().put(player, sco);
			}
			Debug.out("Plugin enabled");

			// Scoreboard ticker
			/*this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (PerPlayerScoreboard.getMap().size() > 0) for (Player player : PerPlayerScoreboard.getMap().keySet()) {
						PerPlayerScoreboard sco = PerPlayerScoreboard.getMap().get(player);
						sco.resetAll();
					}
					TextAnimation.tick();
				}
			}, 0, getConfig().getInt("scoreboard.ticking", 1));*/
			RepeatingThread scoreboardThread = new RepeatingThread() {
				@Override
				public void onLoop() {
					if (PerPlayerScoreboard.getMap().size() > 0) for (Player player : PerPlayerScoreboard.getMap().keySet()) {
						PerPlayerScoreboard sco = PerPlayerScoreboard.getMap().get(player);
						sco.resetAll();
					}
					TextAnimation.tick();
				}
			};
			scoreboardThread.setName("Scoreboard Ticking");
			scoreboardThread.delay = getConfig().getInt("scoreboard.ticking", 1) - 1;
			nahkdAPI.threadsManager.startThread(scoreboardThread);
		} else nahkdAPI.console.sendMessage("Scoreboard has been disabled for performance. To enable it, go to main configuration file, and change §3scoreboard > enabled §rto §btrue");
		
		// Commands
		nahkdAPIPlugin.instance.getCommand("timings2").setExecutor(new TimingsCommand());
		nahkdAPIPlugin.instance.getCommand("nahkdapi").setExecutor(new MainCommand());
		nahkdAPIPlugin.instance.getCommand("variable").setExecutor(new VariablesCommand());
		nahkdAPIPlugin.instance.getCommand("script").setExecutor(new ScriptingCommand());
		
		// Timings
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if (nahkdAPI.timingsReport != null) {
					long currentMS = System.currentTimeMillis() - nahkdAPI.timingsReport.previousTimestamp;
					nahkdAPI.timingsReport.ms.add(new TickingTimingsEntry(
							nahkdAPI.timingsReport.startTime,
							currentMS,
							Bukkit.getOnlinePlayers().size()
					));
					nahkdAPI.timingsReport.previousTimestamp = System.currentTimeMillis();
				}
			}
		}, 0, 1);
		
		// Scripts
		//nahkdAPI.loadScripts();
		nahkdAPI.loadScriptsPacks();
		
		// Scripts Packs command registering
		nahkdAPI.scheduleTask(new ScheduledTask(1) {
			@Override
			public void taskRun() {
				if (nahkdAPI.loadedScriptsPacks.size() > 0) {
					Debug.out("§bPerforming reflection on a task worker...");
					Debug.out("§bTask workers are threads with pending tasks to solve");
					
					try {
						final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
						bukkitCommandMap.setAccessible(true);
						CommandMap map = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
						
						for (ScriptPack pack : nahkdAPI.loadedScriptsPacks) {
							for (Entry<String, ScriptPackCommand> entry : pack.commands.entrySet()) {
								entry.getValue().resultCommand = new Command(entry.getKey()) {
									@Override
									public boolean execute(CommandSender sender, String commandLabel, String[] args) {
										if (sender instanceof Entity) {
											ScriptPackCommand commandBase = entry.getValue();
											commandBase.runCommand((Entity) sender, args);
										} else sender.sendMessage("§cTargeted Command sender isn't Entity!");
										return true;
									}
								};
								map.register("napisp-" + pack.name, entry.getValue().resultCommand);
								Debug.out("§aRegistered command: " + entry.getKey() + " §3(" + pack.displayName + "§3)");
							}
						}
						
					} catch (NoSuchFieldException e) {
						Debug.out("§cNo field found while in reflection: commandMap");
						e.printStackTrace();
					} catch (SecurityException e) {
						Debug.out("§cSend this to developer:");
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						Debug.out("§cSend this to developer:");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						Debug.out("§cNo access, or field is protected");
						e.printStackTrace();
					}
				}
			}
		});
		
		// Updater
		nahkdAPI.scheduleTask(new ScheduledTask(5) {
			@Override
			public void taskRun() {
				Debug.out("§bLoading updater...");
				for (PluginUpdaterInfo pls : nahkdAPI.pluginInfos) {
					Debug.out("§bSearching update for " + pls.plugin + "...");
					boolean newUpdate = PluginUpdater.searchForUpdate(pls);
					if (!newUpdate) Debug.out("§bPlugin " + pls.plugin + " is up to date!");
					else {
						Debug.out("§aAn update for plugin " + pls.plugin + " is avaliable!");
						nahkdAPI.avaliableUpdates.add(pls.plugin);
						boolean downloadAvaliable = PluginUpdater.canDownload(pls);
						if (downloadAvaliable) {
							Debug.out("§aDownloading for plugin " + pls.plugin + "...");
							InputStream stream = PluginUpdater.download(pls);
							try {
								FileOutputStream output = new FileOutputStream(new File("plugins", pls.plugin + ".jar"));
								int fileSize = stream.available();
								byte[] content = new byte[fileSize];
								stream.read(content, 0, fileSize);
								output.write(content, 0, fileSize);
								output.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
								Debug.out("§cCouldn't not download plugin. Please go to website");
							}
						} else Debug.out("§aPlease go to website to download latest version.");
					}
				}
			}
		});
	}
	
	@Override
	public void onDisable() {
		
		// Scripts Packs command unregistering
		if (nahkdAPI.loadedScriptsPacks.size() > 0) {
			Debug.out("§bPerforming reflection on main thread...");
		
			try {
				final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				bukkitCommandMap.setAccessible(true);
				CommandMap map = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
	
				for (ScriptPack pack : nahkdAPI.loadedScriptsPacks) {
					for (Entry<String, ScriptPackCommand> entry : pack.commands.entrySet()) {
						entry.getValue().resultCommand.unregister(map);
						Debug.out("§aUnegistered command: " + entry.getKey() + " §3(" + pack.displayName + "§3)");
					}
				}
				
			} catch (NoSuchFieldException e) {
				Debug.out("§cNo field found while in reflection: commandMap");
				e.printStackTrace();
			} catch (SecurityException e) {
				Debug.out("§cSend this to developer:");
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				Debug.out("§cSend this to developer:");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				Debug.out("§cNo access, or field is protected");
				e.printStackTrace();
				}
			}
		
		if (getConfig().getBoolean("scoreboard.enabled", false)) {
			if (PerPlayerScoreboard.getMap().size() > 0) PerPlayerScoreboard.hideAllPlayers();	
		}
		nahkdAPI.timingsReport = null;
		nahkdAPI.scoreboards = null;
		
		// Save
		nahkdAPI.saveVariables();
		
		// Threads
		Debug.out("Stopping all threads...");
		nahkdAPI.threadsManager.stopAll();
		
		Debug.out("Plugin disabled");
	}
	
}
