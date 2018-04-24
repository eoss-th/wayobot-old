package com.eoss.appengine.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.eoss.appengine.bean.Account;
import com.eoss.appengine.bean.BotTemplate;
import com.eoss.appengine.bean.ChatLog;
import com.eoss.appengine.bean.Pusher_count;
import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.DatastoreDAO;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class SetReqParam extends DatastoreDAO{
	
	public String getCurrentDateWithPaltform() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		Date d = new Date();
		return dateFormat.format(d);
		
	}	
	
	public Date getCurrentDate() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		Date d = new Date();
		return d;
		
	}
	public Date getExpireDate(Date d, int i) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		Calendar c = Calendar.getInstance(); 

		c.setTime(d); 
		c.add(Calendar.DATE, i);
		d = c.getTime();
		return d;
		
	}	
	public Account setAccount(HttpServletRequest req) {
		Account a = new Account();
		a.setAccountId(req.getParameter("accountId"));
		a.setAccName(req.getParameter("subAccName"));
		a.setEmail(req.getParameter("email"));
		a.setAccRegisDate(getCurrentDate());
		a.setEditDate(getCurrentDate());
		return a;
	}
	public SubAccount setSubAccount(HttpServletRequest req) {
		SubAccount sa = new SubAccount();
		sa.setAccountId(req.getParameter("accountId"));
		sa.setSubAccName(req.getParameter("subAccName"));
		sa.setPassWord(req.getParameter("passWord").trim());
		sa.setUserName(req.getParameter("email"));
		sa.setRole(req.getParameter("role"));
		sa.setSubRegisDate(getCurrentDate());
		sa.setEditDate(getCurrentDate());
		sa.setTel(req.getParameter("tel"));
		sa.setEmail(req.getParameter("email"));
		return sa;
	}
	
	public ChatLog setChatLog(HttpServletRequest req) {
		ChatLog cl = new ChatLog();
		cl.setAccountId(req.getParameter("accountId"));	
		cl.setBotId(req.getParameter("botId"));
		cl.setChanelName(req.getParameter("chanelName"));
		cl.setMessage(new Text(req.getParameter("message")));
		cl.setReplyMessage(new Text(req.getParameter("replyMessage")));
		cl.setTimeStamp(getCurrentDate());
		cl.setReadFlag(false);
		return cl;
	}
	
	public UserBots setUserBot(HttpServletRequest req) {
		UserBots ub = new UserBots();
		ub.setAccountId(req.getParameter("accountId"));
		ub.setBuyDate(getCurrentDate());
		ub.setExpireDate(getExpireDate(getCurrentDate(),30));
		ub.setBotId(req.getParameter("botId"));
		ub.setCssId(req.getParameter("cssId"));			
		ub.setStatus(req.getParameter("status"));

		return ub;
	}
	
	public BotTemplate setBotTemplate(HttpServletRequest req) {
		BotTemplate bt = new BotTemplate();
		bt.setBotId(req.getParameter("botId"));
		bt.setBotName(req.getParameter("botId"));
		bt.setInfo(req.getParameter("info"));
		bt.setPrice(Double.parseDouble(req.getParameter("price")));
		bt.setAge(Integer.parseInt(req.getParameter("age")));
		
		return bt;
	}
	
	public Pusher_count setPusher_count(HttpServletRequest req) {
		Pusher_count pc = new Pusher_count();
		pc.setAccountId(req.getParameter("accountId"));
		pc.setTimeStamp(getCurrentDateWithPaltform());
		
		return pc;
	}
	
	public String datasotorePut(Entity ent) {
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			return e.getMessage();
		}
		return "success";
	}
	public String datastoreDelete(String entityName,String entittyId) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(entityName, entittyId));
		}catch (Exception e) {
			return e.getMessage();
		}		
		return "success";
	}
}

