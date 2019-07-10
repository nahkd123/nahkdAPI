package me.nahkd.spigot.api.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PerPlayerScoreboard {
	
	public Player player;
	public Scoreboard playerScoreboard;
	public Objective playerObj;
	public DisplayScoreboard ref;
	
	public boolean bufAlt;
	
	public PerPlayerScoreboard(Player player, DisplayScoreboard ref) {
		this.player = player;
		this.ref = ref;
		this.playerScoreboard = null;
		
		this.bufAlt = false;
	}
	public void initScoreboard() {
		playerScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		if (!bufAlt) playerObj = playerScoreboard.registerNewObjective("boardcontent1", "dummy");
		else playerObj = playerScoreboard.registerNewObjective("boardcontent2", "dummy");
		playerObj.setDisplaySlot(DisplaySlot.SIDEBAR);
		playerObj.setDisplayName(ScoreboardEntry.toScoreboardLine(ref.title, 0, player));
		player.setScoreboard(playerScoreboard);
	}
	public void resetAll() {
		Objective altObj = null;
		if (!bufAlt) altObj = playerScoreboard.registerNewObjective("boardcontent2", "dummy");
		else altObj = playerScoreboard.registerNewObjective("boardcontent1", "dummy");
		altObj.setDisplayName(ScoreboardEntry.toScoreboardLine(ref.title, 0, player));
		altObj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (int i = 15; i >= 0; i--) {
			if (15 - i >= ref.entries.size()) break;
			Score score = altObj.getScore(ScoreboardEntry.toScoreboardLine(ref.entries.get(15 - i), i, player));
			score.setScore(i);
		}
		playerObj.setDisplayName(ScoreboardEntry.toScoreboardLine(ref.title, 0, player));
		playerObj.unregister();
		playerObj = altObj;
		bufAlt = !bufAlt;
	}
	
	private static HashMap<Player, PerPlayerScoreboard> scoreboards;
	public static HashMap<Player, PerPlayerScoreboard> getMap() {
		if (scoreboards == null) scoreboards = new HashMap<Player, PerPlayerScoreboard>();
		return scoreboards;
	}
	
	public static void hideAllPlayers() {
		for (Player player : getMap().keySet()) {
			PerPlayerScoreboard scoreboard = getMap().get(player);
			if (scoreboard.playerScoreboard != null && player.getScoreboard() != null) {
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
}
