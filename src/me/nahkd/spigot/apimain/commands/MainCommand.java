package me.nahkd.spigot.apimain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderListener;
import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scoreboard.PerPlayerScoreboard;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§3nahkd§bAPI §7> §fMain Command");
			sender.sendMessage("§3/nahkdapi listplaceholders §bList all placeholders");
			sender.sendMessage("§3/nahkdapi listactions §bList all script actions");
			sender.sendMessage("§3/nahkdapi gc §bPerform garbage collector (for low free memory)");
			sender.sendMessage("§3/nahkdapi scoreboard §bShow scoreboard");
			sender.sendMessage("§3/nahkdapi pparse <String...> §bParse placeholder");
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
			} else if (args[0].equalsIgnoreCase("scoreboard")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.setScoreboard(PerPlayerScoreboard.getMap().get(player).playerScoreboard);
					player.sendMessage("§aDone");
				} else sender.sendMessage("§cScoreboard only avaliable for player");
			}
		} else if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("pparse")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					String output = args[1];
					for (int i = 2; i < args.length; i++) output += args[i];
					player.sendMessage(PlaceholderManager.parse(output, player));
				} else sender.sendMessage("§cPlaceholders only avaliable for player");
				
			}
		}
		return true;
	}

}
