package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class GetShowcaseList extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	private final int PAGE_SIZE = 10;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getParameter("accountId")==null){
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);
			String startCursor = req.getParameter("cursor");
			System.out.println("startCursor" +startCursor);
		    if (startCursor != null) {
		    	System.out.println("start" +Cursor.fromWebSafeString(startCursor));
		        fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
		    }
		    
		    Query q = new Query("ShowCase").setFilter(new Query.FilterPredicate("publish", FilterOperator.EQUAL, true)).addSort("timeStamp", SortDirection.DESCENDING);
		    PreparedQuery pq = ds.prepare(q);
		    QueryResultList<Entity> results = null;
		    
		    try {
		        results = pq.asQueryResultList(fetchOptions);
		    } catch (IllegalArgumentException e) {
		    	System.out.println("err"+e.getMessage());
		    }
		    String json = srp.parseJsonStatus("getShowCase", srp.parseJsonEntityList(results), results.getCursor().toWebSafeString());
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(json);
			
		}else {
			
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			String accountId = req.getParameter("accountId");
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);
			String startCursor = req.getParameter("cursor");
		    if (startCursor != null) {
		        fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
		    }
		    
	        FilterPredicate property1Filter = new FilterPredicate("publish", FilterOperator.EQUAL, true);
	        FilterPredicate property2Filter = new FilterPredicate("accountId", FilterOperator.EQUAL, accountId);
	        CompositeFilter compositeFilter = CompositeFilterOperator.and(property1Filter, property2Filter);
	        Query q = new Query("ShowCase").setFilter(compositeFilter).addSort("timeStamp", SortDirection.DESCENDING);
		    
		    PreparedQuery pq = ds.prepare(q);
		    QueryResultList<Entity> results = null;
		    
		    try {
		        results = pq.asQueryResultList(fetchOptions);
		    } catch (IllegalArgumentException e) {
		    	System.out.println("err"+e.getMessage());
		    }
		    String json = srp.parseJsonStatus("getShowCase", srp.parseJsonEntityList(results), results.getCursor().toWebSafeString());
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(json);			
		}
		
	}
	

}
