package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.UserBots;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

//extend from com.eoss.appengine.dao.initial.class
public class UserBotsDAO extends DatastoreDAO{
	private final String Tag_Name ="UserBots";
	
	public String addUserBots(UserBots u) {
		Entity ent = new Entity(Tag_Name,u.getUserBotId());
		ent.setProperty("botId", u.getBotId());
		ent.setProperty("accountId", u.getAccountId());
		ent.setProperty("expireDate", u.getExpireDate().toString());
		ent.setProperty("buyDate", u.getBuyDate().toString());
		ent.setProperty("remainMessage", u.getRemainMessage());
		ent.setProperty("toDayMessage", u.getToDayMessage());
		ent.setProperty("messageCount", u.getMessageCount());
		ent.setProperty("cssId", u.getCssId());
		ent.setProperty("status", u.getStatus());
		ent.setProperty("notiNumber", u.getNotiNumber());
		ent.setProperty("country", u.getCountry());
		ent.setProperty("BuyFlag", u.getBuyFlag());
		ent.setProperty("paid", u.getPaid());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
	

	public List<Entity> getUserBotsList(UserBots u) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
		//if input only UserBotId
		if(u.getUserBotId() != null) {
			
			try {
				Entity ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, u.getUserBotId()));
				el.add(ent);
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return el;
		}
		
		//if input accountId/botId/status
		if(u.getAccountId() != null && u.getBotId() != null &&  u.getStatus() != null) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, u.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("botId", FilterOperator.EQUAL, u.getBotId());
	        FilterPredicate property3Filter = new FilterPredicate("status", FilterOperator.EQUAL, u.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter,property3Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input accountId/botId
		if(u.getAccountId() != null && u.getBotId() != null) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, u.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("botId", FilterOperator.EQUAL, u.getBotId());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input accountId/status
		if(u.getAccountId() != null && u.getStatus()!= null) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, u.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("status", FilterOperator.EQUAL, u.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}	
		//if input botId/status
		if( u.getBotId() != null && u.getStatus()!= null) {
	        FilterPredicate property1Filter = new FilterPredicate("botId", FilterOperator.EQUAL, u.getBotId());
	        FilterPredicate property2Filter = new FilterPredicate("status", FilterOperator.EQUAL, u.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input only accountId
		if(u.getAccountId() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("accountId", FilterOperator.EQUAL, u.getAccountId()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input only botId
		if(u.getBotId() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("botId", FilterOperator.EQUAL, u.getBotId()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}	
		
		//if input only userBotId
		if(u.getUserBotId() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("botId", FilterOperator.EQUAL, u.getBotId()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}		
		//if input nothing
		q = new Query(Tag_Name).addSort("buyDate", SortDirection.ASCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
		}		
		return el;		
	}
	
	public Entity getByUserBotId(String userBotId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, userBotId));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ent;
	}
	
	
	public String delUserBots(UserBots u) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, u.getUserBotId()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
	
}
