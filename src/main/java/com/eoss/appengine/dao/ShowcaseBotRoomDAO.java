package com.eoss.appengine.dao;
import com.eoss.appengine.bean.ShowcaseBotRoom;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

public class ShowcaseBotRoomDAO extends DatastoreDAO{
	private final String Tag_Name ="ShowcaseBotRoom";
	public String addShowcaseBotRoom(ShowcaseBotRoom showcaseBotRoom) {
		Entity ent = new Entity(Tag_Name,showcaseBotRoom.getBotId());
		ent.setProperty("roomList", showcaseBotRoom.getRoomList());

		try {
			getDatastoreService().put(ent);
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		
		return "success";
	}
	
	public Entity getShowcaseBotRoom(String botId) {
		Entity ent = null;
		try {
			ent = getDatastoreService().get(KeyFactory.createKey(Tag_Name, botId));
		} catch (EntityNotFoundException e) {
			return null;
		}
		return ent;
	}
	
	public String delShowcaseBotRoom(String botId) {
		try {
			getDatastoreService().delete(KeyFactory.createKey(Tag_Name, botId));
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return "success";
		
	}
}
