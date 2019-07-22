package me.nahkd.spigot.api.placeholder;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.debugger.Debug;

public class PlaceholderManager {
	
	private static HashMap<String, PlaceholderListener> listeners;
	public static HashMap<String, PlaceholderListener> getRegistered() {
		if (listeners == null) listeners = new HashMap<String, PlaceholderListener>();
		return listeners;
	}
	
	public static void register(PlaceholderListener listener) {
		getRegistered().put(listener.lookupName, listener);
	}
	
	public static String parse(String input, Player player) {
		String out = "";
		String lookup = "";
		String data = "";
		int mode = 0;
		/*
		 * 0 - Still waiting for placeholder activation char
		 * 1 - Getting lookup name
		 * 2 - Getting placeholder data
		 * 0 - Once it found the placeholder, it will come back to 0
		 * 
		 * 3, 4, 5 - Escape
		 */
		for (char c : input.toCharArray()) {
			if (mode == 0) {
				if (c == '\\') mode = 3;
				else if (c == '%') mode = 1;
				else out += c;
			} else if (mode == 1) {
				if (c == ':') mode = 2;
				else if (c == '\\') mode = 4;
				else if (c == '%') {
					mode = 0;
					// Find placeholder listener
					PlaceholderListener listener = getRegistered().get(lookup);
					if (listener != null) listener.onCalled(player, data);
					else
						try {throw new Exception("Placeholder not found: " + lookup);}
					catch (Exception e) {
						e.printStackTrace();
						Debug.out("§cPlaceholder not found: §b" + lookup);
						return "§cInternal Error";
					}
					// Clear all
					lookup = "";
					data = "";
				} else lookup += c;
			} else if (mode == 2) {
				if (c == '\\') mode = 5;
				else if (c == '%') {
					mode = 0;
					// Find placeholder listener
					PlaceholderListener listener = getRegistered().get(lookup);
					if (listener != null) {
						out += listener.onCalled(player, data);
					} else
						try {
							throw new Exception("Placeholder not found: " + lookup);
						} catch (Exception e) {
							e.printStackTrace();
							Debug.out("§cPlaceholder not found: §b" + lookup);
							return "§cInternal Error";
						}
					// Clear all
					lookup = "";
					data = "";
				} else data += c;
			} else if (mode == 3) {
				out += c;
				mode = 0;
			} else if (mode == 4) {
				lookup += c;
				mode = 1;
			} else if (mode == 5) {
				data += c;
				mode = 2;
			}
		}
		
		// Out of incoming chars
		if (mode >= 1) out += "%" + lookup;
		if (mode >= 2) out += "%" + lookup + ":" + data;
		return out;
	}
	
	public static String parse(String input, ConfigurationSection configRef) {
		String out = "";
		int mode = 0;
		String configPath = "";
		for (char c : input.toCharArray()) {
			if (mode == 0) {
				if (c == '^') mode = 1;
				else if (c == '$') mode = 2;
				else out += c;
			} else if (mode == 1) {
				out += c;
				mode = 0;
			} else if (mode == 2) {
				if (c == '[') mode = 3;
				else {
					out += "$" + c;
					mode = 0;
				}
			} else if (mode == 3) {
				if (c == '^') mode = 4;
				else if (c == ']') {
					out += configRef.get(configPath, "Undefined").toString();
					mode = 0;
				} else configPath += c;
			}
		}
		return out;
	}

	public static String parse(String input, Player player, ConfigurationSection configRef) {
		String out = parse(input, configRef);
		out = parse(out, player);
		return out;
	}
	
	public static String parseWithEntity(String input, Entity entity, ConfigurationSection configRef) {
		if (entity instanceof Player || entity instanceof HumanEntity) {
			return parse(input, Bukkit.getPlayer(input), configRef);
		} else return parse(input, configRef);
	}
	
}
