package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;

import com.eoss.appengine.bean.Account;
import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.AccountDAO;
import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.dao.TokenDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SendEmail;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.appengine.secure.EncryptPassword;
import com.eoss.brain.net.Context;
import com.eoss.servlet.SessionManager;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class FBSignInService extends HttpServlet {
	SetReqParam srq = new SetReqParam();
	SetRespPram srp = new SetRespPram();
	SubAccountDAO subAccountDao = new SubAccountDAO();
	SubAccount subAccount = new SubAccount();
	String accountId;
	String email;
	String name;
	String pusherKey;
	String pusherCluster;
	String app_id;
	String app_secret;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		accountId = req.getParameter("userId");
		email = req.getParameter("email");
		name = req.getParameter("name");
		Date d = srq.getCurrentDate();
		String language = req.getParameter("language");
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#%^?";
		String pwd = RandomStringUtils.random( 8, characters );
		
		AccountDAO accountDao = new AccountDAO();
		Account account = new Account();
		Entity ent = accountDao.getAccount(accountId);
		String status;
		status = srp.parseJsonStatus("SignIn", "fail", "");

		if (ent == null) {
			if (validateEmail()) {

				account.setAccName(name);
				account.setAccountId(accountId);
				account.setEmail(email);
				account.setEditDate(d);
				account.setAccRegisDate(d);

				if (accountDao.addAccount(account).equals("success")) {
					EncryptPassword encryptor = new EncryptPassword();
				
					subAccount.setSubAccName(account.getAccName());
					subAccount.setAccountId(account.getAccountId());
					subAccount.setRole("master");
					subAccount.setEditDate(d);
					subAccount.setSubRegisDate(d);
					subAccount.setPassWord(encryptor.encryptPassword(pwd));
					subAccount.setEmail(email);
					subAccount.setUserName(email);
					subAccount.setTel(null);

					
					if (subAccountDao.addSubAccount(subAccount).equals("success")) {
						
						UserBots userBot = new UserBots();
						UserBotsDAO userBotDao = new UserBotsDAO();
						ChatlogsDAO chatlogDao = new ChatlogsDAO();
						String notiNumber = chatlogDao.getChanel();
						userBot = srq.setUserBot(req);
						userBot.setAccountId(account.getAccountId());
						userBot.setBuyDate(d);
						userBot.setExpireDate(srq.getExpireDate(d, 30));
						userBot.setStatus("enable");
						userBot.setBotId(account.getAccName());
						userBot.setCssId("default1");
						userBot.setMessageCount(0);
						userBot.setToDayMessage(0);
						userBot.setRemainMessage(10000);
						userBot.setUserBotId(notiNumber);
						userBot.setNotiNumber(notiNumber);
						userBot.setCountry(language);

						userBotDao.addUserBots(userBot);

						String contextName = account.getAccountId() + "/" + notiNumber;

						Context context = SessionManager.instance().context(contextName);
						context.properties.put("language", language);
						context.properties.put("greeting", System.getenv("greeting"));
						context.properties.put("unknown", System.getenv("unknown"));
						context.properties.put("title", System.getenv("title"));
						context.save();
						
						pusherKey = account.getPusherKey();
						pusherCluster = account.getPusherCluster();
						app_id = account.getApp_id();
						app_secret = account.getApp_secret();
						
						SendEmail sendEmail = new SendEmail();
						sendEmail.send("text","Facebook Register Success", "Thank you for use our service you can use This email and password to login or use facebook login <br/>Email:"+email+"<br/>Password: "+pwd+"<br/>you can change password in dashboard Profile menu", email, System.getenv("senGridEmail"));
						status = srp.parseJsonStatus("Register",setSession(req), "");
					}
				}

			} else {
				status = srp.parseJsonStatus("Register", "fail", "This email is alredy use"+email);
			}
		} else {
			subAccount.setUserName(email);
			pusherKey = ent.getProperty("pusherKey").toString();
			pusherCluster = ent.getProperty("pusherCluster").toString();
			app_id = ent.getProperty("app_id").toString();
			app_secret = ent.getProperty("app_secret").toString();
			
			ent = subAccountDao.getSubAccount(email);
			if (ent != null) {
				status = srp.parseJsonStatus("SignIn",setSession(req), "");
			}
		}
		
		resp = srp.setRespHead(resp);
		resp.getWriter().write(status);
	}

	private String setSession(HttpServletRequest req) {
		TokenDAO tokendao = new TokenDAO();
		
		Entity token = tokendao.generateToken(subAccount, 1);
		HttpSession session = req.getSession(true);
		
		session.setAttribute("token", token.getProperty("tokenId"));
		session.setAttribute("accountId", accountId);
		session.setAttribute("userName", name);
		session.setAttribute("email", email);
		session.setAttribute("role", "administrator");
		session.setAttribute("pusherKey", pusherKey);
		session.setAttribute("pusherCluster", pusherCluster);
		session.setAttribute("app_id", app_id);
		session.setAttribute("app_secret", app_secret);
		
		return "success";
	}

	public Boolean validateEmail() {

		if (subAccountDao.getSubAccount(email) != null) {
			return false;
		}

		return true;
	}
}
