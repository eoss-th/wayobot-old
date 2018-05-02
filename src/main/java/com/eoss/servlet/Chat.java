package com.eoss.servlet;

import java.util.Date;

public class Chat {

	public final String name;
	public final String message;
	public final long timestamp;
	public final String chatImg;

	public Chat(String name, String message,String chatImg) {
		this.name = name;
		this.message = message;
		this.chatImg = chatImg;
		this.timestamp = new Date().getTime();
	}

	@Override
	public String toString() {
		return message;
	}

	public String getName() {
		return name;
	}
	
	public String chatImg() {
		return chatImg;
	}
	
	public String getMessage() {
		return message;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

}
