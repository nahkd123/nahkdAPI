package me.nahkd.spigot.api.placeholder;

import org.bukkit.entity.Player;

public abstract class PlaceholderListener {
	
	// Lookup name: Something like %listener_lookup_name:data_string%
	public String lookupName;
	
	public PlaceholderListener(String lookupName) {
		this.lookupName = lookupName;
	}
	
	public abstract String onCalled(Player player, String dataString);
	
}
