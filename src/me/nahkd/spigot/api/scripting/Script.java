package me.nahkd.spigot.api.scripting;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public class Script {
	
	public ConfigurationSection configScript;
	
	public Script(ConfigurationSection configScript) {
		this.configScript = configScript;
	}
	
	public void invoke(Entity target, ConfigurationSection data) {
		for (String actionName : configScript.getKeys(false)) {
			String group = configScript.getString(actionName + ".group", "None");
			String type = configScript.getString(actionName + ".type", "None");
			ScriptActionGroup.invoke(target, group, type, data, configScript.getConfigurationSection(actionName));
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.configScript = null;
	}
	
}
