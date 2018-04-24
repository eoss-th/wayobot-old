package com.eoss.appengine.dao;

import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.SubAccount;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
//extend from com.eoss.appengine.dao.initial.class
public class SubAccountDAO extends DatastoreDAO{
	private final String Tag_Name ="SubAccount";
	public String addSubAccount(SubAccount u) {
		Entity ent = new Entity(Tag_Name,u.getUserName());
		ent.setProperty("accountId", u.getAccountId());
		ent.setProperty("passWord", u.getPassWord());
		ent.setProperty("subAccName", u.getSubAccName());
		ent.setProperty("role", u.getRole());
		ent.setProperty("subRegisDate", u.getSubRegisDate().toString());
		ent.setProperty("editDate", u.getEditDate().toString());
		ent.setProperty("tel", u.getTel());
		ent.setProperty("email", u.getEmail());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
	
	public List<Entity> getSubAccountList(SubAccount u) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
		//input UserName
		if(u.getUserName() != "" && u.getUserName() != null) {
			
			try {
				Entity ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, u.getUserName()));
				el.add(ent);
				
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("user not found");
			}
			return el;
			
		}
		//input AccountId
		if(u.getAccountId() != "" && u.getAccountId() != null) {
			
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("accountId", FilterOperator.EQUAL, u.getAccountId()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		
		//input email
		if(u.getEmail() != "" && u.getEmail() != null) {
			
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate( "email", FilterOperator.EQUAL, u.getEmail()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
			
		}
		
		//not input anything
		q = new Query(Tag_Name);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
		}
		return el;
	
	}	
	
	public Entity getSubAccount (String email) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, email));
		} catch (EntityNotFoundException e) {

		}
		return ent;
	}
	
	
	public String delSubAccount(SubAccount u) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, u.getUserName()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
}
