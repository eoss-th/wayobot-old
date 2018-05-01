package com.eoss.appengine.dao;
import com.eoss.appengine.bean.AccessLog;
import com.google.appengine.api.datastore.Entity;

public class AccessLogDAO extends DatastoreDAO{
	private final String Tag_Name ="AccessLog";
	public String addAccessLog(AccessLog accesslog) {
		Entity ent = new Entity(Tag_Name,Long.toString(System.nanoTime()));
		ent.setProperty("time", accesslog.getTimeStamp());
		ent.setProperty("reqIp", accesslog.getReqIp());
		ent.setProperty("reqPath", accesslog.getReqPath());
		ent.setProperty("reqHost", accesslog.getReqHost());
		ent.setProperty("reqQueryString", accesslog.getReqQueryString());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
}
