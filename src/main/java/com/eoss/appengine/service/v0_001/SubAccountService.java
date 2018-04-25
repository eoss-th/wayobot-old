package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;

@SuppressWarnings("serial")
public class SubAccountService extends HttpServlet{
	private SubAccount sa = new SubAccount();
	private SubAccountDAO saDao = new SubAccountDAO();
	private SetReqParam srq = new SetReqParam();
	private SetRespPram srp = new SetRespPram();
	private String json;
	private String status;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sa = srq.setSubAccount(req);
		json = srp.parseJsonEntityList(saDao.getSubAccountList(sa));
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sa = srq.setSubAccount(req);
		status = saDao.addSubAccount(sa);
		json = srp.parseJsonStatus("addUser", status,"");
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
}
