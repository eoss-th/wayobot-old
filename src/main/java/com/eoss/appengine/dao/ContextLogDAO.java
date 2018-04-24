package com.eoss.appengine.dao;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class ContextLogDAO extends DatastoreDAO{
	private final String Tag_Name ="contextLog";
	public Entity getContextLog(String accountId,String botId,String chanel,String type) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, accountId+"-"+botId+"-"+chanel+"-"+type));
		} catch (EntityNotFoundException e) {
			ent = null;
		}

		return ent;
	}
}
