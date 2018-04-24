package com.eoss.appengine.dao;


import java.util.ArrayList;
import java.util.List;
import com.eoss.appengine.bean.Orders;
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
public class OrdersDAO extends DatastoreDAO{
	private final String Tag_Name ="Orders";
	public String addOrders(Orders o) {
		
		Entity ent = new Entity(Tag_Name,o.getOrderId());
		ent.setProperty("botId", o.getBotId());
		ent.setProperty("accountId", o.getAccountId());
		ent.setProperty("Price", o.getPrice());
		ent.setProperty("status", o.getStatus());
		ent.setProperty("buyTime", o.getBuyTime().toString());
		ent.setProperty("invoice", o.getInvoice());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
	}
	

	public List<Entity> getOrdersList(Orders o) {
		List<Entity> el = new ArrayList<Entity>();
		Query q;
		//if input only orderId
		if(o.getOrderId() != "" && o.getOrderId() != null) {
			try {
				el.add(getDatastoreService().get(KeyFactory.createKey(Tag_Name, o.getOrderId())));
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return el;
		}
		//if input accountId/botId/status
		if((o.getAccountId() != "" && o.getAccountId() != null) && (o.getBotId() !="" && o.getBotId() != null) && (o.getStatus() != "" && o.getStatus() != null)) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, o.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("botId", FilterOperator.EQUAL, o.getBotId());
	        FilterPredicate property3Filter = new FilterPredicate("status", FilterOperator.EQUAL, o.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter,property3Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input accountId/botId
		if((o.getAccountId() != "" && o.getAccountId() != null) && (o.getBotId() !="" && o.getBotId() != null)) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, o.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("botId", FilterOperator.EQUAL, o.getBotId());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter).addSort("buyTime", SortDirection.DESCENDING);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input accountId/status
		if((o.getAccountId() != "" && o.getAccountId() != null) && (o.getStatus() !="" && o.getStatus()!= null)) {
	        FilterPredicate property1Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, o.getAccountId());
	        FilterPredicate property2Filter = new FilterPredicate("status", FilterOperator.EQUAL, o.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}	
		//if input botId/status
		if((o.getBotId() != "" && o.getBotId() != null) && (o.getStatus() !="" && o.getStatus()!= null)) {
	        FilterPredicate property1Filter = new FilterPredicate("botId", FilterOperator.EQUAL, o.getBotId());
	        FilterPredicate property2Filter = new FilterPredicate("status", FilterOperator.EQUAL, o.getStatus());
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
			q = new Query(Tag_Name).setFilter(compositeFilter);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input only accountId
		if(o.getAccountId() != "" && o.getAccountId() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("accountId", FilterOperator.EQUAL, o.getAccountId())).addSort("buyTime", SortDirection.DESCENDING);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}
		//if input only botId
		if(o.getBotId() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("botId", FilterOperator.EQUAL, o.getBotId())).addSort("buyTime", SortDirection.ASCENDING);
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}		
		//if input only invoice
		if(o.getInvoice() != "" && o.getInvoice() != null) {
			q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("invoice", FilterOperator.EQUAL, o.getInvoice()));
			for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
			}					
			return el;
		}		
		//if input nothing
		q = new Query(Tag_Name).addSort("buyTime", SortDirection.ASCENDING);
		for (Entity entity : getDatastoreService().prepare(q).asIterable()) {
				el.add(entity);
		}		
		return el;
	}
	public Entity getOrders(String invoice) {
		Entity ent = null;
		Query q = new Query(Tag_Name).setFilter(new Query.FilterPredicate("invoice", FilterOperator.EQUAL, invoice));
		ent = getDatastoreService().prepare(q).asSingleEntity();

		return ent;
	}
	
	public String delOrders(Orders o) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, o.getOrderId()));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
}
