package com.eoss.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.ChatRoom;
import com.eoss.appengine.dao.ChatRoomDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.brain.MessageObject;
import com.eoss.brain.Session;
import com.eoss.brain.net.Node;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class BoardcastServlet extends HttpServlet {

	static class Entry {
		String owner;
		String response;
		Node node;

		Entry(String owner, String response, Node node) {
			this.owner = owner;
			this.response = response;
			this.node = node;
		}
	}

	SetRespPram srp = new SetRespPram();
	ChatRoomDAO chatRoomDao = new ChatRoomDAO();
	ChatRoom chatRoom = new ChatRoom();
	int i = 0;
	Map<String, Chat> lastMessageMap = new HashMap<>();
	Map<String, List<Chat>> messagesMap = new HashMap<>();
	
	int limit = 5;

	Session getSession(String roomId, String contextName) {

		SessionManager sessionManager = SessionManager.instance();
		Session session = sessionManager.get(roomId + "_" + contextName, contextName);
		session.learning = false;

		return session;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String message = req.getParameter("message");
		String roomId = req.getParameter("roomId");
		Chat lastChat = lastMessageMap.get(roomId);
		if (message != null) {
			if (req.getSession().getAttribute("token") != null) {
				lastChat = new Chat((String) req.getSession().getAttribute("email"), message);
			} else {
				lastChat = new Chat("Guest", message);
			}
		}

		List<Chat> messages = messagesMap.get(roomId);
		if (messages == null) {
			messages = new ArrayList<Chat>();
			messagesMap.put(roomId, messages);
		}
		
		while (messages.size()>limit) {
			messages.remove(0);				
		}

		if (lastChat != null) {
			lastMessageMap.put(roomId, lastChat);
			List<String> contextList = new ArrayList<String>();
			Entity ent = chatRoomDao.getChatRoom(roomId);

			if (ent != null) {
				if (ent.getProperty("botPath") != null) {
					String botPathList = ent.getProperty("botPath").toString();
					contextList = new ArrayList<String>(Arrays
							.asList(botPathList.replace("[", "").replace("]", "").replaceAll(" ", "").split(",")));
				}

				if (contextList.size() > 0) {
					Map<Float, Entry> responseMap = new TreeMap<>();

					Session session;
					String response;
					float confidence;
					for (String contextName : contextList) {
						session = getSession(roomId, contextName);
						response = session.parse(MessageObject.build(lastChat.message));
						if (!response.isEmpty()) {
							confidence = session.lastEntry().node.active();
							responseMap.put(confidence,
									new Entry(session.context.name, response, session.lastEntry().node));
						}
					}

					float maxConfidence = Float.MIN_VALUE;
					Entry maxEntry = null;
					for (Map.Entry<Float, Entry> entry : responseMap.entrySet()) {
						System.out.println(entry.getKey() + ":" + entry.getValue());
						if (entry.getKey() > maxConfidence) {
							maxConfidence = entry.getKey();
							maxEntry = entry.getValue();
						}
					}

					String maxOwner = null;
					String maxResponse = null;
					if (maxEntry != null) {
						maxOwner = maxEntry.owner;
						maxResponse = maxEntry.response;
						maxEntry.node.release();
					}

					if (lastChat.message == message) {
						messages.add(lastChat);
						lastMessageMap.put(roomId, lastChat);
					}
					if (maxResponse != null) {
						Chat newChat = new Chat(maxOwner, maxResponse);
						messages.add(newChat);
						lastMessageMap.put(roomId, newChat);
					}

				} else {
					if (lastChat.message == message) {
						messages.add(lastChat);
						lastMessageMap.put(roomId, lastChat);
					}
				}
			}

		}

		String json = srp.parseJsonListChat(messages);
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
}
