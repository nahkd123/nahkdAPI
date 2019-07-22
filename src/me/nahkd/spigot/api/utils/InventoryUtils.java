package me.nahkd.spigot.api.utils;

/**
 * Utils for inventory system
 * @author nahkd123
 *
 */
public class InventoryUtils {
	
	public static int getSlotsNeededForChestInventory(int itemsCount) {
		if (itemsCount > 0 && itemsCount <= 9) return 9;
		if (itemsCount > 9 && itemsCount <= 18) return 18;
		if (itemsCount > 18 && itemsCount <= 27) return 27;
		if (itemsCount > 27 && itemsCount <= 36) return 36;
		if (itemsCount > 36 && itemsCount <= 45) return 45;
		if (itemsCount > 45 && itemsCount <= 54) return 54;
		if (itemsCount > 54 && itemsCount <= 63) return 63;
		if (itemsCount > 63 && itemsCount <= 72) return 72;
		return 81;
	}
	
}
