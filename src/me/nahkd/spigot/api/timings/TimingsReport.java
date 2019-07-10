package me.nahkd.spigot.api.timings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TimingsReport {
	
	public ArrayList<TickingTimingsEntry> ms;
	public long startTime;
	public long previousTimestamp;
	
	public TimingsReport() {
		this.ms = new ArrayList<TickingTimingsEntry>();
		this.startTime = System.currentTimeMillis();
		this.previousTimestamp = System.currentTimeMillis();
	}
	
	public void save(String recordName) {
		File file = new File(recordName + ".bin");
		if (!file.exists()) {
			try {
				FileOutputStream output = new FileOutputStream(file);
				for (TickingTimingsEntry entry : ms) {
					int outputMS = (int) entry.ms;
					//System.out.println((outputMS >> 8) + " - " + (outputMS - ((outputMS >> 8) << 8)));
					byte[] toFile = new byte[] {
							// Mils
							(byte) (outputMS >> 8),
							(byte) (outputMS - ((outputMS >> 8) << 8)),
							// Online players
							(byte) (entry.onlinePlayers >> 8),
							(byte) (entry.onlinePlayers - ((entry.onlinePlayers >> 8) << 8)),
					};
					output.write(toFile);
				}
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
