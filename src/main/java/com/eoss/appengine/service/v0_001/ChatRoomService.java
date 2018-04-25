package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eoss.appengine.bean.ChatRoom;
import com.eoss.appengine.dao.ChatRoomDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;


@SuppressWarnings("serial")
public class ChatRoomService extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	ChatRoomDAO chatRoomDao = new ChatRoomDAO();
	ChatRoom chatRoom = new ChatRoom();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String roomId = req.getParameter("roomId");
		List<String> botPathArray = new ArrayList<String>();
		
		Entity ent = chatRoomDao.getChatRoom(roomId);
		if(ent != null) {
			if(ent.getProperty("botPath") != null) {
				String array = ent.getProperty("botPath").toString();
				botPathArray = new ArrayList<String>(Arrays.asList(array.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
				array = new Gson().toJson(botPathArray);		
				ent.setProperty("botPath", array);	
			}	
		}

		
		String json = srp.parseJsonEntity(ent);
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("token") != null) {
			String roomId = req.getParameter("roomId");
			String botPath = req.getSession().getAttribute("accountId")+"/"+req.getParameter("botPath");
			List<String> botPathArray = new ArrayList<String>();
			chatRoom.setRoomId(roomId);
			String json = "";
			Entity ent = chatRoomDao.getChatRoom(chatRoom.getRoomId());
			if(ent != null) {
				if(ent.getProperty("botPath") != null) {
					String  botPathList = ent.getProperty("botPath").toString();
					botPathArray = new ArrayList<String>(Arrays.asList(botPathList.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
				}
				
				if(botPathArray.size() < 10) {
					if(!botPathArray.contains(botPath)) {
						botPathArray.add(botPath);
						chatRoom.setBotPath(botPathArray);
						chatRoomDao.addRoom(chatRoom);	
						json = srp.parseJsonStatus("addChatRoomBot", chatRoomDao.addRoom(chatRoom), "");
					}else {
						json = srp.parseJsonStatus("addChatRoomBot", "fail", "This bot Is Already Add");
					}
				}else {
					json = srp.parseJsonStatus("addChatRoomBot", "fail", "err01");
				}
			}else{
				botPathArray.add(botPath);
				chatRoom.setBotPath(botPathArray);				
				json = srp.parseJsonStatus("addNewChatRoomBot", chatRoomDao.addRoom(chatRoom),"");
			}
			
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(json);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("token") != null) {
			String json = srp.parseJsonStatus("removeChatRoomBot", "fail", "not found this bot in room");
			String roomId = req.getParameter("roomId");
			String botPath = req.getSession().getAttribute("accountId")+"/"+req.getParameter("botPath");
			List<String> botPathArray = new ArrayList<String>();
			chatRoom.setRoomId(roomId);
			Entity ent = chatRoomDao.getChatRoom(chatRoom.getRoomId());
			if(ent != null) {
				String botPathList = ent.getProperty("botPath").toString();
				botPathArray = new ArrayList<String>(Arrays.asList(botPathList.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
				if(botPathArray.contains(botPath)) {
					if(botPathArray.size() > 1)
						botPathArray.remove(botPath);
					else
						botPathArray.clear();
					chatRoom.setBotPath(botPathArray);
					chatRoomDao.addRoom(chatRoom);	
					json = srp.parseJsonStatus("removeChatRoomBot", chatRoomDao.addRoom(chatRoom), "");
				}
			}
			
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(json);
		}
	}
	
	
}
