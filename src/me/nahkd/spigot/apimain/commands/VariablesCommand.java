package me.nahkd.spigot.apimain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import me.nahkd.spigot.apimain.nahkdAPI;

public class VariablesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§3nahkd§bAPI §7> §fVariables");
			sender.sendMessage("  §7§oYou can use this on script or placeholder.");
			sender.sendMessage("§3/variable set <Path> <Value> §bSet string");
			sender.sendMessage("§3/variable setnumber <Path> <Value> §bSet number");
			sender.sendMessage("§3/variable increases <Path> <Amount> §bIncreases (use negative for decrease)");
			sender.sendMessage("§3/variable view <Path> §bView");
			sender.sendMessage("§3/variable list §bList all path parents");
			sender.sendMessage("§3/variable clear §bClear all variables");
			sender.sendMessage("§3/variable save §bSave all variables");
			sender.sendMessage("§3/variable reload §bLoad variables from file");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("clear")) {
				sender.sendMessage("§cClearing all variables...");
				nahkdAPI.variables = new YamlConfiguration();
				sender.sendMessage("§aDone");
			}
		}
		return true;
	}

}