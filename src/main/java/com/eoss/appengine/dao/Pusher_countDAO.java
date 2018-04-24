package com.eoss.appengine.dao;
import com.eoss.appengine.bean.Pusher_count;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class Pusher_countDAO extends DatastoreDAO{
	private final String Tag_Name ="Pusher_count";
	
	public String addPusherCount(Pusher_count p_count) {
		String id = p_count.getAccountId()+"_"+p_count.getTimeStamp();
		AccountDAO accountDao = new AccountDAO();
		Integer maxCount = p_count.getMaxCount();
		Entity accountEnt = accountDao.getAccount(p_count.getAccountId());
		if(accountEnt.getProperty("paid").toString().equals("true")) {
			maxCount = 100000;
		}
		Entity ent = new Entity(Tag_Name,id);
		ent.setProperty("accountId", p_count.getAccountId());
		ent.setProperty("timeStamp", p_count.getTimeStamp());
		ent.setProperty("count", p_count.getCount());
		ent.setProperty("maxCount", maxCount);
		
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			return e.getMessage();
		}
		
		return "success";
	}	
	
	public Entity getPusherCount(Pusher_count p_count){
		
		String id = p_count.getAccountId()+"_"+p_count.getTimeStamp();
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, id));
		} catch (EntityNotFoundException e) {
			
		}
		return ent;
	}
}
