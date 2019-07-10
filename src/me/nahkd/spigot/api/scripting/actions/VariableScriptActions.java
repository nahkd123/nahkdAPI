package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;
import me.nahkd.spigot.apimain.nahkdAPI;

public class VariableScriptActions extends ScriptActionGroup {

	public VariableScriptActions() {
		super("Variable");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		String path = actionInput.getString("path", "default.test");
		if (target instanceof Player || target instanceof HumanEntity) {
			Player player = Bukkit.getPlayer(target.getName());
			path = PlaceholderManager.parse(path, player, data);
		} else path = PlaceholderManager.parse(path, data);
		switch (type) {
		case "Set String": {
			String output = actionInput.getString("value", "Undefined");
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				output = PlaceholderManager.parse(output, player, data);
			} else output = PlaceholderManager.parse(output, data);
			nahkdAPI.variables.set(path, output);
			break;
		}
		case "Set Number": {
			String output = actionInput.getString("value", "0.0");
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				output = PlaceholderManager.parse(output, player, data);
			} else output = PlaceholderManager.parse(output, data);
			nahkdAPI.variables.set(path, Double.parseDouble(output)); break;
		}
		case "Increase":
		case "Increases": {
			String output = actionInput.getString("value", "0.0");
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				output = PlaceholderManager.parse(output, player, data);
			} else output = PlaceholderManager.parse(output, data);
			nahkdAPI.variables.set(path, nahkdAPI.variables.getDouble(path, 0.0D) + Double.parseDouble(output)); break;
		}
		case "Remove": nahkdAPI.variables.set(path, null);
		case "Save": nahkdAPI.saveVariables();
		default: break;
		}
	}

}
