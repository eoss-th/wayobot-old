package com.eoss.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.brain.MessageObject;
import com.eoss.brain.Session;
import com.eoss.brain.command.wakeup.WakeupCommandNode;
import com.eoss.brain.net.Context;
import com.eoss.brain.net.gcs.GcsContext;

@SuppressWarnings("serial")
public class BotServlet extends HttpServlet {
	
	private static final String bucket = "eoss-chatbot";
		
	Map<String, Context> contextMap = new HashMap<>();
	Map<String, Session> sessionMap = new HashMap<>();
	
	Context getContext(String uri) {
		Context context = contextMap.get(uri);
		if (context==null) {
			context = new GcsContext(bucket, uri);
		}
		contextMap.put(uri, context);
		return context;
	}
	
	Session getSession(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String sessionId = req.getParameter("sessionId");		
		Session session = sessionMap.get(sessionId);
		if (session==null) {
			session = new Session(getContext(uri));
			session.learning = true;
			new WakeupCommandNode(session).execute(null);
		}
		sessionMap.put(sessionId, session);
		return session;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");		
		Session session = getSession(req);		
		String message = req.getParameter("message");
		resp.getWriter().print(session.parse(MessageObject.build(message)));
	}

}