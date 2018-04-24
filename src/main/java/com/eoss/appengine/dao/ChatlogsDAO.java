package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;
import com.eoss.appengine.bean.ChatLog;
import com.eoss.brain.MessageObject;
import com.eoss.brain.Session;
import com.eoss.servlet.SessionManager;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
//extend from com.eoss.appengine.dao.initial.class
public class ChatlogsDAO extends DatastoreDAO{
	private final String Tag_Name ="Chatlogs";
	
	public String getChanel() {
		String Chanel = Long.toString(System.currentTimeMillis());
		return Chanel;
	}
	
	public String addChatLog(ChatLog m) {
		
		Entity ent = new Entity(Tag_Name,Long.toString(System.nanoTime()));
		ent.setProperty("chanelName", m.getChanelName());
		ent.setProperty("message", m.getMessage());
		ent.setProperty("timeStamp", m.getTimeStamp().toString());
		ent.setProperty("readFlag", m.isReadFlag());
		ent.setProperty("accountId", m.getAccountId());
		ent.setProperty("botId", m.getBotId());
		ent.setProperty("replyMessage", m.getReplyMessage());
		ent.setProperty("contextName", m.getContextName());
		ent.setProperty("replyLog", m.getReplyLog());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
	
	public List<Entity> getChatLog(String chanelName,String botId) {
		
		List<Entity> el = new ArrayList<Entity>();
		Query q;
        FilterPredicate property1Filter = new FilterPredicate("chanelName", FilterOperator.EQUAL, chanelName);
        FilterPredicate property2Filter = new FilterPredicate("botId", FilterOperator.EQUAL, botId);
        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
		q = new Query(Tag_Name).setFilter(compositeFilter).addSort("timeStamp", SortDirection.ASCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			el.add(entity);
		}					
		return el;
		
	}
	
	public List<Entity> getChatLogByBotId(String userBotId,Boolean readFlag) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
        FilterPredicate property1Filter = new FilterPredicate("botId", FilterOperator.EQUAL, userBotId);
        FilterPredicate property2Filter = new FilterPredicate("readFlag", FilterOperator.EQUAL, readFlag);
        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
		q = new Query(Tag_Name).setFilter(compositeFilter).addSort("timeStamp", SortDirection.DESCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			el.add(entity);
		}					
		return el;
	}
	
	public List<Entity> getChatLogByContextName(String contextName,Boolean readFlag) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
        FilterPredicate property1Filter = new FilterPredicate("contextName", FilterOperator.EQUAL, contextName);
        FilterPredicate property2Filter = new FilterPredicate("readFlag", FilterOperator.EQUAL, readFlag);
        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
		q = new Query(Tag_Name).setFilter(compositeFilter).addSort("timeStamp", SortDirection.DESCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			el.add(entity);
		}					
		return el;
	}

	public void clear(String contextName, boolean readFlag) {
		Query q;
        FilterPredicate property1Filter = new FilterPredicate("contextName", FilterOperator.EQUAL, contextName);
        FilterPredicate property2Filter = new FilterPredicate("readFlag", FilterOperator.EQUAL, readFlag);
        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
		q = new Query(Tag_Name).setFilter(compositeFilter).addSort("timeStamp", SortDirection.DESCENDING);
			
		Session session = SessionManager.instance().get("admin", contextName);		
		String message, response;
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			message = ((Text) entity.getProperty("message")).getValue();
			response = session.parse(MessageObject.build(message));
					
			if (!response.isEmpty()) {
				
				entity.setProperty("readFlag", true);
				getDatastoreService().put(entity);
			}
		}					
	}
	
	
}
