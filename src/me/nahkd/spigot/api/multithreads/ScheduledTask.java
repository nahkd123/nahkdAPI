package me.nahkd.spigot.api.multithreads;

public abstract class ScheduledTask {
	
	public int remainingTicks;
	
	public ScheduledTask(int ticks) {
		this.remainingTicks = ticks;
	}
	
	public abstract void taskRun();
	public void taskFailed(Exception e) {
		System.err.println("[nahkdAPI](Task) Scheduled task exception throw:");
		e.printStackTrace();
	}
	
}
