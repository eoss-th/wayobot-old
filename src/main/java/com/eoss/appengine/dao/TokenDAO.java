package com.eoss.appengine.dao;

import java.util.UUID;

import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.helper.SetReqParam;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
//extend from com.eoss.appengine.dao.initial.class
public class TokenDAO extends DatastoreDAO{
	private final String Tag_Name ="Token";
	private SetReqParam sr = new SetReqParam();
	
	public Entity generateToken(SubAccount u,int i) {
		String token = UUID.randomUUID().toString().toUpperCase()+"_"+u.getUserName();
		Entity ent = new Entity(Tag_Name,u.getUserName());
		ent.setProperty("tokenId", token);
		ent.setProperty("timeStamp", sr.getCurrentDate().toString());
		ent.setProperty("expire", sr.getExpireDate(sr.getCurrentDate(),i).toString());
		
		getDatastoreService().put(ent);
		
		return ent;
	}
	public Entity getToken(SubAccount u) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, u.getUserName()));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ent;
		
	}
}
