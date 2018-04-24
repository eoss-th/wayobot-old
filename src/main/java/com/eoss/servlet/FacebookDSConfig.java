package com.eoss.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class FacebookDSConfig {
	
	private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public final String name;
	
	public final String appSecret;
	
	public final String pageAccessToken;
	
	public final String verifyToken;
	
	public final String languageTag;
	
	public FacebookDSConfig(String name) throws EntityNotFoundException {
		Key employeeKey = KeyFactory.createKey("facebook", name);
		Entity facebook = datastore.get(employeeKey);
		appSecret = facebook.getProperty("APP_SECRET").toString();
		pageAccessToken = facebook.getProperty("PAGE_ACCESS_TOKEN").toString();
		verifyToken = facebook.getProperty("VERIFY_TOKEN").toString();
		languageTag = facebook.getProperty("LANGUAGE_TAG").toString();		
		this.name = name;
	}

}
