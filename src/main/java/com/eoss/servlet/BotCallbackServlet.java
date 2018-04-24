package com.eoss.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.eoss.appengine.bean.ChatLog;
import com.eoss.appengine.bean.Message_count;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.dao.Message_countDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.brain.MessageObject;
import com.eoss.brain.NodeEvent;
import com.eoss.brain.Session;
import com.eoss.brain.net.ContextListener;
import com.eoss.brain.net.Hook;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;


@SuppressWarnings("serial")
public class BotCallbackServlet extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	private SetReqParam srq = new SetReqParam();

	ChatlogsDAO chatlogDao = new ChatlogsDAO();
	UserBotsDAO userBotDao = new UserBotsDAO();
	
	Session getSession(final HttpServletRequest req) {
		
		String[] data = req.getRequestURI().split("/");
		String botId = data[3];
		String accountId = data[2];
		
		String sessionId = botId + req.getParameter("sessionId");
		String contextName = accountId + "/" + botId;
	
		SessionManager sessionManager = SessionManager.instance();
		Session session = sessionManager.get(sessionId, contextName);
		
		final String unknown = session.context.properties.get("unknown");
		
		session.context.callback(new ContextListener() {

			@Override
			public void callback(NodeEvent nodeEvent) {
				if (nodeEvent.event==NodeEvent.Event.HesitateConfidence) {
					req.setAttribute("hesitate", Hook.toString(nodeEvent.node.hookList()));
				} else if (nodeEvent.event==NodeEvent.Event.LowConfidence) {
					req.setAttribute("unknown", unknown);
				} else if (nodeEvent.event==NodeEvent.Event.ContextSaved) {
					chatlogDao.clear(nodeEvent.messageObject.toString(), false);
				}
			}
			
		});
			
		String role = (String) req.getSession().getAttribute("role");
		String sessionAccountId = (String) req.getSession().getAttribute("accountId");
		if(role != null && role.equals("administrator") && accountId.equals(sessionAccountId)) {
			String token = (String) req.getSession().getAttribute("token");			
			session.context.admin(Arrays.asList(token));					
			session.learning = true;	
		}else{
			session.learning = false;
		}
		
		return session;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String chanel = chatlogDao.getChanel();
		resp = srp.setRespHead(resp);
		resp.getWriter().print(chanel);
	}	
	
	@Override
	protected void doPost(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
			throws ServletException, IOException {
		ChatLog chatlog = new ChatLog();
		Message_count m_count = new Message_count();
		Message_countDAO m_countDAO = new Message_countDAO();
		HttpSession httpSession = req.getSession();
			
		Session session = getSession(req);	
		
		String[] data = req.getRequestURI().split("/");
		String botId = data[3];
		String accountId = data[2];		
		String contextName = accountId + "/" + botId;
		
		String message = req.getParameter("message");
		String sessionId = req.getParameter("sessionId");
		String role = (String) req.getSession().getAttribute("role");
		//String accountId = req.getParameter("accountId");
		//String botId = req.getParameter("botId");
	
		SessionManager sessionManager = SessionManager.instance(); 
		String type = req.getParameter("header");
		
		boolean isAdminCmd = false;
		if(role != null && role.equals("administrator")) {
			if(req.getParameter("cmd") != null) {
				if(req.getParameter("cmd").equals("import")) {
					httpSession.setAttribute("importMessage", message);
					message = getHeaderType(type)+"\n"+message;
					isAdminCmd = true;
					sessionManager.clearContext(contextName);
				}  
				if(req.getParameter("cmd").equals("clear")) {
					httpSession.setAttribute("importMessage", "");
					message = "ล้างข้อมูล";
					isAdminCmd = true;
					sessionManager.clearContext(contextName);
				} 
				if(req.getParameter("cmd").equals("get")) {
					System.out.println(type);
					if(type.equals("type1")) {
						message = "ดูข้อมูลถามตอบ";
					}
					if(type.equals("type2")) {
						message = "ดูข้อมูลดิบ";
					}
					isAdminCmd = true;
				}				
			}
		}
		
		MessageObject messageObject = MessageObject.build(message);
		if(httpSession.getAttribute("token") != null) {
			System.out.println("token not null");
			messageObject.attributes.put("userId", httpSession.getAttribute("token").toString());
		}

		String reply = session.parse(messageObject);
		session.releaseLastActive();
		
		Boolean readFlag = true;
		String replyLog = reply;
		String hesitate = (String) req.getAttribute("hesitate");
		if (hesitate!=null) {
			reply = hesitate + "?" + System.lineSeparator() + System.lineSeparator() + reply;
			readFlag = false;
		}
		
		String unknown = (String) req.getAttribute("unknown");
		if (unknown!=null) {
			reply = unknown;
			readFlag = false;
			replyLog = "";
		}
					
		if(compareDate(botId) == true) {
			 if (!isAdminCmd) {
				chatlog.setChanelName(sessionId);
				chatlog.setMessage(new Text(message));
				chatlog.setReplyMessage(new Text(reply));
				chatlog.setReplyLog(new Text(replyLog));
				chatlog.setReadFlag(readFlag);
				chatlog.setTimeStamp(srq.getCurrentDate());
				chatlog.setBotId(botId);
				chatlog.setContextName(contextName);
				chatlogDao.addChatLog(chatlog);
			 }	
				m_count.setAccountId(accountId);
				m_count.setBotId(botId);
				m_count.setTimeStamp(srq.getCurrentDateWithPaltform());
				
				Entity ent = m_countDAO.getMessageCount(m_count);
				if(ent != null) {
					Integer i = Integer.parseInt(ent.getProperty("count").toString());
					i = i+1;
					ent.setProperty("count", i);
					srq.datasotorePut(ent);
				}else {
					m_count.setCount(1);
					m_countDAO.addMessageCount(m_count);
				}			
			}else {
				reply = "This bot is expire";
				Entity ent = userBotDao.getByUserBotId(botId);
				if(ent != null) {
					ent.setProperty("status","disable");
					srq.datasotorePut(ent);
				}
			}			

		resp = srp.setRespHead(resp);
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");	
		resp.getWriter().print(reply);
	}
	
	private Boolean compareDate(String botid) {
		UserBots userBot = new UserBots();
		UserBotsDAO userBotDao = new UserBotsDAO();

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
		DateFormat parserSDF= new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy",Locale.ENGLISH);
		parserSDF.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		
		Date datastoreDate = null;
		userBot.setUserBotId(botid);
		Entity ent = userBotDao.getByUserBotId(userBot.getUserBotId());

		try {
			datastoreDate = parserSDF.parse(ent.getProperty("expireDate").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if(datastoreDate.after(new Date())) {
        	return true;
        }

		return false;
	}

	private String getHeaderType(String header) {
		String headerName = null;
		if(header.equals("type1")) {
			headerName = "ใส่ข้อมูลถามตอบ";
		}
		if(header.equals("type2")) {
			headerName = "ใส่ข้อมูลดิบ";
		}	
		if(header.equals("type3")) {
			headerName = "ล้างข้อมูล";
		}	
		return headerName;
	}
}