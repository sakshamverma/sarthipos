package com.aggarwalsweets.printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aggarwalsweets.model.Order;

public class PrinterTaskTest {

	private List<Order> orders = new ArrayList<>();

	@Before
	public void setupOrders() {
		orders.add(new Order.Builder().orderId(1).productName("samosa").quantity(10).productName("Kaju Katli")
				.quantity(15).productName("Ras Malai").quantity(25).build());

		orders.add(new Order.Builder().orderId(2).productName("samosa").quantity(10).build());

		orders.add(new Order.Builder().orderId(3).productName("Pizza").quantity(1).build());
	}

	@Test
	public void test() throws Exception {
		PrinterTaskUtil utility = (PrinterTaskUtil) PrinterTaskUtil.getUtility();
		PrinterTaskQueue.getQueue().addAll(orders);
		utility.startTask();
		Thread.sleep(20000);
		Map<String, Integer> stats = utility.getStats();
		
		Assert.assertEquals((Integer)3, stats.get("Printed"));
		
	}

}
