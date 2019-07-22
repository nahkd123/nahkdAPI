package me.nahkd.spigot.api.multithreads;

public abstract class RepeatingThread extends Thread {
	
	private final boolean sync;
	private final Object mutex;
	public int delay;
	public int currentTick;
	
	/**
	 * Create a synchronized task
	 */
	public RepeatingThread(Object mutex) {
		this.sync = true;
		this.mutex = mutex;
		this.delay = 0;
		this.currentTick = 0;
	}
	
	/**
	 * Create a asynchronized task
	 */
	public RepeatingThread() {
		this.sync = false;
		this.mutex = null;
		this.delay = 0;
		this.currentTick = 0;
	}
	
	/**
	 * This method will run every 50ms or 1 game tick.
	 */
	public abstract void onLoop();
	
	@Override
	public void run() {
		try {
			try {
				while (true) {
					long beforeRun = System.currentTimeMillis();
					if (currentTick <= 0) {
						if (this.sync) {
							synchronized (mutex) {
								onLoop();
								currentTick = delay;
							}
						} else {
							onLoop();
							currentTick = delay;
						}
					} else currentTick--;
					long afterRun = System.currentTimeMillis();
					if (50 - (afterRun - beforeRun) > 0) Thread.sleep(50 - (afterRun - beforeRun));
				}
			} catch (InterruptedException e) {
				throw e;
			} catch (Exception e) {
				System.err.println();
			}
		} catch (InterruptedException e) {
			System.out.println("[nahkdAPI](Multithreading) Thread has been interrupted while sleeping, exiting...");
		}
	}
	
}
