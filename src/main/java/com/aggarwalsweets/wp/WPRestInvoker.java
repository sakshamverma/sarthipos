package com.aggarwalsweets.wp;

import java.io.IOException;
import java.time.Clock;
import java.util.Random;

import org.apache.log4j.Logger;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class WPRestInvoker {

	static Logger logger = Logger.getLogger(WPRestInvoker.class);

	private static WPRestInvoker invoker = null;
	
	//Singleton
	private WPRestInvoker() {}
	
	private WPAuth wpAuth;
	
	private void buildWPAuth() {
		wpAuth = new WPAuth.Builder()
				.consumerKey(WPProperties.consumerKey)
				.consumerSecret(WPProperties.consumerSecret)
				.accessSecret(WPProperties.accessSecret)
				.accessToken(WPProperties.accessToken)
				.random(new Random())
				.clock(Clock.systemUTC())
				.build();
	}
	
	public Response invoke(Request unsignedRequest) {
		Request signedRequest = null;
		Response retVal = null;

		String url = unsignedRequest.urlString();
		try {
			signedRequest = wpAuth.signRequest(unsignedRequest);
			logger.trace("Request url: " + url + " signed successfully");
		} catch(IOException ioe) {
			logger.error("Could not sign request with parameters in wp.properties", ioe);
			return retVal;
		}
		OkHttpClient client = new OkHttpClient();
		try {
			long startTime = System.currentTimeMillis();
			retVal = client.newCall(signedRequest).execute();
			logger.debug("Total time taken for invoking url: " + url + " " + (System.currentTimeMillis() - startTime));
			logger.debug("Response code: "+ retVal.code());
		} catch(IOException e) {
			logger.error("Failed to invoke the url: " + url, e);
		}
		return retVal;
	}
	
	public Response invoke(String url) {
		Request unSignedRequest = new Request.Builder()
				.url(url)
				.get()
				.build();
		return invoke(unSignedRequest);
	}

	public static WPRestInvoker getInvoker() {
		if(invoker == null) {
			synchronized(WPRestInvoker.class) {
				if(invoker == null) {
					invoker = new WPRestInvoker();
					invoker.buildWPAuth();
				}
			}
		}
		return invoker;
	}
}
