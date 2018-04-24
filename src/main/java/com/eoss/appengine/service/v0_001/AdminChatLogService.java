package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class AdminChatLogService extends HttpServlet{
	ChatlogsDAO chatlogDao = new ChatlogsDAO();
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String botId = req.getParameter("botId");
		Boolean readFlag = Boolean.parseBoolean(req.getParameter("readFlag"));
		
		List<Entity> le = chatlogDao.getChatLogByBotId(botId, readFlag);
		String Json = srp.parseJsonEntityList(le);
		resp = srp.setRespHead(resp);
		resp.getWriter().write(Json);
	}
}
