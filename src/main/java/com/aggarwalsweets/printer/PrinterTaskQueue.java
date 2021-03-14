package com.aggarwalsweets.printer;

import java.util.concurrent.LinkedBlockingQueue;

import com.aggarwalsweets.model.Order;

public class PrinterTaskQueue extends LinkedBlockingQueue<Order> {

	private static final long serialVersionUID = -3648953865387073390L;

	private PrinterTaskQueue () {}
	
	private static PrinterTaskQueue queue = null;
	
	public static PrinterTaskQueue getQueue() {
		if(queue == null) {
			synchronized(PrinterTaskQueue.class) {
				if(queue == null) {
					queue = new PrinterTaskQueue();
				}
			}
		}
		return queue;
	}
	
}
