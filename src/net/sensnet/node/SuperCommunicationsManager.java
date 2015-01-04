package net.sensnet.node;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuperCommunicationsManager implements Runnable {
	private static final SuperCommunicationsManager instance = new SuperCommunicationsManager();
	private LinkedBlockingQueue<ExceptionRunnable> queue = new LinkedBlockingQueue<ExceptionRunnable>();

	private SuperCommunicationsManager() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			if (queue.isEmpty()) {
				try {
					Thread.sleep(15000);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (!queue.isEmpty()) {
				try {
					queue.peek().run();
					queue.poll();
				} catch (Exception e) {
					Logger.getLogger("SuperCommunicatinsManager")
							.log(Level.WARNING,
									"Could not release tasks into the wild. Waiting 15 secounds.",
									e);
					break;
				}
			}
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public static SuperCommunicationsManager getInstance() {
		return instance;
	}

	public void putJob(ExceptionRunnable r) {
		queue.add(r);
	}

}
