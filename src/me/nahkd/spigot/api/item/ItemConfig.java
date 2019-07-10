package me.nahkd.spigot.api.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;

public class ItemConfig {
	
	public static ItemStack getFromConfig(ConfigurationSection section) {
		ItemStack output = new ItemStack(
				Material.valueOf(section.getString("material", "PAPER").toUpperCase()),
				section.getInt("amount", 1),
				(short) section.getInt("datavalue", 0)
				);
		if (section.contains("meta")) {
			ItemMeta meta = output.getItemMeta();
			if (section.contains("meta.localizedName")) meta.setLocalizedName(section.getString("meta.localizedName"));
			if (section.contains("meta.name")) meta.setLocalizedName(section.getString("meta.name"));
			if (section.contains("meta.lore")) meta.setLore(section.getStringList("meta.lore"));
			output.setItemMeta(meta);
			if (section.contains("meta.glow")) {
				switch (section.getString("meta.glow", "false")) {
				case "true":
				case "yes":
				case "1": {
					meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					break;
				}
				default: break;
				}
			}
		}
		return output;
	}
	
	public static ItemStack getFromConfigWithPlaceholder(ConfigurationSection section, Player player) {
		ItemStack output = new ItemStack(
				Material.valueOf(PlaceholderManager.parse(section.getString("material", "PAPER"), player, section).toUpperCase()),
				section.getInt("amount", 1),
				(short) section.getInt("datavalue", 0)
				);
		if (section.contains("meta")) {
			ItemMeta meta = output.getItemMeta();
			if (section.contains("meta.localizedName")) meta.setLocalizedName(PlaceholderManager.parse(section.getString("meta.localizedName"), player));
			if (section.contains("meta.name")) meta.setLocalizedName(PlaceholderManager.parse(section.getString("meta.name"), player));
			if (section.contains("meta.lore")) {
				List<String> loreOld = section.getStringList("meta.lore");
				ArrayList<String> lore = new ArrayList<String>();
				for (String l : loreOld) lore.add(PlaceholderManager.parse(l, player));
				meta.setLore(lore);
			}
			if (section.contains("meta.glow")) {
				switch (section.getString("meta.glow", "false")) {
				case "true":
				case "yes":
				case "1": {
					meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					break;
				}
				default: break;
				}
			}
			output.setItemMeta(meta);
		}
		return output;
	}
	
}
