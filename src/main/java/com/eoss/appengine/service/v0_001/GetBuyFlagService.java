package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class GetBuyFlagService extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userBotId = req.getParameter("botId");
		UserBots userbot = new UserBots();
		UserBotsDAO userBotDao = new UserBotsDAO();
		userbot.setUserBotId(userBotId);
		Entity ent = userBotDao.getByUserBotId(userbot.getUserBotId());

		String json = ent.getProperty("BuyFlag").toString();
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
}
