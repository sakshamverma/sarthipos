package com.aggarwalsweets.printer;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aggarwalsweets.sarthipos.TaskUtil;

public final class PrinterTaskUtil extends TaskUtil{

	private static PrinterTaskUtil utility = null;

	private IPrinter printer = null;
	private PrinterTaskUtil () {
		this.name = "PrintOrders";
		this.logger = Logger.getLogger(PrinterTaskUtil.class);
		printer = PrinterFactory.getPrinter();
		this.command = new PrintTask(printer);
		this.interval = PrinterProperties.printInterval;
		this.delay = PrinterProperties.printInitialDelay;
	}
	
	public static TaskUtil getUtility() {
		if(utility == null) {
			synchronized(PrinterTaskUtil.class) {
				if(utility == null) {
					utility = new PrinterTaskUtil();
				}
			}
		}
		return utility;
	}

	public Map<String, Integer> getStats() {
		return printer.getStatistics();
	}
}
