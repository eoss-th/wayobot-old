package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.Orders;
import com.eoss.appengine.dao.OrdersDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class OrderHistorySevice extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		OrdersDAO orderDao = new OrdersDAO();
		Orders orders = new Orders();
		String accountId = (String) req.getSession().getAttribute("accountId");
		String botId = req.getParameter("botId");
		orders.setBotId(botId);
		orders.setAccountId(accountId);

		List<Entity> orderList = orderDao.getOrdersList(orders);
		String json = srp.parseJsonEntityList(orderList);
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
}
