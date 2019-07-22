package me.nahkd.spigot.apimain.listeners;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.scoreboard.DisplayScoreboard;
import me.nahkd.spigot.api.scoreboard.PerPlayerScoreboard;
import me.nahkd.spigot.apimain.nahkdAPI;

public class PlayerLoggingListener implements Listener {
	
	@EventHandler
	public void login(PlayerJoinEvent event) {
		if (nahkdAPI.config.getBoolean("scoreboard.enabled", false)) {
			PerPlayerScoreboard scoreboard = new PerPlayerScoreboard(event.getPlayer(), DisplayScoreboard.fromConfig(YamlConfiguration.loadConfiguration(new File(nahkdAPI.scoreboards, nahkdAPI.config.getString("scoreboard.default", "scoreboard.yml")))));
				PerPlayerScoreboard.getMap().put(
					event.getPlayer(),
					scoreboard
					);
			
			Debug.out("§bInit scoreboard...");
			scoreboard.initScoreboard();
			event.getPlayer().setScoreboard(scoreboard.playerScoreboard);
			event.getPlayer().sendMessage("aa");
			Debug.out("Added scoreboard for player " + event.getPlayer().getName());
		}
		
		if (event.getPlayer().isOp() && nahkdAPI.avaliableUpdates.size() > 0) {
			for (String pl : nahkdAPI.avaliableUpdates) event.getPlayer().sendMessage("§bAn new update for §a" + pl + " §bis avaliable. Check console for more info.");
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
