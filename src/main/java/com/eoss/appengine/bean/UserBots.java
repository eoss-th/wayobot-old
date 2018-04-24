package com.eoss.appengine.bean;

import java.util.Date;

public class UserBots {
	private String userBotId;
	private String botId;
	private String accountId;
	private Date expireDate;
	private Date buyDate;
	private Integer remainMessage;
	private Integer toDayMessage;
	private Integer messageCount;
	private String cssId;
	private String status;
	private String notiNumber;
	private Boolean BuyFlag = true;
	private Boolean paid = false;
	private String Country;
	public String getBotId() {
		return botId;
	}
	public String getUserBotId() {
		return userBotId;
	}
	public void setUserBotId(String userBotId) {
		this.userBotId = userBotId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	public Integer getRemainMessage() {
		return remainMessage;
	}
	public void setRemainMessage(Integer remainMessage) {
		this.remainMessage = remainMessage;
	}
	public Integer getToDayMessage() {
		return toDayMessage;
	}
	public void setToDayMessage(Integer toDayMessage) {
		this.toDayMessage = toDayMessage;
	}
	public Integer getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}
	public String getCssId() {
		return cssId;
	}
	public void setCssId(String cssId) {
		this.cssId = cssId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNotiNumber() {
		return notiNumber;
	}
	public void setNotiNumber(String notiNumber) {
		this.notiNumber = notiNumber;
	}

	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public Boolean getBuyFlag() {
		return BuyFlag;
	}
	public void setBuyFlag(Boolean buyFlag) {
		this.BuyFlag = buyFlag;
	}
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
}
