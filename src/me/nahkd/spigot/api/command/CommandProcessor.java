package me.nahkd.spigot.api.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProcessor {
	
	public static void processCommand(String subCommand, String[] args, CommandSender sender, Runnable method) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(subCommand)) {
				method.run();
			}
		} else {
			sender.sendMessage("§cInvaild command.");
		}
	}
	
	public static void processCommandPlayer(String subCommand, String[] args, CommandSender sender, Runnable method) {
		if (sender instanceof Player) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase(subCommand)) {
					method.run();
				}
			} else {
				sender.sendMessage("§cInvaild command.");
			}
		} else {
			sender.sendMessage("§cThis command must executed as player.");
		}
	}
	
}
