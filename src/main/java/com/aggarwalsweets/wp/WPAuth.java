package com.aggarwalsweets.wp;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import okio.Buffer;
import okio.ByteString;

public final class WPAuth implements Interceptor {
	private static final Escaper ESCAPER = UrlEscapers.urlFormParameterEscaper();
	private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	private static final String OAUTH_NONCE = "oauth_nonce";
	private static final String OAUTH_SIGNATURE = "oauth_signature";
	private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
	private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	private static final String OAUTH_ACCESS_TOKEN = "oauth_token";
	private static final String OAUTH_VERSION = "oauth_version";
	private static final String OAUTH_VERSION_VALUE = "1.0";

	private final String consumerKey;
	private final String consumerSecret;
	private final String accessToken;
	private final String accessSecret;
	private final Random random;
	private final Clock clock;

	private WPAuth(String consumerKey, String consumerSecret, String accessToken, String accessSecret,
			Random random, Clock clock) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessSecret = accessSecret;
		this.random = random;
		this.clock = clock;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		return chain.proceed(signRequest(chain.request()));
	}

	public Request signRequest(Request request) throws IOException {
		byte[] nonce = new byte[32];
		random.nextBytes(nonce);
		String oauthNonce = ((Long) random.nextLong()).toString();

		String oauthTimestamp = String.valueOf(clock.millis() / 1000);

		String consumerKeyValue = ESCAPER.escape(consumerKey);
		String accessTokenValue = ESCAPER.escape(accessToken);

		SortedMap<String, String> parameters = new TreeMap<>();
		parameters.put(OAUTH_CONSUMER_KEY, consumerKeyValue);

		if (StringUtils.isNotEmpty(accessTokenValue)) {
			parameters.put(OAUTH_ACCESS_TOKEN, accessTokenValue);
		}

		parameters.put(OAUTH_NONCE, oauthNonce);
		parameters.put(OAUTH_TIMESTAMP, oauthTimestamp);
		parameters.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
		parameters.put(OAUTH_VERSION, OAUTH_VERSION_VALUE);

		HttpUrl url = request.httpUrl();
	    for (int i = 0; i < url.querySize(); i++) {
	      parameters.put(ESCAPER.escape(url.queryParameterName(i)),
	          ESCAPER.escape(url.queryParameterValue(i)));
	    }

		Buffer base = new Buffer();
		String method = request.method();
		base.writeUtf8(method);
		base.writeByte('&');
		base.writeUtf8(ESCAPER.escape(request.httpUrl().newBuilder().query(null).build().toString()));
		base.writeByte('&');

		boolean first = true;
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			if (!first) {
				base.writeUtf8(ESCAPER.escape("&"));
			}
			first = false;
			base.writeUtf8(ESCAPER.escape(entry.getKey()));
			base.writeUtf8(ESCAPER.escape("="));
			base.writeUtf8(ESCAPER.escape(entry.getValue()));
		}

		String signingKey = ESCAPER.escape(consumerSecret) + "&" + ESCAPER.escape(accessSecret);

		SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalStateException(e);
		}
		byte[] result = mac.doFinal(base.readByteArray());
		String signature = ByteString.of(result).base64();

		StringBuilder sBuild = new StringBuilder();
		
		addToQueryParameterString(sBuild, OAUTH_CONSUMER_KEY, consumerKeyValue);
		addToQueryParameterString(sBuild, OAUTH_NONCE, oauthNonce);
		addToQueryParameterString(sBuild, OAUTH_SIGNATURE, signature);
		addToQueryParameterString(sBuild, OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
		addToQueryParameterString(sBuild, OAUTH_TIMESTAMP, oauthTimestamp);
		addToQueryParameterString(sBuild, OAUTH_VERSION, OAUTH_VERSION_VALUE);
		
		String authQuery = sBuild.toString();

		String url1 = request.httpUrl().newBuilder().query(authQuery).build().toString();
		return request.newBuilder().url(url1).build();
	}

	private void addToQueryParameterString(StringBuilder sBuild, String key, String value) {
		if(StringUtils.isNotEmpty(sBuild.toString())) {
			sBuild.append("&");
		}
		sBuild.append(key).append("=").append(value);
	}
	public static final class Builder {
		private String consumerKey;
		private String consumerSecret;
		private String accessToken;
		private String accessSecret;
		private Random random = new SecureRandom();
		private Clock clock = Clock.systemUTC();

		public Builder consumerKey(String consumerKey) {
			if (consumerKey == null)
				throw new NullPointerException("consumerKey = null");
			this.consumerKey = consumerKey;
			return this;
		}

		public Builder consumerSecret(String consumerSecret) {
			if (consumerSecret == null)
				throw new NullPointerException("consumerSecret = null");
			this.consumerSecret = consumerSecret;
			return this;
		}

		public Builder accessToken(String accessToken) {
			if (accessToken == null)
				throw new NullPointerException("accessToken == null");
			this.accessToken = accessToken;
			return this;
		}

		public Builder accessSecret(String accessSecret) {
			if (accessSecret == null)
				throw new NullPointerException("accessSecret == null");
			this.accessSecret = accessSecret;
			return this;
		}

		public Builder random(Random random) {
			if (random == null)
				throw new NullPointerException("random == null");
			this.random = random;
			return this;
		}

		public Builder clock(Clock clock) {
			if (clock == null)
				throw new NullPointerException("clock == null");
			this.clock = clock;
			return this;
		}

		public WPAuth build() {
			if (consumerKey == null)
				throw new IllegalStateException("consumerKey not set");
			if (consumerSecret == null)
				throw new IllegalStateException("consumerSecret not set");
			if (accessToken == null)
				throw new IllegalStateException("accessToken not set");
			if (accessSecret == null)
				throw new IllegalStateException("accessSecret not set");
			return new WPAuth(consumerKey, consumerSecret, accessToken, accessSecret, random, clock);
		}
	}
}
