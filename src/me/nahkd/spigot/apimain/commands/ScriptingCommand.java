package me.nahkd.spigot.apimain.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.scripting.Script;
import me.nahkd.spigot.apimain.nahkdAPI;

public class ScriptingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) {
				sender.sendMessage("§3The correct usage is...");
				sender.sendMessage("§b/script <Script>.yml [Arguments...]");
			} else {
				YamlConfiguration scriptConfig = YamlConfiguration.loadConfiguration(new File(nahkdAPI.scripts, args[0]));
				Script script = new Script(scriptConfig);
				YamlConfiguration data = new YamlConfiguration();
				String[] aargs = new String[args.length - 1];
				for (int i = 0; i < aargs.length; i++) aargs[i] = args[i + 1];
				for (int i = 0; i < aargs.length; i++) data.set("runtime.command.argument." + i, aargs[i]);
				for (int i = 0; i < aargs.length; i++) data.set("runtime.command.argument.length", aargs.length);
				script.invoke((Player) sender, data);
			}
		} else sender.sendMessage("§6Warn> §7Use entity instead");
		return true;
	}
	
}
