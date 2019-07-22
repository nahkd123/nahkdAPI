package me.nahkd.spigot.api.multithreads;

import java.util.ArrayList;

public class RepeatingThreadsManager {
	
	public ArrayList<RepeatingThread> threads;
	
	public RepeatingThreadsManager() {
		this.threads = new ArrayList<RepeatingThread>();
	}
	
	public void startThread(RepeatingThread thread) {
		this.threads.add(thread);
		thread.start();
	}
	public void stopAll() {
		for (int i = 0; i < threads.size(); i++) {
			RepeatingThread thread = threads.get(i);
			if (!thread.isInterrupted()) thread.interrupt();
			threads.remove(i);
		}
	}
	
}
