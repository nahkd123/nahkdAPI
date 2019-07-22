package me.nahkd.spigot.api.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SelectorUtils {
	
	public static ArrayList<Entity> getEntities(Location location, double xrange, double yrange, double zrange) {
		return new ArrayList<Entity>(location.getWorld().getNearbyEntities(location, xrange, yrange, zrange));
	}
	
}
