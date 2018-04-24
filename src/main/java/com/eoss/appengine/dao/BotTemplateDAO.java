package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.BotTemplate;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

//extend from com.eoss.appengine.dao.initial.class
public class BotTemplateDAO extends DatastoreDAO{
	private final String Tag_Name ="BotTemplate";
	
	public String addBotTemplate(BotTemplate b) {
		Entity ent = new Entity(Tag_Name,b.getBotId());
		ent.setProperty("price", b.getPrice());
		ent.setProperty("info", b.getInfo());
		ent.setProperty("botName", b.getBotName());
		ent.setProperty("age", b.getAge());
		
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public List<Entity> getBotTemplateList(BotTemplate b) {
		List<Entity> el = new ArrayList<Entity>();
		if(b.getBotId() != null && !b.getBotId().isEmpty()) {
			try {
				Entity ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, b.getBotId()));
				el.add(ent);
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return el;
		}

		Query q = new Query(Tag_Name);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
		}
		
		return el;
	}
	
	public Entity getBotTemplate(String botId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, botId));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ent;
	}
	
	public String delBotTemplate(BotTemplate b) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, b.getBotId()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
}
