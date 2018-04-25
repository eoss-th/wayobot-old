package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.BotTemplate;
import com.eoss.appengine.dao.BotTemplateDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class BotService extends HttpServlet{
	SetReqParam srq = new SetReqParam();
	SetRespPram srp = new SetRespPram();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BotTemplate botTemplate = new BotTemplate();
		BotTemplateDAO botTemplateDAO = new BotTemplateDAO();
		List<Entity> le = botTemplateDAO.getBotTemplateList(botTemplate);
		
		String json = srp.parseJsonEntityList(le);
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BotTemplate botTemplate = new BotTemplate();
		BotTemplateDAO botTemplateDAO = new BotTemplateDAO();
		botTemplate = srq.setBotTemplate(req);
		String status = botTemplateDAO.addBotTemplate(botTemplate);
		String json = srp.parseJsonStatus("addBot", status, "");
		
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
		
	}

}
