package com.aggarwalsweets.printer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.aggarwalsweets.model.LineItem;
import com.aggarwalsweets.model.Order;

public class ConsolePrinter implements IPrinter {

	private static final Logger logger = Logger.getLogger(ConsolePrinter.class);

	private static ConsolePrinter instance = null;

	AtomicInteger printedCount = new AtomicInteger();
	
	@Override
	public void printOrder(Order order) throws Exception {
		logger.debug("\t\t OrderID \t " + order.getOrderId());

		for (LineItem item : order.getLineItems()) {
			logger.debug("\t\t" + item.getName() + "\t\t" + item.getQuantity());
		}
		printedCount.incrementAndGet();

	}

	private ConsolePrinter() {
	}

	public static ConsolePrinter getPrinter() {
		if (instance == null) {
			synchronized (PosPrinter.class) {
				if (instance == null) {
					instance = new ConsolePrinter();
				}
			}
		}
		return instance;
	}

	@Override
	public Map<String, Integer> getStatistics() {
		Map<String, Integer> stats = new HashMap<>();
		stats.put("Printed", printedCount.get());
		return stats;
	}

}
