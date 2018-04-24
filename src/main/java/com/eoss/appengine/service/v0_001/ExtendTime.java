package com.eoss.appengine.service.v0_001;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.eoss.appengine.dao.DatastoreDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

public class ExtendTime extends DatastoreDAO{
	SetRespPram srp = new SetRespPram();
	SetReqParam srq = new SetReqParam();
	public String extend(String botId,Integer date) {

		String botid = botId;
		UserBotsDAO userBotDao = new UserBotsDAO();
		String status = "fail";
		Entity ent = userBotDao.getByUserBotId(botid);
		if(ent != null) {
			status = compareDate(ent,date);
		}
		
		return status;
	}
	
	private String compareDate(Entity ent,Integer date) {
		Calendar calendar = Calendar.getInstance();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		DateFormat parserSDF= new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy",Locale.ENGLISH);
		parserSDF.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		Date datastoreDate = null;
		Date currentdate = new Date();
		
		try {
			datastoreDate = parserSDF.parse(ent.getProperty("expireDate").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(datastoreDate.before(new Date())) {
        	calendar.setTime(currentdate);
        }else {
        	calendar.setTime(datastoreDate);
        }
        

        calendar.add(Calendar.DATE, date);
   
        
		currentdate = calendar.getTime();
	
		ent.setProperty("expireDate", currentdate.toString());
		ent.setProperty("status", "enable");	
		ent.setProperty("paid", true);
		ent.setProperty("BuyFlag", true);
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			return e.getMessage();
		}
		
		
		
		return "success";
	}
	
}
