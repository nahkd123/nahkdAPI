package me.nahkd.spigot.api.scripting.actions;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.Script;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;
import me.nahkd.spigot.api.utils.SelectorUtils;

public class LoopsScriptActions extends ScriptActionGroup {

	public LoopsScriptActions() {
		super("Loop");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		switch (type) {
		case "For 1": {
			// For loop #1: The basic loop
			long startIndex = Math.round(Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("start", "0"), target, data)));
			long endIndex = Math.round(Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("end", "10"), target, data)));
			String localVariableName = PlaceholderManager.parseWithEntity(actionInput.getString("variable", "i"), target, data);
			Script internalScriptChild = new Script(actionInput.getConfigurationSection("children"));
			for (long i = startIndex; i < endIndex; i++) {
				ConfigurationSection d2 = Script.cloneData(data);
				d2.set(localVariableName, i + "");
				internalScriptChild.invoke(target, d2);
			}
			break;
		}
		case "Entities 1": {
			// Entities: Loop though entities
			String localVariableName = PlaceholderManager.parseWithEntity(actionInput.getString("variable", "i"), target, data);
			Location loc = new Location(target.getWorld(),
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("x", target.getLocation().getX() + ""), target, data)),
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("y", target.getLocation().getY() + ""), target, data)),
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("z", target.getLocation().getZ() + ""), target, data)));
			ArrayList<Entity> entities = SelectorUtils.getEntities(loc,
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("xrange", "1.0"), target, data)),
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("yrange", "1.0"), target, data)),
					Double.parseDouble(PlaceholderManager.parseWithEntity(actionInput.getString("zrange", "1.0"), target, data)));
			Script internalScriptChild = new Script(actionInput.getConfigurationSection("children"));
			if (entities.size() > 0) for (int i = 0; i < entities.size(); i++) {
				ConfigurationSection d2 = Script.cloneData(data);
				d2.set(localVariableName, i + "");
				internalScriptChild.invoke(entities.get(i), d2);
			}
			break;
		}
		default: break;
		}
	}

}
