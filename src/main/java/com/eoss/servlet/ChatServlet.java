package com.eoss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.*;

@SuppressWarnings("serial")
@WebServlet(name = "chat2", urlPatterns = { "/chat2/*" })

public class ChatServlet extends HttpServlet {
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// pre-flight request processing
		resp.setHeader("Access-Control-Allow-Origin", "*");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String cmd = req.getParameter("cmd");
		String roomName = req.getParameter("room_name");
		System.out.println(cmd);
		if(cmd.equals("query")) {
			Query q = new Query(roomName).addSort("PostTime", SortDirection.ASCENDING);
			List<Entity> el = new ArrayList<Entity>();
			for (Entity entity : ds.prepare(q).asIterable(FetchOptions.Builder.withLimit(10))) {
				el.add(entity);
			}
			String json = new Gson().toJson(el);
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(json);		
			
		}else {
			Entity e = new Entity(roomName);
			e.setProperty("UserName", req.getParameter("user"));
			e.setProperty("Message", req.getParameter("text"));
			e.setProperty("PostTime", new Date());
			ds.put(e);		
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write("Save Success");
			
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
