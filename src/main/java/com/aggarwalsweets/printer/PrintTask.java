package com.aggarwalsweets.printer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.aggarwalsweets.model.Order;

public class PrintTask implements Runnable {

	private IPrinter printer;

	public PrintTask(IPrinter printer) {
		this.printer = printer;
	}

	private static final Logger logger = Logger.getLogger(PrintTask.class);

	@Override
	public void run() {
		try {
			List<Order> newOrders = new ArrayList<Order>(PrinterProperties.batchSize);
			PrinterTaskQueue.getQueue().drainTo(newOrders, PrinterProperties.batchSize);

			for (Order order : newOrders) {
				final int orderId = order.getOrderId();
				logger.debug("Printing the order: " + orderId);
				try {
					printer.printOrder(order);
					logger.trace("Print successful for order: " + orderId);
				} catch (Exception e) {
					logger.error("Error while printing the order: " + orderId, e);
				}
			}
		} catch (Throwable t) {
			logger.error("Some error occurred while printing: " + t.getMessage(), t);
		}

	}
}
