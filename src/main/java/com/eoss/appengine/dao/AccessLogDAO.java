package com.eoss.appengine.dao;
import com.eoss.appengine.bean.AccessLog;
import com.google.appengine.api.datastore.Entity;

public class AccessLogDAO extends DatastoreDAO{
	private final String Tag_Name ="AccessLog";
	public String addAccessLog(AccessLog accesslog) {
		long currentTimeMillis = System.currentTimeMillis();
		Entity ent = new Entity(Tag_Name,currentTimeMillis);
		ent.setProperty("time", accesslog.getTimeStamp());
		ent.setProperty("reqIp", accesslog.getReqIp());
		ent.setProperty("reqPath", accesslog.getReqPath());
		ent.setProperty("reqHost", accesslog.getReqHost());
		ent.setProperty("reqQueryString", accesslog.getReqQueryString());
		ent.setProperty("reqHeader", accesslog.getReqHeader());
		ent.setProperty("_createAt", currentTimeMillis);
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
}
