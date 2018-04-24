package com.eoss.appengine.dao;

import com.eoss.appengine.bean.Debug;
import com.google.appengine.api.datastore.Entity;

public class DebugDAO extends DatastoreDAO{
	private final String Tag_Name ="Debug";
	public String addDebug(Debug debug) {
		Entity ent = new Entity(Tag_Name,Long.toString(System.nanoTime()));
		ent.setProperty("timeStamp", debug.getTimeStamp());
		ent.setProperty("message", debug.getMessage());

		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}		
		return "success";
	}
}
