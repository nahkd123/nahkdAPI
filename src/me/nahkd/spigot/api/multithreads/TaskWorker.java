package me.nahkd.spigot.api.multithreads;

import java.util.ArrayList;

public class TaskWorker extends RepeatingThread {
	
	public ArrayList<ScheduledTask> tasks;

	public TaskWorker() {
		tasks = new ArrayList<ScheduledTask>();
	}
	
	@Override
	public void onLoop() {
		for (int i = 0; i < tasks.size(); i++) {
			ScheduledTask task = tasks.get(i);
			task.remainingTicks--;
			if (task.remainingTicks <= 0) {
				try {task.taskRun();} catch (Exception e) {task.taskFailed(e);}
				tasks.remove(i);
			}
		}
	}
	
}
