package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.helper.SetRespPram;

@SuppressWarnings("serial")
public class MessageService extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp = srp.setRespHead(resp);
		String json = srp.parseJsonStatus("message", "success", "test cross origin");
		resp.getWriter().write(json);		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		/*String botId = req.getParameter("botId");
		String reqDonmain = req.getServerName();
		String text = req.getParameter("text");*/
		
		
		/*String uri = req.getScheme() + "://" +   // "http" + "://
				req.getServerName() +       // "myhost"
	             ":" + req.getServerPort() + // ":" + "8080"
	             req.getRequestURI() +       // "/people"
	            (req.getQueryString() != null ? "?" +
	            	req.getQueryString() : ""); // "?" + "lastname=Fox&age=30"*/
		
		
		String json = srp.parseJsonStatus("message", "success", "test cross origin");
		resp.getWriter().write(json);
		
	}
}
