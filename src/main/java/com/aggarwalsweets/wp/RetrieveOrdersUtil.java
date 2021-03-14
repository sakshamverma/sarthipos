package com.aggarwalsweets.wp;

import org.apache.log4j.Logger;

import com.aggarwalsweets.sarthipos.TaskUtil;

public final class RetrieveOrdersUtil extends TaskUtil {
	
	private static RetrieveOrdersUtil utility = null;
	
	private RetrieveOrdersUtil () {
		this.name = "RetrieveOrders";
		this.logger = Logger.getLogger(RetrieveOrdersUtil.class);
		this.command = new RetrieveOrdersTask();
		this.delay = WPProperties.delay;
		this.interval = WPProperties.interval;
	}
	
	public static RetrieveOrdersUtil getUtility() {
		if(utility == null) {
			synchronized(RetrieveOrdersUtil.class) {
				if(utility == null) {
					utility = new RetrieveOrdersUtil();
				}
			}
		}
		return utility;
	}
}
