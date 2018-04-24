package com.eoss.appengine.bean;

import java.util.Date;

public class Account {
	private String accountId;
	private String accName;
	private String email;
	private Date accRegisDate;
	private Date editDate;
	private Boolean paid = false;
	private String pusherCluster = System.getenv("PUSHER_CLUSTER");
	private String pusherKey = System.getenv("PUSHER_APP_KEY");
	private String app_id = System.getenv("PUSHER_APP_ID");
	private String app_secret = System.getenv("PUSHER_APP_SECRET");
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getAccRegisDate() {
		return accRegisDate;
	}
	public void setAccRegisDate(Date accRegisDate) {
		this.accRegisDate = accRegisDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public String getPusherCluster() {
		return pusherCluster;
	}
	public void setPusherCluster(String pusherCluster) {
		this.pusherCluster = pusherCluster;
	}
	public String getPusherKey() {
		return pusherKey;
	}
	public void setPusherKey(String pusherKey) {
		this.pusherKey = pusherKey;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_secret() {
		return app_secret;
	}
	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	
	
}
