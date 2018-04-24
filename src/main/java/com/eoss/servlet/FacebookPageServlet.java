package com.eoss.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FacebookPageServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String name = req.getParameter("name");
		String message = req.getParameter("message");
		
		try {
			FacebookPage fbPage = new FacebookPage(new FacebookDSConfig(name));
			resp.getWriter().println(new String(fbPage.publish(message).getContent()));
		} catch (Exception e) {
			e.printStackTrace();
			resp.getWriter().println(e);
		}
	}

}