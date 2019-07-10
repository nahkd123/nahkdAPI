package me.nahkd.spigot.api.placeholder.placeholders;

import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.apimain.nahkdAPI;

public class VariablePlaceholders extends PlaceholderListener {

	public VariablePlaceholders() {
		super("variable");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		if (dataString.startsWith("@")) return nahkdAPI.variables.get(PlaceholderManager.parse(dataString.substring(1), player), "null").toString();
		else if (dataString.startsWith("#")) return nahkdAPI.variables.contains(PlaceholderManager.parse(dataString.substring(1), player)) + "";
		else return "@: Value/#: Contains";
	}

}
