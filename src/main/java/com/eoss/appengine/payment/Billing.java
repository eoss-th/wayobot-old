package com.eoss.appengine.payment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class Billing extends HttpServlet{
	SetReqParam srq = new SetReqParam();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String botId = req.getParameter("botId");

		UserBotsDAO userBotDao = new UserBotsDAO();

		Entity ent = userBotDao.getByUserBotId(botId);

		ent.setProperty("BuyFlag", false);
		
		srq.datasotorePut(ent);
		
		resp.sendRedirect("/dashboard/dashboard.jsp");
	}
}
