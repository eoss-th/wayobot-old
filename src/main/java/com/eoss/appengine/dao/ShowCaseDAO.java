package com.eoss.appengine.dao;
import java.util.ArrayList;
import java.util.List;

import com.eoss.appengine.bean.ShowCase;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;


public class ShowCaseDAO extends DatastoreDAO{
	private final String Tag_Name ="ShowCase";
	
	
	public String addShowCase(ShowCase showcase) {
		
		Entity ent = new Entity(Tag_Name,showcase.getBotId());
		ent.setProperty("publish", showcase.getPublish());
		ent.setProperty("timeStamp", showcase.getTimeStamp());
		ent.setProperty("accountId", showcase.getAccountId());
		ent.setProperty("hasImage", showcase.getHasImage());
		ent.setProperty("title", showcase.getTitle());
		ent.setProperty("description", showcase.getDescription());
		ent.setProperty("owner", showcase.getOwner());
		ent.setProperty("viewCount", showcase.getViewCount());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	

	public Entity getShowCase(String botId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, botId));
		} catch (EntityNotFoundException e) {

		}
		return ent;

	}
	
	public List<Entity> getShowcaseList(String accountId,Boolean publish) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, accountId);
        FilterPredicate property2Filter = new FilterPredicate("publish", FilterOperator.EQUAL, publish);
        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
		q = new Query(Tag_Name).setFilter(compositeFilter).addSort("timeStamp", SortDirection.ASCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
			el.add(entity);
		}					
		return el;
	}
}
