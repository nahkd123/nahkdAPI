package me.nahkd.spigot.apimain.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.nahkd.spigot.api.timings.TimingsReport;
import me.nahkd.spigot.apimain.nahkdAPI;

public class TimingsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (nahkdAPI.timingsReport == null) {
			nahkdAPI.timingsReport = new TimingsReport();
			sender.sendMessage("§aTimings started. Type this command again to stop and get result");
		} else {
			TimingsReport report = nahkdAPI.timingsReport;
			report.save("timings_" + report.startTime);
			nahkdAPI.timingsReport = null;
			sender.sendMessage("§7+----Timings report-------------------------------+");
			sender.sendMessage("For better overall, please run this in console.");
			sender.sendMessage("§fGraphs:                                        §7Mils");
			for (int i = 0; i < 16; i++) {
				int readPoint = (int) Math.round((i / 16.0D) * report.ms.size());
				String msg = "§f[";
				for (int j = 0; j < report.ms.get(readPoint).ms /2; j++) msg += "=";
				msg += "] §a" + report.ms.get(readPoint).ms + " §fms";
				sender.sendMessage(msg);
			}
			sender.sendMessage("§7+-------------------------------------------------+");
		}
		return true;
	}

}
