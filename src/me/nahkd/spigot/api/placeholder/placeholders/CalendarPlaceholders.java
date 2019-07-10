package me.nahkd.spigot.api.placeholder.placeholders;

import java.util.Calendar;

import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;

public class CalendarPlaceholders extends PlaceholderListener {

	public CalendarPlaceholders() {
		super("calendar");
	}

	@Override
	public String onCalled(Player player, String dataString) {
		Calendar current = Calendar.getInstance();
		     if (dataString.equalsIgnoreCase("current_year")) return current.get(Calendar.YEAR) + "";
		else if (dataString.equalsIgnoreCase("current_month")) return current.get(Calendar. MONTH) + "";
		else if (dataString.equalsIgnoreCase("current_day")) return current.get(Calendar.DATE) + "";
		return "";
	}

}
