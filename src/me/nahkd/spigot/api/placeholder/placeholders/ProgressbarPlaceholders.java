package me.nahkd.spigot.api.placeholder.placeholders;

import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;

public class ProgressbarPlaceholders extends PlaceholderListener {

	public ProgressbarPlaceholders() {
		super("progressbar");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		if (dataString.startsWith("0(")) {
			String subPlaceholder = dataString.substring(2, dataString.length() - 1);
			double progress = Double.parseDouble(PlaceholderManager.parse("%" + subPlaceholder + "%", player));
			
			// Preset:
			int chars = 16; // 16 segment
			int activated = (int) Math.round(progress * chars);
			int unactivated = chars - activated;
			String out = "§a";
			for (int i = 0; i < activated; i++) out += '|';
			out += "§7";
			for (int i = 0; i < unactivated; i++) out += '|';
			return out;
		}
		if (dataString.startsWith("1(")) {
			String subPlaceholder = dataString.substring(2, dataString.length() - 1);
			double progress = Double.parseDouble(PlaceholderManager.parse("%" + subPlaceholder + "%", player));
			
			// Preset:
			int chars = 16; // 16 segment
			int activated = (int) Math.round(progress * chars);
			int unactivated = chars - activated;
			String out = "§a";
			for (int i = 0; i < activated; i++) out += '=';
			out += "§7";
			for (int i = 0; i < unactivated; i++) out += '=';
			return out;
		}
		return "§cError";
	}

}
