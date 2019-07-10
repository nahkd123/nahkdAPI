package me.nahkd.spigot.api.scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.animation.TextAnimation;
import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;

public class ScoreboardEntry implements Cloneable {
	
	public ArrayList<ScoreboardEntry> children;
	public String input;
	public ArrayList<TextAnimation> animations;
	public EntryType type;
	
	public ScoreboardEntry(EntryType type, String input) {
		this.children = new ArrayList<ScoreboardEntry>();
		this.animations = new ArrayList<TextAnimation>();
		this.input = input;
		this.type = type;
	}
	
	public enum EntryType {
		TEXT("< No Text Avaliable >"),
		ANIMATED("Animation Test"),
		EMPTY("");
		
		public String defaultPlaceholder;
		
		private EntryType(String defaultPlaceholder) {
			this.defaultPlaceholder = defaultPlaceholder;
		}
	}
	
	public String toDisplayString(Player player) {
		String out = input;
		out = PlaceholderManager.parse(out, player);
		for (TextAnimation anim : this.animations) {
			out = anim.getString(out);
		}
		return out;
	}
	
	@Override
	public ScoreboardEntry clone() {
		ScoreboardEntry cloned = new ScoreboardEntry(this.type, this.input);
		cloned.animations.addAll(this.animations);
		cloned.children.addAll(this.children);
		return cloned;
	}
	
	public static ScoreboardEntry fromConfig(ConfigurationSection config) {
		ScoreboardEntry entry = new ScoreboardEntry(EntryType.valueOf(config.getString("type", "text").toUpperCase()), config.getString("input", "Missing input field"));
		if (config.contains("children")) {
			for (String key : config.getConfigurationSection("children").getKeys(false)) {
				ScoreboardEntry children = ScoreboardEntry.fromConfig(config.getConfigurationSection("children." + key));
				entry.children.add(children);
			}
		}
		if (entry.type == EntryType.ANIMATED) {
			// Animated
			try {
				for (String dat : config.getStringList("animations")) {
					TextAnimation anim = TextAnimation.getAnimations().get(dat.toLowerCase());
					if (anim != null) entry.animations.add(anim);
					else Debug.out("§cAnimation not found: §b" + dat);
				}
			} catch (NullPointerException e) {
				Debug.out("§cNullPointerException occured, most likely the configuration file is missing 'animations' key");
				Debug.out("§bat §3" + config.getCurrentPath() + ".animation §bin §3onfiguration file");
			}
		}
		return entry;
	}
	
	public static String toScoreboardLine(ArrayList<ScoreboardEntry> line, int lineNum, Player player) {
		String lineNumFormated = new DecimalFormat("00").format(lineNum);
		String out = "§" + lineNumFormated.charAt(0) + "§" + lineNumFormated.charAt(1) + "§r";
		for (ScoreboardEntry entry : line) {
			if (entry.type != EntryType.EMPTY) out += entry.toDisplayString(player);
		}
		return out;
	}
	
}
