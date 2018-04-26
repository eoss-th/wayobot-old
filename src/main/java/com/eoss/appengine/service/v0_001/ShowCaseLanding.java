package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.eoss.appengine.dao.ShowCaseDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.brain.net.Context;
import com.eoss.servlet.SessionManager;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ShowCaseLanding extends HttpServlet{
	private SetRespPram srp = new SetRespPram();
	private SetReqParam srq = new SetReqParam();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ShowCaseDAO showcaseDao = new ShowCaseDAO();
		
		String[] data = req.getRequestURI().split("/");
		String botId = data[4];
		String accountId = data[3];
		String contextName = accountId+"/"+botId;
		if(validateParam(accountId,botId)) {
			Entity ent = showcaseDao.getShowCase(botId);
			if(ent != null) {
				Boolean publish = Boolean.parseBoolean(ent.getProperty("publish").toString());
				if(publish) {
					Integer viewCount = Integer.parseInt(ent.getProperty("viewCount").toString()) +1;
					Context context = SessionManager.instance().context(contextName);
					try {
						context.load();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					ent.setProperty("viewCount", viewCount);
					srq.datasotorePut(ent);
					
					JSONObject object = new JSONObject(context.properties);
					req.setAttribute("bot_title", object.getString("title"));	
					req.setAttribute("bot_greeting", object.getString("greeting"));
					req.setAttribute("bot_botId", botId);
					req.setAttribute("bot_accountId", accountId);
					req.setAttribute("bot_viewCount", viewCount.toString());
					req.setAttribute("showcase_title", ent.getProperty("title").toString());
					req.setAttribute("showcase_description", ent.getProperty("description").toString());
					req.setAttribute("showcase_owner", ent.getProperty("owner").toString());
					resp = srp.setRespHead(resp,System.getenv("domain"));
					getServletContext().getRequestDispatcher("/showcase.jsp").forward(req, resp);						
				}else {
					resp.sendRedirect("/index.jsp");
				}
			}else {
				resp.sendRedirect("/index.jsp");
			}		
		}else {
			resp.sendRedirect("/index.jsp");
		}
	}
	
	private Boolean validateParam(String accountId,String botId) {
		if(accountId == null || accountId.isEmpty()) {
			return false;
		}
		if(botId == null || botId.isEmpty()) {
			return false;
		}		
		return true;
	}
}
