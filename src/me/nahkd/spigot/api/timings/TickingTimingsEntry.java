package me.nahkd.spigot.api.timings;

public class TickingTimingsEntry implements Stampable {

	public long ms;
	public long timestamp;
	public int onlinePlayers;
	
	public TickingTimingsEntry(long baseTime, long ms, int onlinePlayers) {
		this.ms = ms;
		this.timestamp = System.currentTimeMillis() - baseTime;
		this.onlinePlayers = onlinePlayers;
	}
	
	@Override
	public long getTimeStamp() {
		return 0;
	}

}
