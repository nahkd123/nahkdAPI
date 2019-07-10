package me.nahkd.spigot.apimain;

import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.nahkd.spigot.api.animation.TextAnimation;
import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.placeholder.placeholders.PluginVaultPlaceholders;
import me.nahkd.spigot.api.scoreboard.DisplayScoreboard;
import me.nahkd.spigot.api.scoreboard.PerPlayerScoreboard;
import me.nahkd.spigot.api.scripting.actions.PluginVaultScriptActions;
import me.nahkd.spigot.api.timings.TickingTimingsEntry;
import me.nahkd.spigot.apimain.commands.MainCommand;
import me.nahkd.spigot.apimain.commands.TimingsCommand;
import me.nahkd.spigot.apimain.commands.VariablesCommand;

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
			this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (PerPlayerScoreboard.getMap().size() > 0) for (Player player : PerPlayerScoreboard.getMap().keySet()) {
						PerPlayerScoreboard sco = PerPlayerScoreboard.getMap().get(player);
						sco.resetAll();
					}
					TextAnimation.tick();
				}
			}, 0, getConfig().getInt("scoreboard.ticking", 1));
		} else nahkdAPI.console.sendMessage("Scoreboard has been disabled for performance. To enable it, go to main configuration file, and change §3scoreboard > enabled §rto §btrue");
		
		// Commands
		nahkdAPIPlugin.instance.getCommand("timings2").setExecutor(new TimingsCommand());
		nahkdAPIPlugin.instance.getCommand("nahkdapi").setExecutor(new MainCommand());
		nahkdAPIPlugin.instance.getCommand("variable").setExecutor(new VariablesCommand());
		
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
	}
	
	@Override
	public void onDisable() {
		if (getConfig().getBoolean("scoreboard.enabled", false)) {
			if (PerPlayerScoreboard.getMap().size() > 0) PerPlayerScoreboard.hideAllPlayers();
		}
		nahkdAPI.timingsReport = null;
		nahkdAPI.scoreboards = null;
		
		// Save
		nahkdAPI.saveVariables();
		Debug.out("Plugin disabled");
	}
	
}
