package me.nahkd.spigot.apimain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§3nahkd§bAPI §7> §fMain Command");
			sender.sendMessage("§3/nahkdapi listplaceholders §bList all placeholders");
			sender.sendMessage("§3/nahkdapi listactions §bList all script actions");
			sender.sendMessage("§3/nahkdapi gc §bPerform garbage collector (for low free memory)");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("listplaceholders")) {
				sender.sendMessage("§7Listing all placeholders:");
				for (PlaceholderListener p : PlaceholderManager.getRegistered().values()) sender.sendMessage("§7 - §a" + p.lookupName);
			} else if (args[0].equalsIgnoreCase("listactions")) {
				sender.sendMessage("§7Listing all script action groups:");
				for (ScriptActionGroup p : ScriptActionGroup.getMap().values()) sender.sendMessage("§7 - §a" + p.name);
			} else if (args[0].equalsIgnoreCase("gc")) {
				long i = Runtime.getRuntime().freeMemory();
				sender.sendMessage("§3Current free memory: §f" + i);
				System.gc();
				sender.sendMessage("§aGarbage collector complete");
				long k = Runtime.getRuntime().freeMemory();
				sender.sendMessage("§7Extra free memory: §f" + (k - i));
			}
		}
		return true;
	}

}
