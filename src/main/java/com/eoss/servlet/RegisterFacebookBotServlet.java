package com.eoss.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class RegisterFacebookBotServlet extends HttpServlet {
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		String [] paths = req.getRequestURI().split("/", 3);
		String name = paths[2];			
		
		Key employeeKey = KeyFactory.createKey("facebook", name);
		try {
			Entity facebook = datastore.get(employeeKey);
			String appSecret = facebook.getProperty("APP_SECRET").toString();
			String pageAccessToken = facebook.getProperty("PAGE_ACCESS_TOKEN").toString();
			String verifyToken = facebook.getProperty("VERIFY_TOKEN").toString();
			String languageTag = facebook.getProperty("LANGUAGE_TAG").toString();
			resp.getWriter().println(appSecret);
			resp.getWriter().println(pageAccessToken);
			resp.getWriter().println(verifyToken);
			resp.getWriter().println(Locale.forLanguageTag(languageTag).getLanguage());
			resp.getWriter().println(Locale.forLanguageTag(languageTag).getCountry());
		} catch (EntityNotFoundException expected) {
			throw new RuntimeException(expected);
		}
	}

}