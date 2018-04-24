package com.eoss.appengine.dao;
import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.CssTemplate;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
//extend from com.eoss.appengine.dao.initial.class
public class CssDAO extends DatastoreDAO{
	private final String Tag_Name ="CssTemplate";
	
	public String addCssTemplate(CssTemplate c) {
		Entity ent = new Entity(Tag_Name,c.getCssId());
		ent.setProperty("cssFilePath", c.getCssFilePath());
		ent.setProperty("cssName", c.getCssName());
		ent.setProperty("price", c.getPrice());

		
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public List<Entity> getCssTemplate(CssTemplate c) {
		List<Entity> el = new ArrayList<Entity>();
		if(c.getCssId()!="" && c.getCssId() != null) {
			try {
				Entity ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, c.getCssId()));
				el.add(ent);
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return el;
		}
		Query q = new Query(Tag_Name).addSort("cssId", SortDirection.ASCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
		}
		return el;
	}
	
	public String delCssTemplate(CssTemplate c) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, c.getCssId()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
}
