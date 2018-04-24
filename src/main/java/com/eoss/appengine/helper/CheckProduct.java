package com.eoss.appengine.helper;
import java.util.Map;

import com.eoss.appengine.bean.BotTemplate;
import com.eoss.appengine.dao.BotTemplateDAO;
import com.google.appengine.api.datastore.Entity;

public class CheckProduct {
	public boolean checkProduct(Map<String, String> data) {
		BotTemplate botTemplate = new BotTemplate();
		BotTemplateDAO botTemplateDAO = new BotTemplateDAO();
		String botId = data.get("item_name");
		Double price = Double.parseDouble(data.get("amount1"));
		String days = data.get("item_number");
		
		botTemplate.setBotId(botId);
		
		Entity ent = botTemplateDAO.getBotTemplate(botTemplate.getBotId());
		if(ent != null) {
			if(price != Double.parseDouble(ent.getProperty("price").toString())) {
				return false;
			}
			if(!days.equals(ent.getProperty("age").toString())) {
				return false;
			}
			return true;			
		}
		return false;
	}
}
