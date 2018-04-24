package com.eoss.appengine.dao;
import com.eoss.appengine.bean.ChatRoom;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class ChatRoomDAO extends DatastoreDAO{
	private final String Tag_Name ="ChatRoom";
	public String addRoom(ChatRoom chatRoom) {
		Entity ent = new Entity(Tag_Name,chatRoom.getRoomId());
		ent.setProperty("botPath", chatRoom.getBotPath());
		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public Entity getChatRoom(String  roomId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, roomId));
		} catch (EntityNotFoundException e) {

		}
		return ent;
	}
	
	public String delChatRoom(String  roomId) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, roomId));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
}
