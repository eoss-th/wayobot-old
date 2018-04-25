package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.dao.ShowCaseDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ShowcaseListChatRoom extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("token")!= null) {
			String accountId = (String) req.getSession().getAttribute("accountId");
			Boolean publish = true;
			ShowCaseDAO shocaseDao = new ShowCaseDAO();
			List<Entity> le = shocaseDao.getShowcaseList(accountId, publish);
			String json = srp.parseJsonEntityList(le);
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write(json);			
		}
	}
}
