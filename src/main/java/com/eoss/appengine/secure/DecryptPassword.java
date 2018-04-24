package com.eoss.appengine.secure;

public class DecryptPassword {
	EncryptPassword encryptor = new EncryptPassword();
	public boolean checkPassword(String p1,String p2) {
		if(p1 != null && p2 != null) {
			String Resault = encryptor.encryptPassword(p1);
				
			if(Resault.equals(p2)) {
				return true;
			}		
		}

		return false;
	}
}
