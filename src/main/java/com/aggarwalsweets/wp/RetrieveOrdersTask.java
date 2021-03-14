package com.aggarwalsweets.wp;

import static com.aggarwalsweets.sarthipos.Constants.ID;
import static com.aggarwalsweets.sarthipos.Constants.LINE_ITEMS;
import static com.aggarwalsweets.sarthipos.Constants.NAME;
import static com.aggarwalsweets.sarthipos.Constants.QUANTITY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.aggarwalsweets.model.Order;
import com.aggarwalsweets.printer.PrinterTaskQueue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class RetrieveOrdersTask implements Runnable {
	private static final Logger logger = Logger.getLogger(RetrieveOrdersTask.class);
	private Request unsignedRequest = new Request.Builder()
			.url(WPProperties.retrieveOrdersEndpoint)
			.get()
			.build();

	@Override
	public void run() {
		// long lastInvokedTimestamp = System.currentTimeMillis();

		try {

			Response response = WPRestInvoker.getInvoker().invoke(unsignedRequest);

			boolean isValid = validateResponse(response);

			String responseBody = null;
			if (isValid) {
				try {
					responseBody = response.body().string();
				} catch (IOException e) {
					logger.error("Error occured while reading the response body", e);
				}
			} else {
				logger.error("Response wasn't valid, not updating the last timestamp");
			}
			List<Order> orders = getOrdersFromResponse(responseBody);

			if (orders.isEmpty()) {
				logger.debug("There were no new orders");
			} else {
				PrinterTaskQueue.getQueue().addAll(orders);
			}
		} catch (Throwable t) {
			logger.error("Some error occurred in retrieve orders: " + t.getMessage(), t);
		}

	}

	private boolean validateResponse(Response response) {
		int statusCode = response.code();
		if(statusCode != 200) {
			logger.error("The call to retrive pending orders for print didn't succeed");
			logger.error("The status code returned: " + statusCode);
			return false;
		}
		return true;
	}
	
	private List<Order> getOrdersFromResponse(String response) {
		JsonElement jelement = JsonParser.parseString(response);

		JsonArray orders = jelement.getAsJsonArray();

		List<Order> internalOrders = new ArrayList<>();

		for (JsonElement order : orders) {
			JsonObject orderJson = order.getAsJsonObject();
			JsonArray lineItems = orderJson.getAsJsonArray(LINE_ITEMS);
			Order.Builder orderBuilder = new Order.Builder();
			orderBuilder.orderId(orderJson.get(ID).getAsInt());
			for (JsonElement item : lineItems) {
				JsonObject itemJson = item.getAsJsonObject();
				String name = itemJson.get(NAME).getAsString();
				int quantity = itemJson.get(QUANTITY).getAsInt();
				orderBuilder.productName(name).quantity(quantity);
			}
			internalOrders.add(orderBuilder.build());
		}
				
		return internalOrders;
	}
}
