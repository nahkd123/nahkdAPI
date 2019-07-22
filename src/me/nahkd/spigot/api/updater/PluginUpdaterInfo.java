package me.nahkd.spigot.api.updater;

import java.util.ArrayList;
import java.util.Arrays;

public class PluginUpdaterInfo {
	
	public String plugin;
	public String currentVersion;
	public ArrayList<UpdaterParameter> parameters;
	
	public PluginUpdaterInfo(String plugin, String currentVersion) {
		this.plugin = plugin;
		this.currentVersion = currentVersion;
		this.parameters = new ArrayList<UpdaterParameter>();
		this.parameters.add(new UpdaterParameter("version", currentVersion));
	}

	public void addParameter(UpdaterParameter par) {
		this.parameters.add(par);
	}
	public void addParameter(UpdaterParameter... par) {
		this.parameters.addAll(Arrays.asList(par));
	}
	
}
