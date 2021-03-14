package com.aggarwalsweets.sarthipos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.aggarwalsweets.printer.PrinterTaskUtil;
import com.aggarwalsweets.wp.RetrieveOrdersUtil;

public class PosMain 
{
	static Logger logger = Logger.getLogger(PosMain.class);
    public static void main( String[] args ) throws InterruptedException
    {
    	ClassLoader classLoader = PosMain.class.getClassLoader();
    	PropertyConfigurator.configure(classLoader.getResourceAsStream(Constants.LOG4J_PROPERTIES));
        
    	RetrieveOrdersUtil.getUtility().startTask();
    	
    	PrinterTaskUtil.getUtility().startTask();
    	
    	Object lock = new Object();
    	
    	synchronized(lock) {
    		lock.wait();
    	}
    }
}
