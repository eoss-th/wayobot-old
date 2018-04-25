package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.ChatLog;
import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class ChatLogService extends HttpServlet{
	private ChatlogsDAO chatlogDao = new ChatlogsDAO();
	private SetRespPram srp = new SetRespPram();
	private SetReqParam srq = new SetReqParam();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ChatLog chatlog = new ChatLog();
		String sessionId = req.getParameter("sessionId").trim();
		String botId = req.getParameter("botId").trim();
		chatlog.setChanelName(sessionId);
		List<Entity> list = chatlogDao.getChatLog(chatlog.getChanelName(),botId);
		String json = srp.parseJsonEntityList(list);
		resp = srp.setRespHead(resp,"*");
		resp.getWriter().print(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ChatLog chatlog = new ChatLog();
		String message = req.getParameter("message");
		String sessionId = req.getParameter("sessionId");
		String botId = req.getParameter("botId");
		
		Text text = new Text("");
		chatlog.setChanelName(sessionId);
		chatlog.setMessage(text);
		chatlog.setReplyMessage(new Text(message));
		chatlog.setReadFlag(true);
		chatlog.setTimeStamp(srq.getCurrentDate());
		chatlog.setBotId(botId);
		chatlog.setContextName("");
		chatlogDao.addChatLog(chatlog);
	}
}
