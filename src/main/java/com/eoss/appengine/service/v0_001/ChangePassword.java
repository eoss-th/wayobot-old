package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.dao.SubAccountDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.helper.SetRespPram;
import com.eoss.appengine.helper.ValidateParam;
import com.eoss.appengine.secure.EncryptPassword;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ChangePassword extends HttpServlet{
	SetReqParam srq = new SetReqParam();
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = (String) req.getSession().getAttribute("token");
		ValidateParam valid = new ValidateParam();
		SubAccountDAO subAccountDAO = new SubAccountDAO();
		EncryptPassword encrypt = new EncryptPassword();
		Date d = srq.getCurrentDate();
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		String confirmPassword = req.getParameter("confirmPassword");
		String json = srp.parseJsonStatus("changePassword", "fail", "token null");
		
		if(token != null) {
			String[] parts = token.split("_");
			String email = parts[1];
			Entity ent = subAccountDAO.getSubAccount(email);
			if(ent != null) {
				if(encrypt.encryptPassword(oldPassword).equals(ent.getProperty("passWord"))) {
					if(valid.validatePassword(newPassword)) {
						if(!newPassword.equals(oldPassword)) {
							if(valid.validateComfirmPassword(newPassword, confirmPassword)) {
								ent.setProperty("passWord", encrypt.encryptPassword(newPassword));
								ent.setProperty("editDate", d);
								if(srq.datasotorePut(ent) == "success") {
									json = srp.parseJsonStatus("changePassword", "success", "Change password Success");
								}								
							}else {
								json = srp.parseJsonStatus("changePassword", "fail", "Confirm password not correct");
							}						
						}else {
							json = srp.parseJsonStatus("changePassword", "fail", "Your new password is same as old password please change to new one");
						}
					}else {
						json = srp.parseJsonStatus("changePassword", "fail", "Password lenght must have alleast 8 character and contain 1 Uppercase letter, 1 Lowercase Letter,1 Special Letter");
					}
				}else {
					json = srp.parseJsonStatus("changePassword", "fail", "Old password not correct");
				}			
			}
		}
		resp = srp.setRespHead(resp,System.getenv("domain"));
		resp.getWriter().write(json);
	}
	
}
