package com.eoss.appengine.dao;

import com.eoss.appengine.bean.Message_count;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;


public class Message_countDAO extends DatastoreDAO{
	
	private final String Tag_Name ="Message_count";
	
	public String addMessageCount(Message_count msc) {
		String id = msc.getAccountId()+"_"+msc.getBotId()+"_"+msc.getTimeStamp();
		
		Entity ent = new Entity(Tag_Name,id);
		ent.setProperty("accountId", msc.getAccountId());
		ent.setProperty("timeStamp", msc.getTimeStamp());
		ent.setProperty("count", msc.getCount());
		ent.setProperty("botId", msc.getBotId());
		
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			return e.getMessage();
		}
		
		return "success";
	}
	
	public Entity getMessageCount(Message_count msc){
		
		String id = msc.getAccountId()+"_"+msc.getBotId()+"_"+msc.getTimeStamp();

		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, id));
		} catch (EntityNotFoundException e) {
			
		}
		return ent;
	}
	
}
