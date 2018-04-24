package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.dao.AccountDAO;
import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.dao.TokenDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.appengine.secure.DecryptPassword;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class SignIn extends HttpServlet {
	private SetReqParam srq = new SetReqParam();
	private SetRespPram srp = new SetRespPram();
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SubAccount subAccount = new SubAccount();
		SubAccountDAO subAccountDao = new SubAccountDAO();
		AccountDAO accountDao = new AccountDAO();
		subAccount = srq.setSubAccount(req);
		String status;
		status = srp.parseJsonStatus("SignIn", "fail", "Username or password is not correct");

			Entity ent = subAccountDao.getSubAccount(subAccount.getEmail());
			
			if (ent != null) {
				Entity account = accountDao.getAccount(ent.getProperty("accountId").toString());
				DecryptPassword dc = new DecryptPassword();
				if(ent.getProperty("passWord") != null) {
					if (dc.checkPassword(subAccount.getPassWord(), ent.getProperty("passWord").toString())) {
						TokenDAO tokendao = new TokenDAO();
						Entity token = tokendao.generateToken(subAccount, 1);
						String json = srp.parseJsonEntity(token);
						status = srp.parseJsonStatus("SignIn", "success", json);
						HttpSession session = req.getSession(true);
						session.setAttribute("token", token.getProperty("tokenId"));
						session.setAttribute("accountId", ent.getProperty("accountId"));
						session.setAttribute("userName", ent.getProperty("subAccName"));
						session.setAttribute("email", ent.getProperty("email"));
						session.setAttribute("role", "administrator");
						session.setAttribute("pusherKey", account.getProperty("pusherKey"));
						session.setAttribute("pusherCluster", account.getProperty("pusherCluster"));
						session.setAttribute("app_id", account.getProperty("app_id"));
						session.setAttribute("app_secret", account.getProperty("app_secret"));
						
						status = srp.parseJsonStatus("SignIn", "success", "");
					}					
				}			
			}
			
			resp = srp.setRespHead(resp);
			resp.getWriter().write(status);

	}
}
