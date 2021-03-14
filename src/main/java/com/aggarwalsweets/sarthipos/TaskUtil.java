package com.aggarwalsweets.sarthipos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public abstract class TaskUtil {
	protected Logger logger ;

	private ScheduledExecutorService scheduledExecutorService;

	protected Runnable command = null;
	protected String name;

	protected long delay;
	protected long interval;

	public void startTask() {
		if(scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
			setupTaskExecutor();
		} else {
			logger.error(name + " Task is already running");
		}
	}
	public void stopTask() {
		if(scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
			logger.error(name + " Task is already stopped");
		} else {
			scheduledExecutorService.shutdown();
		}
	}

	private void setupTaskExecutor() {
		scheduledExecutorService = Executors.newScheduledThreadPool(1,
				new ThreadFactoryBuilder().setNameFormat(name + "-pool-%d").build());
		if (interval == 0) {
			interval = 60L;
		}
		scheduledExecutorService.scheduleAtFixedRate(command, delay, interval, TimeUnit.SECONDS);
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
	}
	
}
