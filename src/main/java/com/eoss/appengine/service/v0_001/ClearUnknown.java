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
public class ClearUnknown extends HttpServlet{
	ChatlogsDAO chatlogDao = new ChatlogsDAO();
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String context = req.getParameter("context");

		List<Entity> le = chatlogDao.getChatLogByContextName(context, false);
		String json = srp.parseJsonEntityList(le);
		System.out.println(json);
		resp.getWriter().write(json);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
