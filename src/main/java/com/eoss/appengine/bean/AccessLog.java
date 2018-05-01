package com.eoss.appengine.bean;

import java.util.Date;

public class AccessLog {
	private String reqIp;
	private String reqHost;
	private String reqPath;
	private String reqQueryString;
	private String reqHeader;
	private String _createAt;
	private Date timeStamp;
	
	public String getReqIp() {
		return reqIp;
	}
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	public String getReqHost() {
		return reqHost;
	}
	public void setReqHost(String reqHost) {
		this.reqHost = reqHost;
	}
	public String getReqPath() {
		return reqPath;
	}
	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getReqQueryString() {
		return reqQueryString;
	}
	public void setReqQueryString(String reqQueryString) {
		this.reqQueryString = reqQueryString;
	}
	public String getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}
	public String get_createAt() {
		return _createAt;
	}
	public void set_createAt(String _createAt) {
		this._createAt = _createAt;
	}
	
	
}
