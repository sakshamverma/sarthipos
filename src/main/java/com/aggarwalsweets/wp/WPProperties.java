package com.aggarwalsweets.wp;

import static com.aggarwalsweets.sarthipos.Constants.*;
import static com.aggarwalsweets.sarthipos.Constants.ACCESS_TOKEN;
import static com.aggarwalsweets.sarthipos.Constants.RETRIEVE_ORDERS_EP;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.aggarwalsweets.sarthipos.Constants;

public class WPProperties {

	private static final String PROPERTIES_FILE = "wp.properties";
	static Logger logger = Logger.getLogger(WPProperties.class);
	
	public static final String consumerKey ;
	public static final String consumerSecret;
	public static final String accessToken;
	public static final String accessSecret;
	public static final String retrieveOrdersEndpoint;
	public static final long interval;
	public static final long delay;

	static {
		Properties wpProperties = new Properties();
		ClassLoader classLoader = WPProperties.class.getClassLoader();
		
		try {
			wpProperties.load(classLoader.getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		consumerKey = (String) wpProperties.getOrDefault(Constants.CONSUMER_KEY, "");
		consumerSecret = (String) wpProperties.getOrDefault(Constants.CONSUMER_SECRET, "");
		accessToken = (String) wpProperties.getOrDefault(ACCESS_TOKEN, "");
		accessSecret = (String) wpProperties.getOrDefault(ACCESS_SECRET, "");
		retrieveOrdersEndpoint = (String) wpProperties.getOrDefault(RETRIEVE_ORDERS_EP, "");
		interval = Long.parseLong((String) wpProperties.getOrDefault(RETRIEVE_ORDERS_INTERVAL, "60"));
		delay = Long.parseLong((String) wpProperties.getOrDefault(RETRIEVE_ORDERS_INITIAL_DELAY, "60"));
		logger.debug("WordPress properties got laoded");
	}
}
