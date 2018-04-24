package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.Account;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
//extend from com.eoss.appengine.dao.initial.class
public class AccountDAO extends DatastoreDAO{
	private final String Tag_Name ="Account";
	public String addAccount(Account acc) {
		Entity ent = new Entity(Tag_Name,acc.getAccountId());
		ent.setProperty("accName", acc.getAccName());
		ent.setProperty("email", acc.getEmail());
		ent.setProperty("accRegisDate", acc.getAccRegisDate().toString());
		ent.setProperty("editDate", acc.getEditDate().toString());
		ent.setProperty("pusherKey", acc.getPusherKey());
		ent.setProperty("pusherCluster", acc.getPusherCluster());
		ent.setProperty("app_id", acc.getApp_id());
		ent.setProperty("app_secret", acc.getApp_secret());
		ent.setProperty("paid", acc.getPaid());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public List<Entity> getAccountList(Account acc) {
		List<Entity> el = new ArrayList<Entity>();
		if(acc.getAccountId() == "" || acc.getAccountId() == null) {
			
			Query q = new Query(Tag_Name).addSort("accRegisDate", SortDirection.ASCENDING);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
					el.add(entity);
			}
			
		}else {
			
			try {
				Entity ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, acc.getAccountId()));
				el.add(ent);
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return el;
			   
		}
		return el;
		
	}
	
	public Entity getAccount(String accId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, accId));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return ent;
	}
	
	public String delAccount(Account acc) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, acc.getAccountId()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
}
