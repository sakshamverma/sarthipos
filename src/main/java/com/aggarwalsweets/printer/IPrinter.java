package com.aggarwalsweets.printer;

import java.util.Map;

import com.aggarwalsweets.model.Order;

public interface IPrinter {

	public void printOrder(Order order) throws Exception;
	
	public Map<String, Integer> getStatistics();
}
