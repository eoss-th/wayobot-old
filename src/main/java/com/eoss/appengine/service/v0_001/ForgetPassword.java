package com.eoss.appengine.service.v0_001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.helper.SendEmail;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.appengine.secure.EncryptPassword;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ForgetPassword extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	SetReqParam srq = new SetReqParam();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EncryptPassword encryptor = new EncryptPassword();
		String json = srp.parseJsonStatus("forgetpassword", "fail", "Not found this email on system");
		String sendTo = req.getParameter("email");
		SubAccountDAO subAccountDao = new SubAccountDAO();
		Entity ent = subAccountDao.getSubAccount(sendTo);
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#%^?";
		String pwd = RandomStringUtils.random( 8, characters );
		if(ent != null) {
			ent.setProperty("passWord", encryptor.encryptPassword(pwd));
			if(srq.datasotorePut(ent) == "success") {
				SendEmail sendEmail = new SendEmail();
				json = srp.parseJsonStatus("forgetpassword", "success", "We send your new password to your email please use it to log in");
				sendEmail.send("text","Reset Password", "Please use this password to log in and please change it to new password<br/>Password: "+pwd, sendTo, System.getenv("senGridEmail"));				
			}
		}
		resp = srp.setRespHead(resp);
		resp.getWriter().write(json);
	}
}
