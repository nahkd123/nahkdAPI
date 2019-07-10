package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.nahkd.spigot.api.item.ItemConfig;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class PlayerScriptActions extends ScriptActionGroup {

	public PlayerScriptActions() {
		super("Player");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		if (target instanceof Player || target instanceof HumanEntity) {
			Player player = Bukkit.getPlayer(target.getName());
			switch (type) {
			case "Message": {
				player.sendMessage(PlaceholderManager.parse(actionInput.getString("message", "§7§o< no message >"), player, data));
				break;
			}
			case "AddItem": {
				ItemStack result = ItemConfig.getFromConfigWithPlaceholder(actionInput, player);
				player.getInventory().addItem(result);
				break;
			}
			default: break;
			}
		}
	}

}
