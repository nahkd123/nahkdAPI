package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class CommandsScriptActions extends ScriptActionGroup {

	public CommandsScriptActions() {
		super("Commands");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		String command = actionInput.getString("command", "say Missing `command` entry");
		if (target instanceof Player || target instanceof HumanEntity) {
			Player player = Bukkit.getPlayer(target.getName());
			command = PlaceholderManager.parse(command, player, data);
		} else command = PlaceholderManager.parse(command, data);
		switch (type) {
		case "Console": Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); break;
		case "Player": {
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				Bukkit.dispatchCommand(player, command);
			} else System.out.println("[nahkdAPI](Scripting) The entity involed in action isn't Player or HumanEntity");
			break;
		}
		default: break;
		}
	}

}
