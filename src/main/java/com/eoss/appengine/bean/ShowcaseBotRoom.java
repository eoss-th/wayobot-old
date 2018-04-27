package com.eoss.appengine.bean;

import java.util.List;

public class ShowcaseBotRoom {
	private String botId;
	private List<String> roomList;
	
	public String getBotId() {
		return botId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public List<String> getRoomList() {
		return roomList;
	}
	public void setRoomList(List<String> roomList) {
		this.roomList = roomList;
	}

	
}
