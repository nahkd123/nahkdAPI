package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.nahkd.spigot.api.scripting.ScriptActionGroup;
import net.milkbowl.vault.economy.Economy;

public class PluginVaultScriptActions extends ScriptActionGroup {

	public PluginVaultScriptActions() {
		super("Vault");
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		if (target instanceof Player || target instanceof HumanEntity) {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
			Economy economy = rsp.getProvider();
			Player player = Bukkit.getPlayer(target.getName());
			
			switch (type) {
			case "Add":
			case "Give": economy.depositPlayer(player, actionInput.getDouble("amount", 1.0)); break;
			case "Remove":
			case "Take": economy.withdrawPlayer(player, actionInput.getDouble("amount", 1.0)); break;
			default: break;
			}
			economy = null;
			rsp = null;
		}
	}
}