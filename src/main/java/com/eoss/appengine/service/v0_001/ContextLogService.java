package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.dao.ContextLogDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ContextLogService extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String botId = req.getParameter("botId");
		String accountId = req.getParameter("accountId");
		String chanel = req.getParameter("chanel");
		String type = req.getParameter("type");
		resp = srp.setRespHead(resp,System.getenv("domain"));
		String json = "false";
		ContextLogDAO contextDao = new ContextLogDAO();
		Entity ent = contextDao.getContextLog(accountId, botId, chanel,type);
		if( ent != null) {
			json = srp.parseJsonEntity(ent);
		}
		resp.getWriter().write(json);
		
	}
}
