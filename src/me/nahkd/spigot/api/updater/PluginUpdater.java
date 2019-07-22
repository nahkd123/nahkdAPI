package me.nahkd.spigot.api.updater;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import me.nahkd.spigot.api.debugger.Debug;

public class PluginUpdater {
	
	private static HashMap<String, UpdaterDriver> drivers;

	public static Collection<UpdaterDriver> getDrivers() {
		if (drivers == null) drivers = new HashMap<String, UpdaterDriver>();
		return drivers.values();
	}
	private static HashMap<String, UpdaterDriver> driversMap() {
		if (drivers == null) drivers = new HashMap<String, UpdaterDriver>();
		return drivers;
	}
	/**
	 * Add driver for updating
	 * @param type The driver type (or driver identifier)
	 * @param driver The driver
	 */
	public static void addDriver(String type, UpdaterDriver driver) {
		Debug.out("§bPlugin updater driver registered: " + type);
		driversMap().put(type, driver);
	}
	
	public static boolean searchForUpdate(PluginUpdaterInfo info) {
		for (UpdaterDriver driver : getDrivers()) {
			if (driver.isAvaliable(info.parameters.toArray(new UpdaterParameter[0]))) return true;
		}
		return false;
	}
	public static boolean canDownload(PluginUpdaterInfo info) {
		for (UpdaterDriver driver : getDrivers()) {
			if (driver.downloadAllowed(info.parameters.toArray(new UpdaterParameter[0]))) return true;
		}
		return false;
	}
	public static InputStream download(PluginUpdaterInfo info) {
		for (UpdaterDriver driver : getDrivers()) {
			if (driver.downloadAllowed(info.parameters.toArray(new UpdaterParameter[0]))) return driver.getLatest(info.parameters.toArray(new UpdaterParameter[0]));
		}
		return null;
	}
	
	public static UpdaterParameter getParmeter(String key, UpdaterParameter... parameters) {
		for (UpdaterParameter par : parameters) if (par.key.equalsIgnoreCase(key.toLowerCase())) return par;
		return null;
	}
	
	public static int comparesVersion(String v1, String v2) {
		int dist = 0;
		
		String[] v1spl = v1.split("\\.");
		String[] v2spl = v2.split("\\.");
		for (int i = 0; i < v1spl.length; i++) {
			if (i == 0) dist += (Integer.parseInt(v2spl[i]) - Integer.parseInt(v1spl[i])) * 1000000;
			if (i == 1) dist += (Integer.parseInt(v2spl[i]) - Integer.parseInt(v1spl[i])) * 10000;
			if (i == 2) dist += (Integer.parseInt(v2spl[i]) - Integer.parseInt(v1spl[i])) * 100;
			if (i == 3) dist += (Integer.parseInt(v2spl[i]) - Integer.parseInt(v1spl[i])) * 1;
		}
		
		return dist;
	}
	
}
