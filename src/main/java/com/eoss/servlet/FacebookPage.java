package com.eoss.servlet;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class FacebookPage {

	public static final String GRAPH_URL = "https://graph.facebook.com/v2.10/";

	private URLFetchService url_service = URLFetchServiceFactory.getURLFetchService();
	
	private FacebookDSConfig facebookDSConfig;

	public FacebookPage(FacebookDSConfig facebookConfig) {
		this.facebookDSConfig = facebookConfig;
	}

	public HTTPResponse publish(String message) throws Exception {

		String url = GRAPH_URL + facebookDSConfig.name + "/feed";
		HTTPRequest request = new HTTPRequest(new URL(url), HTTPMethod.POST);
		
		Map<String, String> params = new HashMap<>();
		params.put("message", message);
		params.put("access_token", facebookDSConfig.pageAccessToken);
		request.setPayload(getPostData(params));

		return url_service.fetch(request);
	}

	private byte[] getPostData(Map<String, String> map) { 
		StringBuilder sb = new StringBuilder(); 
		for (Map.Entry<String, String> entry : map.entrySet()) { 
			sb.append(entry.getKey()); 
			sb.append('='); 
			sb.append(entry.getValue()); 
			sb.append('&'); 
		} 
		if (sb.length() > 0) { 
			sb.setLength(sb.length() - 1); // Remove the trailing &. 
		} 
		return sb.toString().getBytes(StandardCharsets.UTF_8); 
	} 
}
