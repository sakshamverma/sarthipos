package com.aggarwalsweets.printer;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class PrinterFactory {

	private static final Logger logger = Logger.getLogger(PrinterFactory.class);

	public static IPrinter getPrinter() {

		IPrinter retVal = null;
		try {
			Class printerClass = Class.forName(PrinterProperties.printerClass);

			if (IPrinter.class.isAssignableFrom(printerClass)) {
				Method getPrinterMethod = printerClass.getMethod("getPrinter");
				if (getPrinterMethod == null) {
					logger.error("The printerClass should have a static method getPrinter");
				}
				retVal = (IPrinter) getPrinterMethod.invoke(null);

			} else {
				logger.error("The printerClass mentioned in the printer.properties file is invalid");
			}
		} catch (ClassNotFoundException ce) {
			logger.error(
					"The class mentioned in printer.properties for Printer is not found. Using the default PdfPrinter");
		} catch (NoSuchMethodException me) {
			logger.error("The printerClass should have a static method getPrinter. Using the default PdfPrinter");
		} catch (Throwable t) {
			logger.error("Some error occurred while creating a printer instance: " + t.getMessage(), t);
		}
		return retVal;

	}
}
