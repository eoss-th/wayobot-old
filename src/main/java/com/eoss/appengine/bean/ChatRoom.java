package com.eoss.appengine.bean;

import java.util.List;

public class ChatRoom {
	private String roomId;
	private List<String> botPath;
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public List<String> getBotPath() {
		return botPath;
	}
	public void setBotPath(List<String> botPath) {
		this.botPath = botPath;
	}

}
