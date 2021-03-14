package com.aggarwalsweets.printer;

import java.util.Map;

import org.apache.log4j.Logger;

import com.aggarwalsweets.model.LineItem;
import com.aggarwalsweets.model.Order;

import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.POSPrinterControl114;
import jpos.util.JposPropertiesConst;

public class PosPrinter implements IPrinter{

	private static final Logger logger = Logger.getLogger(PosPrinter.class);

	private static PosPrinter instance = null;
	
	POSPrinterControl114 ptr = (POSPrinterControl114)new POSPrinter();
	
	static {
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, 
				PrinterProperties.posXmlLocation);
	}
	
	private PosPrinter() {
		this.initializePrinter();
	}
	
	public static PosPrinter getPrinter() {
		if(instance == null) {
			synchronized(PosPrinter.class) {
				if(instance == null) {
					instance = new PosPrinter();
				}
			}
		}
		return instance;
	}
	

	private void initializePrinter() {
		try {
			//Open the device.
			//Use the name of the device that connected with your computer.
			ptr.open("POSPrinter");
			
			logger.trace("Printer instance opened");

			//Get the exclusive control right for the opened device.
			//Then the device is disable from other application.
			ptr.claim(1000);

			//Enable the device.
			ptr.setDeviceEnabled(true);
			
			logger.trace("Printer instance init completed");
		}
		catch(JposException ex) {
			logger.error("Unable to initialize the printer. Please check jpos.xml", ex);
		}
	}
	
	public void destroyPrinter() {
		try {
			ptr.close();
		} catch(Exception e) {
			logger.error("Unable to close the printer", e);
		}
	}

	public boolean  printString(String text) {
		return printStringWithCut(text, false);
	}
	
	public boolean printStringWithCut(String text, boolean cut) {
		try {
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, text);
			
			if(cut) {
				cutPaper();
			}
			return true;
		} catch (JposException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void printOrder(Order order) throws JposException {

		String strOutputData = "\n\nAGARWAL SWEETS";
		int iCount = (ptr.getRecLineChars() - (strOutputData.length() * 2)) / 4;
		for (int i=iCount;i!=0;i--){
			strOutputData = " " + strOutputData + " ";
		}
		ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|4C\u001b|cA\u001b|2uC" + strOutputData + "\n");
		
		final int orderID = order.getOrderId();
		
		ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|4COrder ID: " + orderID + "\n");
		
		//ptr.setPageModeVerticalPosition(ptr.getRecLineSpacing());
		
		for(LineItem item: order.getLineItems()) {
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT,  item.getName());
			//ptr.setPageModeHorizontalPosition(ptr.getRecLineWidth() / 4);
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\t" + item.getQuantity() + "\n");
		}
		
	}
	
	
	private void cutPaper() {
		try {
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT,
					"\u001b|" + (ptr.getRecLinesToPaperCut()) + "lF");
			if (ptr.getCapRecPapercut() == true)
				ptr.cutPaper(90);
		} catch (JposException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Integer> getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}
}
