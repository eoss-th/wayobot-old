package com.eoss.appengine.bean;

import java.util.Date;

import com.google.appengine.api.datastore.Text;

public class ChatLog {
	private String chanelName;
	private Text message;
	private Date timeStamp;
	private boolean readFlag;
	private String accountId;
	private String botId;
	private Text replyMessage;
	private String contextName;
	private Text replyLog;
	public String getChanelName() {
		return chanelName;
	}
	public void setChanelName(String chanelName) {
		this.chanelName = chanelName;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public boolean isReadFlag() {
		return readFlag;
	}
	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getBotId() {
		return botId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public Text getMessage() {
		return message;
	}
	public void setMessage(Text message) {
		this.message = message;
	}
	public Text getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(Text replyMessage) {
		this.replyMessage = replyMessage;
	}
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
	public Text getReplyLog() {
		return replyLog;
	}
	public void setReplyLog(Text replyLog) {
		this.replyLog = replyLog;
	}



}
