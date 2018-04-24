package com.eoss.appengine.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class DatastoreDAO {

	protected DatastoreService getDatastoreService() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		return ds;
	}
	
}
