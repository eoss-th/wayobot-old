package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.Account;
import com.eoss.appengine.bean.SubAccount;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.AccountDAO;
import com.eoss.appengine.dao.ChatlogsDAO;
import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.appengine.helper.ValidateParam;
import com.eoss.appengine.secure.EncryptPassword;
import com.eoss.brain.net.Context;
import com.eoss.servlet.SessionManager;

@SuppressWarnings("serial")
public class AccountService extends HttpServlet{
	private AccountDAO accountDao = new AccountDAO();
	
	private SetReqParam srq = new SetReqParam();
	private SetRespPram srp = new SetRespPram();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Account account = new Account();
		String status;
		account = srq.setAccount(req);
		status = srp.parseJsonEntityList(accountDao.getAccountList(account));
		resp = srp.setRespHead(resp);
		resp.getWriter().write(status);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Account account = new Account();
		ChatlogsDAO chatlogDao = new ChatlogsDAO();
		SubAccount subAccount = new SubAccount();
		SubAccountDAO subAccountDao = new SubAccountDAO();
		ValidateParam valid = new ValidateParam();
		
		String status = srp.parseJsonStatus("register", "fail","A01");
		String notiNumber = chatlogDao.getChanel();
		String comfirmPass = req.getParameter("passWordConfirm");
		Date d = srq.getCurrentDate();
		
		subAccount = srq.setSubAccount(req);
		
		
		if(checkParameter(subAccount)) {
			if(valid.validatePassword(subAccount.getPassWord())) {
				if(valid.validateComfirmPassword(subAccount.getPassWord(), comfirmPass)) {
					if(valid.validateEmail(subAccount.getEmail().trim())) {
						if(subAccountDao.getSubAccount(subAccount.getEmail()) == null) {
							account = srq.setAccount(req);
							account.setAccountId(UUID.randomUUID().toString().toUpperCase());
							account.setAccRegisDate(d);
							account.setEditDate(d);
							status = accountDao.addAccount(account);
							if(status.equals("success")) {
								EncryptPassword encryptor = new EncryptPassword();
								subAccount.setSubAccName(account.getAccName());
								subAccount.setAccountId(account.getAccountId());
								subAccount.setRole("master");
								subAccount.setEditDate(d);
								subAccount.setSubRegisDate(d);
								subAccount.setPassWord(encryptor.encryptPassword(subAccount.getPassWord()));
								status = subAccountDao.addSubAccount(subAccount);
								
								if(status.equals("success")) {
									UserBots userBot = new UserBots();
									UserBotsDAO userBotDao = new UserBotsDAO();
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
									userBot.setCountry(req.getParameter("country").toLowerCase());
									status = userBotDao.addUserBots(userBot);
									
									String contextName = account.getAccountId()+"/"+notiNumber;
									
									Context context = SessionManager.instance().context(contextName);
									context.properties.put("language",req.getParameter("country").toLowerCase());
									context.properties.put("greeting", System.getenv("greeting"));
									context.properties.put("unknown", System.getenv("unknown"));
									context.properties.put("title", System.getenv("title"));
									context.save();
									
								}
								status = srp.parseJsonStatus("register", status,"");
							}		
						}else {
							status = srp.parseJsonStatus("register", "fail","A02");
						}
						
					}else {
						status = srp.parseJsonStatus("register", "fail","A03");
					}
					
				}else {
					status = srp.parseJsonStatus("register", "fail","A04");
				}
			}else {
				status = srp.parseJsonStatus("register", "fail","A05");
			}
		}

		resp = srp.setRespHead(resp);
		resp.getWriter().write(status);	
	}

	private Boolean checkParameter(SubAccount subAccount) {
		if(subAccount.getSubAccName() == null || subAccount.getSubAccName().isEmpty()) {
			return false;
			
		}
		if(subAccount.getEmail() == null || subAccount.getEmail().isEmpty()) {
			return false;
		}	

		if(subAccount.getPassWord() == null || subAccount.getPassWord().isEmpty()) {
			return false;
		}	

		return true;
	}
	

	
}
