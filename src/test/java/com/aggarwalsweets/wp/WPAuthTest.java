package com.aggarwalsweets.wp;

import static com.aggarwalsweets.sarthipos.Constants.ID;
import static com.aggarwalsweets.sarthipos.Constants.LINE_ITEMS;
import static com.aggarwalsweets.sarthipos.Constants.NAME;
import static com.aggarwalsweets.sarthipos.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.aggarwalsweets.model.Order;
import com.aggarwalsweets.sarthipos.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class WPAuthTest {

	private static final String consumerKey ;//= "ck_495d819ac7f3cede04debf9337e233dd39a9652b";
	private static final String consumerSecret;// = "cs_f3ccb7c51cf12c24b60f4208c679e3f9aa78bd6d";
	private static final String accessToken;// = "";
	private static final String accessSecret;// = "";
	private static final String url; // = "https://aggarwalsweets.net/beta/wp-json/wc/v3/orders";

	static {
		Properties wpProperties = new Properties();
		ClassLoader classLoader = WPAuthTest.class.getClassLoader();
		
		try {
			wpProperties.load(classLoader.getResourceAsStream("wp.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		consumerKey = (String) wpProperties.getOrDefault(Constants.CONSUMER_KEY, "");
		consumerSecret = (String) wpProperties.getOrDefault(Constants.CONSUMER_SECRET, "");
		accessToken = (String) wpProperties.getOrDefault(ACCESS_TOKEN, "");
		accessSecret = (String) wpProperties.getOrDefault(ACCESS_SECRET, "");
		url = (String) wpProperties.getOrDefault(RETRIEVE_ORDERS_EP, "");
	}
	
	private static final OkHttpClient client = new OkHttpClient();

	private static WPAuth wpAuth;

	private static Request unSignedRequest;

	@BeforeClass
	public static void setup() throws Exception {
		wpAuth = new WPAuth.Builder()
				.consumerKey(consumerKey)
				.consumerSecret(consumerSecret)
				.accessSecret(accessSecret)
				.accessToken(accessToken)
				.build();
		unSignedRequest = new Request.Builder()
				.url(url)
				.get()
				.build();

		
	}

	@Test
	public void testAuthString() throws Exception {
		Request signedRequest = wpAuth.signRequest(unSignedRequest);
		Response response = client.newCall(signedRequest).execute();
		Assert.assertEquals(200, response.code());
	}

	@Test
	public void testLineItemsExists() throws Exception {
		Request signedRequest = wpAuth.signRequest(unSignedRequest);
		Response response = client.newCall(signedRequest).execute();
		
		JsonElement jelement = JsonParser.parseString(response.body().string());
		
	    JsonArray  orders = jelement.getAsJsonArray();
	    
	    List<Order> internalOrders = new ArrayList<>();
	    
	    for(JsonElement order : orders) {
	    	JsonObject orderJson = order.getAsJsonObject();
	    	JsonArray lineItems = orderJson.getAsJsonArray(LINE_ITEMS);
	    	Order.Builder orderBuilder = new Order.Builder();
	    	orderBuilder.orderId(orderJson.get(ID).getAsInt());
	    	for(JsonElement item : lineItems) {
	    		JsonObject itemJson = item.getAsJsonObject();
	    		String name = itemJson.get(NAME).getAsString();
	    		int quantity = itemJson.get(QUANTITY).getAsInt();
	    		orderBuilder.productName(name).quantity(quantity);
	    	}
	    	internalOrders.add(orderBuilder.build());
	    }
	    
	    for(Order internalOrder: internalOrders) {
	    	System.out.println("OrderId: " + internalOrder.getOrderId());
	    	System.out.println(internalOrder.getLineItems());
	    }
	    
	}
}
