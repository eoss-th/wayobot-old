package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eoss.appengine.helper.SetRespPram;

@SuppressWarnings("serial")
public class SignOut extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
	
			HttpSession httpSession = req.getSession();
			httpSession.removeAttribute("token");
			httpSession.removeAttribute("accountId");
			httpSession.removeAttribute("userName");
			httpSession.removeAttribute("role");

			httpSession.invalidate();
			
			
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write("success");
		}catch (Exception e) {
			resp = srp.setRespHead(resp,System.getenv("domain"));
			resp.getWriter().write("fail");
		}
	}
}
