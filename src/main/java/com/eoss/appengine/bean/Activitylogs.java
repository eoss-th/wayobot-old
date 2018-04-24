package com.eoss.appengine.bean;

import java.util.Date;

public class Activitylogs {
	private String logID;
	private String logLevel;
	private String message;
	private Date logTime;
	private String accountId;
	private String subAccountID;
	
	public String getLogID() {
		return logID;
	}
	public void setLogID(String logID) {
		this.logID = logID;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getSubAccountID() {
		return subAccountID;
	}
	public void setSubAccountID(String subAccountID) {
		this.subAccountID = subAccountID;
	}

}
