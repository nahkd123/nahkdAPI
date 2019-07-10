package me.nahkd.spigot.api.scoreboard;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Scoreboard for everyone bruh
 * @author nahkd123
 *
 */
public class DisplayScoreboard {
	
	public ArrayList<ArrayList<ScoreboardEntry>> entries;
	public ArrayList<ScoreboardEntry> title;
	
	public DisplayScoreboard() {
		this.title = new ArrayList<ScoreboardEntry>();
		this.entries = new ArrayList<ArrayList<ScoreboardEntry>>();
	}
	
	public static DisplayScoreboard fromConfig(ConfigurationSection config) {
		DisplayScoreboard scoreboard = new DisplayScoreboard();
		for (String key : config.getConfigurationSection("title").getKeys(false)) {
			scoreboard.title.add(ScoreboardEntry.fromConfig(config.getConfigurationSection("title." + key)));
		}
		for (String key : config.getConfigurationSection("content").getKeys(false)) {
			ArrayList<ScoreboardEntry> line = new ArrayList<ScoreboardEntry>();
			for (String key2 : config.getConfigurationSection("content." + key).getKeys(false)) {
				line.add(ScoreboardEntry.fromConfig(config.getConfigurationSection("content." + key + "." + key2)));
			}
			scoreboard.entries.add(line);
		}
		return scoreboard;
	}
	
}
