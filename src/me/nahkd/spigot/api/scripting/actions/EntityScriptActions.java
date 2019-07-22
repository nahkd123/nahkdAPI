package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class EntityScriptActions extends ScriptActionGroup {

	public EntityScriptActions() {
		super("Entity");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		switch (type) {
		case "Set Vector": {
			double x = Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("x", target.getLocation().getX() + ""), target, data));
			double y = Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("y", target.getLocation().getY() + ""), target, data));
			double z = Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("z", target.getLocation().getZ() + ""), target, data));
			target.setVelocity(new Vector(x, y, z));
		}
		default: break;
		}
	}

}
