package me.nahkd.spigot.apimain.listeners;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.scoreboard.DisplayScoreboard;
import me.nahkd.spigot.api.scoreboard.PerPlayerScoreboard;
import me.nahkd.spigot.apimain.nahkdAPI;

public class PlayerLoggingListener implements Listener {
	
	@EventHandler
	public void login(PlayerLoginEvent event) {
		if (nahkdAPI.config.getBoolean("scoreboard.enabled", false)) {
				PerPlayerScoreboard.getMap().put(
					event.getPlayer(),
					new PerPlayerScoreboard(event.getPlayer(), DisplayScoreboard.fromConfig(YamlConfiguration.loadConfiguration(new File(nahkdAPI.scoreboards, nahkdAPI.config.getString("scoreboard.default", "scoreboard.yml")))))
					);
			Debug.out("Added scoreboard for player " + event.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void logout(PlayerQuitEvent event) {
		if (nahkdAPI.config.getBoolean("scoreboard.enabled", false)) {
			PerPlayerScoreboard.getMap().put(event.getPlayer(), null);
			Debug.out("Removed scoreboard for player " + event.getPlayer().getName());
		}
	}
	
}
