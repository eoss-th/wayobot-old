package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.brain.net.Context;
import com.eoss.servlet.SessionManager;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class UserBotService extends HttpServlet{
	SetReqParam srq = new SetReqParam();
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String json = srp.parseJsonStatus("userBotService", "fail", "session expire");
		if(req.getSession().getAttribute("token") != null) {
			String username = (String) req.getSession().getAttribute("token");
			String[] parts = username.split("_");
			username = parts[1];
			
			SubAccountDAO subaccountDao = new SubAccountDAO();
			SubAccount subaccount = new SubAccount();
			subaccount.setUserName(username);
			Entity ent = subaccountDao.getSubAccount(subaccount.getUserName());
			List<Entity> le = null;
			if(ent != null) {
				UserBots userbot = new UserBots();
				UserBotsDAO userBotDao = new UserBotsDAO();
				userbot.setAccountId(ent.getProperty("accountId").toString());
				le = userBotDao.getUserBotsList(userbot);
			}

			
			json = srp.parseJsonEntityList(le);			
		}

		
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ChatlogsDAO chatlogDao = new ChatlogsDAO();
		UserBots userbot = new UserBots();
		UserBotsDAO userBotDao = new UserBotsDAO();
		String json = "fail";
		String botName = req.getParameter("botId");
		String accountId = req.getParameter("accountId");
		String cssId = "default";
		String status = "enable";
		String notiNumber = chatlogDao.getChanel();
		String country = req.getParameter("country");
		String greeting = req.getParameter("greeting");
		Date d = srq.getCurrentDate();
		String unknown = req.getParameter("unknown");
		String title = req.getParameter("title");
		
		if(checkParameter(botName,greeting,unknown,title)) {
			userbot.setAccountId(accountId);
			userbot.setBotId(botName);
			userbot.setCssId(cssId);
			userbot.setStatus(status);
			userbot.setNotiNumber(notiNumber);
			userbot.setCountry(country);
			userbot.setBuyDate(d);
			userbot.setExpireDate(srq.getExpireDate(d, 1));
			userbot.setMessageCount(0);
			userbot.setToDayMessage(0);
			userbot.setRemainMessage(10000);
			userbot.setUserBotId(notiNumber);		
			
			json = userBotDao.addUserBots(userbot);
			
			String contextName = accountId+"/"+notiNumber;
			
			Context context = SessionManager.instance().context(contextName);
			context.properties.put("language", country.toLowerCase());
			context.properties.put("greeting", greeting);
			context.properties.put("unknown", unknown);
			context.properties.put("title", title);
			context.save();			
		}

		
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("token");
		String[] parts = username.split("_");
		username = parts[1];
		
		SubAccountDAO subaccountDao = new SubAccountDAO();
		SubAccount subaccount = new SubAccount();
		subaccount.setUserName(username);
		Entity ent = subaccountDao.getSubAccount(subaccount.getUserName());
		if(ent != null) {
			UserBots userbot = new UserBots();
			UserBotsDAO userBotDao = new UserBotsDAO();
			userbot.setUserBotId(req.getParameter("userBotId"));
			ent = userBotDao.getByUserBotId(userbot.getUserBotId());
		}
		String json = null;
		if(ent != null) {
			System.out.println(req.getParameter("unknown"));
			ent.setProperty("greeting", req.getParameter("greeting"));
			ent.setProperty("unknown", req.getParameter("unknown"));
			json = srq.datasotorePut(ent);
		}
		
		
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	private Boolean checkParameter(String botName,String greet,String unknown,String title) {

		if (botName == null || botName.isEmpty()) {		
			return false;	
		}
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
