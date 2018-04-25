package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.Pusher_count;
import com.eoss.appengine.dao.Pusher_countDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class PusherCountService extends HttpServlet{
	SetReqParam srq = new SetReqParam();
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String json = srp.parseJsonStatus("pusherCount", "fail", "session expire");
		if(req.getSession().getAttribute("accountId") != null){
			Pusher_countDAO p_countDao = new Pusher_countDAO();
			Pusher_count p_count = new Pusher_count();
			p_count.setAccountId(req.getSession().getAttribute("accountId").toString());
			p_count.setTimeStamp(srq.getCurrentDateWithPaltform());
			
			Entity ent = p_countDao.getPusherCount(p_count);
			
			if(ent == null) {
				p_count.setCount(0);
				if(p_countDao.addPusherCount(p_count)=="success") {
					json = srp.parseJsonObject(p_count);
				}
			}else {
				p_count.setAccountId(ent.getProperty("accountId").toString().trim());
				p_count.setCount(Integer.parseInt(ent.getProperty("count").toString().trim()));
				p_count.setMaxCount(Integer.parseInt(ent.getProperty("maxCount").toString().trim()));
				p_count.setTimeStamp(ent.getProperty("timeStamp").toString().trim());
				json = srp.parseJsonObject(p_count);
			}
		}
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
}
