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
			sender.sendMessage("§7/variable set <Path> <Value> §bSet string");
			sender.sendMessage("§7/variable setnumber <Path> <Value> §bSet number");
			sender.sendMessage("§7/variable increases <Path> <Amount> §bIncreases (use negative for decrease)");
			sender.sendMessage("§3/variable view <Path> §bView");
			sender.sendMessage("§3/variable list §bList all path parents");
			sender.sendMessage("§3/variable clear §bClear all variables");
			sender.sendMessage("§3/variable save §bSave all variables");
			sender.sendMessage("§3/variable reload §bLoad variables from file");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("clear")) {
				nahkdAPI.variables = new YamlConfiguration();
				sender.sendMessage("§aDone");
			} else if (args[0].equalsIgnoreCase("reload")) {
				nahkdAPI.variables = YamlConfiguration.loadConfiguration(nahkdAPI.variablesFile);
				sender.sendMessage("§aDone");
			} else if (args[0].equalsIgnoreCase("save")) {
				nahkdAPI.saveVariables();
				sender.sendMessage("§aDone");
			} else if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage("§7Listing all parents:");
				for (String key : nahkdAPI.variables.getKeys(false)) sender.sendMessage("§7- §f" + key);
			} else sender.sendMessage("§cSubcommand not found");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("view")) {
				sender.sendMessage("");
				sender.sendMessage("§7Path: §f" + args[1]);
				sender.sendMessage("§7Value: §f" + nahkdAPI.variables.get(args[1], "§c§onull").toString());
			}
		}
		return true;
	}

}