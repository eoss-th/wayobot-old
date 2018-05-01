package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class CleanAccesslogService extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogCleanUp(resp);
	}
	
	public void LogCleanUp(HttpServletResponse response) {		
		int limit = 10000;
		Query query = new Query("AccessLog");
		query.setKeysOnly();

		Filter offerf =
				new FilterPredicate("_createAt", FilterOperator.LESS_THAN, System.currentTimeMillis() - 604800000);
		// expired a week ago
		query.setFilter(offerf);

		ArrayList<Key> killList = new ArrayList<Key>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		PreparedQuery pq = datastore.prepare(query);
		Iterable<Entity> entities = pq.asIterable(FetchOptions.Builder
				.withLimit(limit));

		for (Entity expiredSession : entities) {
			System.out.println(expiredSession.getKey());
			Key key = expiredSession.getKey();
			killList.add(key);
		}
		System.out.println(killList);
		datastore.delete(killList);
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			response.getWriter().println("Deleted " + killList.size() + " expired sessions.");
		} catch (IOException ex) {

		} 
	}
}
