package me.nahkd.spigot.api.placeholder.placeholders;

import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;

public class PlayerPlaceholders extends PlaceholderListener {

	public PlayerPlaceholders() {
		super("player");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		if (dataString.equalsIgnoreCase("")) return "§cInvaild";
		else if (dataString.equalsIgnoreCase("name")) return player.getName();
		else if (dataString.equalsIgnoreCase("display_name")) return player.getDisplayName();
		else if (dataString.equalsIgnoreCase("custom_name")) return player.getCustomName();
		else if (dataString.equalsIgnoreCase("ip")) return player.getAddress().getAddress().getHostAddress();
		else if (dataString.equalsIgnoreCase("exp_level")) return player.getLevel() + "";
		else if (dataString.equalsIgnoreCase("exp_progress")) return player.getExp() + "";
		else if (dataString.equalsIgnoreCase("gamemode")) return player.getGameMode() + "";
		else if (dataString.startsWith("exp_progress(")) {
			int maxExp = Integer.parseInt(dataString.substring(13).replace(")", ""));
			long disp = Math.round(((double) player.getExp()) * maxExp);
			return disp + "";
		}
		return "§cUnknown";
	}

}
