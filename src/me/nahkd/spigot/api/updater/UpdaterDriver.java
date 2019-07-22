package me.nahkd.spigot.api.updater;

import java.io.InputStream;

/**
 * Updater driver that download plugin
 * @author nahkd123
 *
 * @param <T> A Object extends {@link UpdaterParameter}
 */
public interface UpdaterDriver {
	
	/**
	 * Check if new version is available or not
	 * @param parameters The input parameters
	 * @return Whenever new version or not
	 */
	public boolean isAvaliable(UpdaterParameter... parameters);
	public boolean downloadAllowed(UpdaterParameter... parameters);
	public InputStream getLatest(UpdaterParameter... parameters);
	
}
