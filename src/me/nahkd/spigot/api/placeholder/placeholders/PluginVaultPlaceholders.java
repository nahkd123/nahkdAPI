package me.nahkd.spigot.api.placeholder.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import net.milkbowl.vault.economy.Economy;

public class PluginVaultPlaceholders extends PlaceholderListener {

	public PluginVaultPlaceholders() {
		super("vault");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		Economy economy = rsp.getProvider();
		
		switch (dataString) {
		case "money":
		case "balance":
		case "currentmoney": {
			double bal = economy.getBalance(player);
			economy = null;
			rsp = null;
			return bal + "";
		}
		default: break;
		}
		return "";
	}

}
