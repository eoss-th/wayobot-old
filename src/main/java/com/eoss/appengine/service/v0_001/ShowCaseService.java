package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.ShowCase;
import com.eoss.appengine.dao.ShowCaseDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class ShowCaseService extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	private ShowCase showcase = new ShowCase();
	private ShowCaseDAO showcaseDao = new ShowCaseDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String botId = req.getParameter("botId");
		Entity ent = showcaseDao.getShowCase(botId);
		
		Boolean hasImage = false;
		String json = srp.parseJsonStatus("getShowCase", hasImage.toString(), "");
		if(ent != null) {
			hasImage = Boolean.parseBoolean(ent.getProperty("hasImage").toString());	
			json = srp.parseJsonStatus("getShowCase", hasImage.toString(), srp.parseJsonEntity(ent));
		}

		
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		
		String accountId = (String) req.getSession().getAttribute("accountId");
		String botId = req.getParameter("botId");
		Boolean publish = Boolean.parseBoolean(req.getParameter("publish"));
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String json = srp.parseJsonStatus("addSwowCase", "fail", "please upload cover image");
		Date date = new Date();
		
		Entity ent = showcaseDao.getShowCase(botId);
		if(ent != null) {
			if(Boolean.parseBoolean(ent.getProperty("hasImage").toString())) {
				Integer viewCount = Integer.parseInt(ent.getProperty("viewCount").toString());
				if(publish) {
					if(validateParam(title,description)) {			
						showcase.setAccountId(accountId);
						showcase.setBotId(botId);
						showcase.setTimeStamp(date);
						showcase.setPublish(publish);
						showcase.setHasImage(true);
						showcase.setTitle(title);
						showcase.setDescription(description);
						showcase.setOwner((String) req.getSession().getAttribute("userName"));	
						showcase.setViewCount(viewCount);
						json = srp.parseJsonStatus("addSwowCase", showcaseDao.addShowCase(showcase), "");
					}else {
						json = srp.parseJsonStatus("addSwowCase", "fail", "Please input Title and Description");
					}					
				}else {
					showcase.setAccountId(accountId);
					showcase.setBotId(botId);
					showcase.setTimeStamp(date);
					showcase.setPublish(publish);
					showcase.setHasImage(true);
					showcase.setTitle(title);
					showcase.setDescription(description);
					showcase.setOwner((String) req.getSession().getAttribute("userName"));	
					showcase.setViewCount(viewCount);
					json = srp.parseJsonStatus("addSwowCase", showcaseDao.addShowCase(showcase), "");					
				}
			}
		}
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	private Boolean validateParam(String title, String description) {
		if(title == null || title.isEmpty()) {
			return false;
		}
		if(description == null || description.isEmpty()) {
			return false;
		}
		return true;
	}
}
