package com.eoss.appengine.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.eoss.servlet.Chat;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

public class SetRespPram{
	
	public String parseJsonEntityList(List<Entity> list) {
		String json = new Gson().toJson(list);
		return json;
	}
	public String parseJsonEntity(Entity e) {
		String json = new Gson().toJson(e);
		return json;
	}
	public String parseJsonObject(Object o) {
		String json = new Gson().toJson(o);
		return json;
	}
	public String parseJsonStatus(String dao,String status,String message) {
		Map<String, String> data = new HashMap<String, String>();
		data.put( "id", dao );
		data.put( "status", status );
		data.put( "message", message );
		String json = new Gson().toJson(data);
		return json;
	}
	public String parseJsonListString(List<String> le) {
		String json = new Gson().toJson(le);
		return json;
	}
	
	public String parseJsonListChat(List<Chat> listChat) {
		String json = new Gson().toJson(listChat);
		return json;		
	}
	public HttpServletResponse setRespHead(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		return resp;
	}
}
