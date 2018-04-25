package com.eoss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.eoss.appengine.helper.SetRespPram;
import com.eoss.brain.net.Context;

@SuppressWarnings("serial")
public class GetContext extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	 @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String accountId = (String) req.getSession().getAttribute("accountId");
		 String userBotId = req.getParameter("userBotId");
		 String contextName = accountId+"/"+userBotId;
		 
		 System.out.println(contextName);
		 Context context = SessionManager.instance().context(contextName);
		 try {
			context.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 
		 JSONObject object = new JSONObject(context.properties);
		 String json = srp.parseJsonObject(object);
		 
		 resp = srp.setRespHead(resp,System.getenv("domain"));
		 resp.getWriter().write(json);
	}

	 @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 	String status = "fail";
		 	String userBotId = req.getParameter("userBotId");
		 	String accountId = (String) req.getSession().getAttribute("accountId");
		 	String contextName = accountId+"/"+userBotId;
		 	String language = req.getParameter("language");
		 	
		 	System.out.println(language);
		 	if(checkParameter(req.getParameter("greeting"),req.getParameter("unknown"),req.getParameter("title"))) {
		 		status = "success";
		 		try {
					Context context = SessionManager.instance().context(contextName);
					context.load();
					context.properties.put("language", language);
					context.properties.put("greeting", req.getParameter("greeting"));
					context.properties.put("unknown", req.getParameter("unknown"));		
					context.properties.put("title", req.getParameter("title"));		
					context.save();
					SessionManager.instance().clearContext(contextName);
				}catch (Exception e) {
					status = e.getMessage();
				}	
			 	
		 	}

			
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(status);
	}
	 
		private Boolean checkParameter(String greet,String unknown,String title) {

			if( greet == null || greet.isEmpty()) {	
				return false;		
			}
			if (unknown == null || unknown.isEmpty()) {			
				return false;
			}
			if (title == null || title.isEmpty()) {			
				return false;
			}
			return true;
		}	 
}
