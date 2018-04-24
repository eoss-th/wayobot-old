package com.eoss.appengine.bean;

import java.util.Date;

public class SubAccount {
	private String accountId;
	private String userName;
	private String passWord;
	private String subAccName;
	private String role;
	private Date subRegisDate;
	private String tel;
	private String email;
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	private Date editDate;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getSubAccName() {
		return subAccName;
	}
	public void setSubAccName(String subAccName) {
		this.subAccName = subAccName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getSubRegisDate() {
		return subRegisDate;
	}
	public void setSubRegisDate(Date subRegisDate) {
		this.subRegisDate = subRegisDate;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
