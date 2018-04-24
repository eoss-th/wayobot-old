package com.eoss.appengine.helper;

import java.util.regex.Pattern;
import com.eoss.appengine.dao.SubAccountDAO;

public class ValidateParam {
	public boolean validatePassword(String password) {
	    Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	    Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
	    Pattern lowerCasePatten = Pattern.compile("[a-z ]");
	    Pattern digitCasePatten = Pattern.compile("[0-9 ]");
		
	    if (password.length() < 8) {
	        return false;
	    }
	    if (!specailCharPatten.matcher(password).find()) {
	        return false;
	    }
	    if (!UpperCasePatten.matcher(password).find()) {
	        return false;
	    }
	    if (!lowerCasePatten.matcher(password).find()) {
	        return false;
	    }
	    if (!digitCasePatten.matcher(password).find()) {
	        return false;
	    }
	    
		return true;	
	}	
	
	public boolean validateComfirmPassword(String password,String confirmPass) {
		if(!password.equals(confirmPass)) {
			return false;
		}
		
		return true;	
	}
	
	public Boolean validateEmail(String email) {
		SubAccountDAO subAccountDao = new SubAccountDAO();
	
		if(subAccountDao.getSubAccount(email) != null) {
			return false;
		}
	
		return true;
	}
}
