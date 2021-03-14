package com.aggarwalsweets.printer;

import static com.aggarwalsweets.sarthipos.Constants.*;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PrinterProperties {

	private static final String PROPERTIES_FILE = "printer.properties";
	static Logger logger = Logger.getLogger(PrinterProperties.class);
	
	public static final String posXmlLocation;
	public static final int batchSize;
	public static final long printInterval;
	public static final long printInitialDelay;
	public static final String printerClass;
	static {
		Properties properties = new Properties();
		ClassLoader classLoader = PrinterProperties.class.getClassLoader();
		
		try {
			properties.load(classLoader.getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		posXmlLocation = properties.getProperty(POS_XML_LOCATION);
		
		batchSize = Integer.parseInt((String) (properties.getOrDefault(PRINT_BATCH_SIZE, "1")));
		printInterval = Long.parseLong((String) (properties.getOrDefault(PRINT_INTERVAL, "60")));
		printInitialDelay = Long.parseLong(((String)properties.getOrDefault(PRINT_INITIAL_DELAY, "60")));
		printerClass = properties.getProperty(PRINTER_CLASS);
	}
		
}
