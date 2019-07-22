package me.nahkd.spigot.api.scripting;

import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

public class ScriptPackCommand {
	
	public Script script;
	public String permission;
	public String usage;
	public String denyMessage;
	
	public Command resultCommand;
	
	public ScriptPackCommand(Script script, String permission, String usage, String denyMessage) {
		this.script = script;
		this.permission = permission;
		this.usage = usage;
		this.denyMessage = denyMessage;
	}
	
	public void runCommand(Entity entity, String... arguments) {
		YamlConfiguration cfgClone = new YamlConfiguration();
		for (String key : script.configScript.getKeys(true)) {
			cfgClone.set(key, script.configScript.get(key, null));
		}
		for (int i = 0; i < arguments.length; i++) cfgClone.set("runtime.command.argument." + i, arguments[i]);
		for (int i = 0; i < arguments.length; i++) cfgClone.set("runtime.command.argument.length", arguments.length);
		script.invoke(entity, cfgClone);
	}
	
}
