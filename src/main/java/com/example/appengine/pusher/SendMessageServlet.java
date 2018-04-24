/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.pusher;

import com.eoss.appengine.bean.Pusher_count;
import com.eoss.appengine.dao.Pusher_countDAO;
import com.eoss.appengine.helper.SetReqParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.rest.data.Result;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Submit a chat message over a channel. Note : we use socket_id to exclude the
 * sender from receiving the message //
 * {@see <ahref="https://pusher.com/docs/server_api_guide/server_excluding_recipients">Excluding
 * Recipients</ahref>}
 */

@SuppressWarnings("serial")
public class SendMessageServlet extends HttpServlet {
	private SetReqParam srq = new SetReqParam();
	private Gson gson = new GsonBuilder().create();
	private TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {
	};
	
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// pre-flight request processing
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "POST");
		resp.setHeader("Access-Control-Allow-Methods", "GET");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String message = req.getParameter("message");
		String socketId = req.getParameter("socketId");
		String channelId = req.getParameter("channelId");
		
		User user = UserServiceFactory.getUserService().getCurrentUser();

		String displayName = user.getNickname().replaceFirst("@.*", "");		
		String taggedMessage = "<strong>&lt;" + displayName + "&gt;</strong> " + message;
		Map<String, String> messageData = new HashMap<>();
		messageData.put("message", taggedMessage);
		Result result = PusherService.getPusher(req.getSession().getAttribute("accountId").toString()).trigger(channelId, "new_message", // name of event
				messageData, socketId); 


		messageData.put("status", result.getStatus().name());

		resp.getWriter().write(gson.toJson(messageData));		
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String body = CharStreams.readLines(request.getReader()).toString();
		String json = body.replaceFirst("^\\[", "").replaceFirst("\\]$", "");
		Map<String, String> data = gson.fromJson(json, typeReference.getType());
		String message = data.get("message");
		String socketId = data.get("socket_id");
		String channelId = data.get("channel_id");
		String accountid = request.getSession().getAttribute("accountId").toString();
		Pusher_countDAO p_countDao = new Pusher_countDAO();
		Pusher_count p_count = new Pusher_count();
		p_count.setAccountId(accountid);
		p_count.setTimeStamp(srq.getCurrentDateWithPaltform());
		Entity ent = p_countDao.getPusherCount(p_count);
		
		String taggedMessage = message;
		Map<String, String> messageData = new HashMap<>();
		
		
		if(checkMaxCount(ent)) {
			messageData.put("message", taggedMessage);
			Result result = PusherService.getPusher(accountid).trigger(channelId, "new_message", // name of event
					messageData, socketId); 

			messageData.put("status", result.getStatus().name());


			if(result.getStatus().name() == "SUCCESS") {

				
				int count;
				if(ent == null) {
					p_count.setCount(1);
					if(p_countDao.addPusherCount(p_count)=="success") {
						count = 1;
						messageData.put("count", Integer.toString(count));
						messageData.put("maxCount", p_count.getMaxCount().toString());
					}
				}else {
					count = Integer.parseInt(ent.getProperty("count").toString().trim())+1;
					
					ent.setProperty("count", count);
					if(srq.datasotorePut(ent)=="success") {
						messageData.put("count", Integer.toString(count));
						messageData.put("maxCount", ent.getProperty("maxCount").toString().trim());
					}

				}					
				
			}			
		}else {
			messageData.put("message", "counter is max");
			messageData.put("status", "counter is max");
		}


		response.getWriter().write(gson.toJson(messageData));
	}
	
	private Boolean checkMaxCount(Entity ent) {
		if(ent == null) {
			return true;
		}
		if(Integer.parseInt(ent.getProperty("count").toString()) < Integer.parseInt(ent.getProperty("maxCount").toString())) {
			return true;
		}
		
		return false;
	}
}
