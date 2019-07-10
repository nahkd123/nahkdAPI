package me.nahkd.spigot.api.scripting;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public abstract class ScriptActionGroup {
	
	public String name;
	
	public ScriptActionGroup(String name) {
		this.name = name;
	}
	public void register() {
		getMap().put(name, this);
	}
	
	public abstract void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput);
	
	public static HashMap<String, ScriptActionGroup> groups;
	public static HashMap<String, ScriptActionGroup> getMap() {
		if (groups == null) groups = new HashMap<String, ScriptActionGroup>();
		return groups;
	}
	public static void invoke(Entity target, String group, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		ScriptActionGroup action = getMap().get(group);
		if (action != null) {
			action.invoke(target, type, data, actionInput);
		} else {
			System.out.println("[nahkdAPI](Scripting) Action group not found: " + group);
		}
	}
	
}
