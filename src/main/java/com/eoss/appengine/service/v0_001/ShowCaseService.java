package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.ChatRoom;
import com.eoss.appengine.bean.ShowCase;
import com.eoss.appengine.bean.ShowcaseBotRoom;
import com.eoss.appengine.dao.ChatRoomDAO;
import com.eoss.appengine.dao.ShowCaseDAO;
import com.eoss.appengine.dao.ShowcaseBotRoomDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class ShowCaseService extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	private SetReqParam srq = new SetReqParam();
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
					
					if(!publish) {
						// remove showcase from all chat room
						ShowcaseBotRoomDAO showcaseBotRoomDao = new ShowcaseBotRoomDAO();
						Entity showcaseBotRoomEnt = showcaseBotRoomDao.getShowcaseBotRoom(botId);
						List<String> roomListArray = new ArrayList<String>();
						if(showcaseBotRoomEnt != null) {
							if(showcaseBotRoomEnt.getProperty("roomList") != null) {
								String  roomList = showcaseBotRoomEnt.getProperty("roomList").toString();
								roomListArray = new ArrayList<String>(Arrays.asList(roomList.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
							}
							ChatRoomDAO chatRoomDao = new ChatRoomDAO();
							List<String> contextList = new ArrayList<String>();
							for(String room:roomListArray) {
								Entity chatRoomEnt = chatRoomDao.getChatRoom(room);
								if(chatRoomEnt != null) {
									if (chatRoomEnt.getProperty("botPath") != null) {
										String botPathList = chatRoomEnt.getProperty("botPath").toString();
										contextList = new ArrayList<String>(Arrays
												.asList(botPathList.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
									}
									if (contextList.size() > 0 && contextList.contains(accountId+"/"+botId)) {
										contextList.remove(accountId+"/"+botId);
										chatRoomEnt.setIndexedProperty("botPath", contextList);
										srq.datasotorePut(chatRoomEnt);
									}
								}
							}
							
							showcaseBotRoomDao.delShowcaseBotRoom(botId);
						}
					}
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
