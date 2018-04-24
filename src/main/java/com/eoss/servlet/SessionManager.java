package com.eoss.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.eoss.brain.Session;
import com.eoss.brain.command.wakeup.WakeupCommandNode;
import com.eoss.brain.net.Context;
import com.eoss.brain.net.gcs.GcsContext;

public class SessionManager {
	
	private static final int minutes = 10;
	
	private static final String bucket = System.getenv("bucket");
	
	private Map<String, Context> contextMap = new HashMap<>();
	
	private Map<String, Session> sessionMap = new HashMap<>();
	private Map<String, Long> sessionTimestampMap = new TreeMap<>();
	
	private Map<String, List<String>> contextToSessionIdMap = new HashMap<>();
	
	private static SessionManager sessionManager;
	
	private SessionManager() {}
	
	public static SessionManager instance() {
		if (sessionManager==null) {
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}
		
	public synchronized Context context(String contextName) {
		return new GcsContext(bucket, contextName);
	}
	
	private synchronized Context getContext(String contextName) {
		Context context = contextMap.get(contextName);
		if (context==null) {
			System.out.println("creat new Context :"+ contextName);
			context = new GcsContext(bucket, contextName);			
			contextMap.put(contextName, context);
		}
		
		return context;
	}
	
	
	public synchronized Session get(String sessionId, String contextName) {
		
		Session session = sessionMap.get(sessionId);
		if (session==null) {			
			session = new Session(getContext(contextName));
			new WakeupCommandNode(session).execute(null);
						
			String language = session.context.properties.get("language");
			
			System.out.println(session.context.properties.toString());
			session.context.locale(new Locale(language));
			
			sessionMap.put(sessionId, session);
			
			List<String> sessionIdList = contextToSessionIdMap.get(contextName);
			if (sessionIdList==null) {
				sessionIdList = new ArrayList<>();
			}
			sessionIdList.add(sessionId);
			contextToSessionIdMap.put(contextName, sessionIdList);
		}
		
		sessionTimestampMap.put(sessionId, System.currentTimeMillis());
		
		return session;
	}
	
	public synchronized void clearContext(String contextName) {
		contextMap.remove(contextName);
		List<String> sessionRemoveList = contextToSessionIdMap.get(contextName);
		if (sessionRemoveList!=null) {
			for (String sessionId:sessionRemoveList) {			
				sessionMap.remove(sessionId);
				sessionTimestampMap.remove(sessionId);			
			}			
		}
	}
	
	public void clean() {
		
		synchronized (sessionMap) {
			long now = System.currentTimeMillis();
			
			List<String> sessionRemoveList = new ArrayList<>();
			//Clear Session
			Session session;
			List<String> sessionIdList;
			for (Map.Entry<String, Long> entry:sessionTimestampMap.entrySet()) {
				if (now-entry.getValue() > 1000 * 60 * minutes) {
						
					session = sessionMap.get(entry.getKey());
					sessionRemoveList.add(entry.getKey());
										
					sessionIdList = contextToSessionIdMap.get(session.context.name);
					sessionIdList.remove(entry.getKey());
				}
			}
				
			for (String sessionId:sessionRemoveList) {
				
				sessionMap.remove(sessionId);
				sessionTimestampMap.remove(sessionId);			
				
			}			
		}
		
		synchronized (contextMap) {
			List<String> contextRemoveList = new ArrayList<>();
			
			//Clear Context
			for (Map.Entry<String, List<String>> entry:contextToSessionIdMap.entrySet()) {
				if (entry.getValue().isEmpty()) {
					contextRemoveList.add(entry.getKey());
				}
			}
			
			for (String contextName:contextRemoveList) {
				contextMap.remove(contextName);
				contextToSessionIdMap.remove(contextName);				
			}
		}
	}
	
}
