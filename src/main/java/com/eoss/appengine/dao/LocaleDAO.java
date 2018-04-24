package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.Locale;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class LocaleDAO extends DatastoreDAO{
	private final String Tag_Name ="Locale";
	public String addLocale(Locale locale) {
		Entity ent = new Entity(Tag_Name,locale.getLocaleId());
		ent.setProperty("locale", locale.getLocale());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public List<Entity> getLocaleList(Locale locale) {
		List<Entity> el = new ArrayList<Entity>();
		
		Query q = new Query(Tag_Name);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			el.add(entity);
		}

		return el;
		
	}
}
