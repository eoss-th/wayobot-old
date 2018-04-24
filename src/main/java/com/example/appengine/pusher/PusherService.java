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

import java.util.HashMap;
import java.util.Map;

import com.eoss.appengine.dao.AccountDAO;
import com.google.appengine.api.datastore.Entity;
import com.pusher.rest.Pusher;

public abstract class PusherService {
	
	private static Map<String, Pusher> pusherMap = new HashMap<>();

  
  public static Pusher getPusher(String accountId) {
	  
	  Pusher pusher = pusherMap.get(accountId);
	  
	  if (pusher==null) {
		  	AccountDAO accountDao = new AccountDAO();

		    Entity ent = accountDao.getAccount(accountId);
		    
		    String APP_ID = ent.getProperty("app_id").toString();
		    String APP_KEY =ent.getProperty("pusherKey").toString();
		    String APP_SECRET =ent.getProperty("app_secret").toString();
		    String CLUSTER = ent.getProperty("pusherCluster").toString();    
		    
		    pusher = new Pusher(APP_ID, APP_KEY, APP_SECRET);
		    pusher.setCluster(CLUSTER); // required, if not default mt1 (us-east-1)
		    pusher.setEncrypted(true); // optional, ensure subscriber also matches these settings
		    pusherMap.put(accountId, pusher);
	  }
    return pusher;
  }
}
