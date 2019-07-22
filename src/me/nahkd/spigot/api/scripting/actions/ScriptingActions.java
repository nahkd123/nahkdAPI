package me.nahkd.spigot.api.scripting.actions;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.Script;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;
import me.nahkd.spigot.apimain.nahkdAPI;

public class ScriptingActions extends ScriptActionGroup {

	public ScriptingActions() {
		super("Script");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		switch (type) {
		case "Run": {
			YamlConfiguration scriptConf = YamlConfiguration.loadConfiguration(new File(nahkdAPI.scripts, actionInput.getString("script", "welcome.yml")));
			Script script = new Script(scriptConf);
			ConfigurationSection scriptdata = new YamlConfiguration();
			if (actionInput.getBoolean("crossdata", false)) scriptdata = data;
			if (actionInput.contains("inputdata")) for (String key : actionInput.getConfigurationSection("inputdata").getKeys(true)) {
				scriptdata.set("runtime.script.callinputs." + key, actionInput.get(key));
			}
			script.invoke(target, scriptdata);
			break;
		}
		case "Set": {
			String output = actionInput.getString("value", "Undefined");
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				output = PlaceholderManager.parse(output, player, data);
			} else output = PlaceholderManager.parse(output, data);
			data.set(actionInput.getString("path", "nullpointer"), output);
			break;
		}
		case "View Data": {
			if (target instanceof Player || target instanceof HumanEntity) {
				for (String a : data.getKeys(true)) ((Player) target).sendMessage("§7" + a + ": §f" + data.get(a).toString());
			}
		}
		default: break;
		}
	}

}
