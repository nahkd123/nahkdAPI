package me.nahkd.spigot.api.scripting.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import me.nahkd.spigot.api.placeholder.PlaceholderManager;
import me.nahkd.spigot.api.scripting.Script;
import me.nahkd.spigot.api.scripting.ScriptActionGroup;

public class ConditionScriptActions extends ScriptActionGroup {

	public ConditionScriptActions() {
		super("Conditional");
		register();
	}

	@Override
	public void invoke(Entity target, String type, ConfigurationSection data, ConfigurationSection actionInput) {
		boolean success = checkConditions(target, actionInput.getConfigurationSection("conditions"), data);
		if (success) {
			if (actionInput.contains("children")) {
				Script internalScriptChild = new Script(actionInput.getConfigurationSection("children"));
				internalScriptChild.invoke(target, data);
			}
		} else {
			if (actionInput.contains("children-else")) {
				Script internalScriptChild = new Script(actionInput.getConfigurationSection("children-else"));
				internalScriptChild.invoke(target, data);
			}
		}
	}
	
	public static boolean checkConditions(Entity target, ConfigurationSection conditions, ConfigurationSection data) {
		boolean success = true;
		for (String child : conditions.getKeys(false)) {
			String input1 = "";
			String input2 = "";
			if (target instanceof Player || target instanceof HumanEntity) {
				Player player = Bukkit.getPlayer(target.getName());
				input1 = PlaceholderManager.parse(conditions.getString(child + ".input1", "0"), player, data);
				input2 = PlaceholderManager.parse(conditions.getString(child + ".input2", "0"), player, data);
			} else {
				input1 = PlaceholderManager.parse(conditions.getString(child + ".input1", "0"), data);
				input2 = PlaceholderManager.parse(conditions.getString(child + ".input2", "0"), data);
			}
			switch (conditions.getString(child + ".type", "==")) {
			case "==":
			case "equals":
			case "string equals":
			case "number equals": success = success && input1.equals(input2); break;
			case ">":
			case "number larger":
			case "larger":
			case "greater": success = success && Double.parseDouble(input1) > Double.parseDouble(input2) ; break;
			case ">=":
			case "number larger equals":
			case "larger equals":
			case "greater equals": success = success && Double.parseDouble(input1) >= Double.parseDouble(input2) ; break;
			case "<":
			case "number smaller":
			case "less":
			case "smaller": success = success && Double.parseDouble(input1) < Double.parseDouble(input2) ; break;
			case "<=":
			case "number smaller equals":
			case "smaller equals":
			case "less equals": success = success && Double.parseDouble(input1) <= Double.parseDouble(input2) ; break;
			default: break;
			}
		}
		return success;
	}

}
