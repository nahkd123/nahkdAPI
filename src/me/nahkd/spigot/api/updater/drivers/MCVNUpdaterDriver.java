package me.nahkd.spigot.api.updater.drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import me.nahkd.spigot.api.debugger.Debug;
import me.nahkd.spigot.api.updater.PluginUpdater;
import me.nahkd.spigot.api.updater.UpdaterDriver;
import me.nahkd.spigot.api.updater.UpdaterParameter;

/**
 * Get new version from https://minecraftvn.net/resources/
 */
public class MCVNUpdaterDriver implements UpdaterDriver {

	@Override
	public boolean isAvaliable(UpdaterParameter... parameters) {
		if (PluginUpdater.getParmeter("mcvn-update", parameters) != null) try {
			URL url = new URL("https://minecraftvn.net/resource_version.api?id=" + PluginUpdater.getParmeter("mcvn-id", parameters).value);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			// Note: You can set User Agent by using -Dhttp.agent=USER-AGENT
			http.connect();
			InputStream result = http.getInputStream();
			InputStreamReader isr = new InputStreamReader(result);
			BufferedReader reader = new BufferedReader(isr);
			String version = null;
			String version2 = null;
			while ((version = reader.readLine()) != null) {
				Debug.out("§bUpdater §7> §bMCVN §7> §aPlugin version: " + version);
				version2 = version;
			}
			reader.close();
			if (version2 == null) return false;
			if (PluginUpdater.comparesVersion(PluginUpdater.getParmeter("version", parameters).value, version2) < 0) {
				Debug.out("§bThis plugin version is higher than resource page!");
				return false;
			}
			else return !PluginUpdater.getParmeter("version", parameters).value.equalsIgnoreCase(version2.toLowerCase());
		} catch (UnknownHostException e) {
			Debug.out("§cUnknownHostException throw, result in no host found");
			return false;
		} catch (MalformedURLException e) {
			Debug.out("§cMalformed URL while checking resource is avaliable or not");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			if (e.getMessage().contains("Server returned HTTP response code: 4")) {
				Debug.out("§cClient side error while fetching for new update (at MCVN): 4xx");
				Debug.out("§cFull message: " + e.getMessage());
			} else e.printStackTrace();
			return false;
		}
		else return false;
	}

	@Override
	/**
	 * This always return false, since MCVN doesn't allow to download plugin at this time
	 */
	public boolean downloadAllowed(UpdaterParameter... parameters) {return false;}

	@Override
	public InputStream getLatest(UpdaterParameter... parameters) {return null;}

}
