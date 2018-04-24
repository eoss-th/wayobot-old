document.onreadystatechange = function() {
	if (document.readyState == "complete") {
		var d = document;
		var Eosscontainer = d.getElementById("eoss-chatbox-container");		
		var frontUrl = Eosscontainer.dataset.domain;
		var pusherKey = "c256028969d0d8721d7d";
		var pusherCluster = "ap1";	
		var titleName = Eosscontainer.dataset.title;
		var cookies = (document.cookie.match(/^(?:.*;)?\s*eoss_Session\s*=\s*([^;]+)(?:.*)?$/)||[,null])[1];
		var botId = Eosscontainer.dataset.botid;
		var accountId = Eosscontainer.dataset.accountid;
		var cookiesBot = document.cookie.indexOf(botId+'=');
		if(cookies == null){
			setSession();
		}else{
			var cookie = readCookie("eoss_Session");
			getChatlog(cookie);
		}
	
		var cssId = 'eoss-chat-room-style';
		if (!document.getElementById(cssId)) {
			var head = document.getElementsByTagName('head')[0];
			var link = document.createElement('link');
			link.id = cssId;
			link.rel = 'stylesheet';
			link.type = 'text/css';
			link.href = frontUrl+'/css/eoss-chat-room-style.css';
			link.media = 'all';
			head.appendChild(link);
		}
		
		var Eossheader = "<div class=\"eoss_msg_head\">"+ titleName +"</div>";
		var Eossbody = "<div class=\"eoss_msg_wrap\" style=\"display:none;\"><div class=\"eoss_msg_body\" id=\"eoss_msg_body\"><div class=\"border\"></div></div><div class=\"eoss_msg_footer\"><textarea id=\"eoss-text-area-chat\" class=\"eoss_msg_input\" rows=\"4\"></textarea><button id=\"eoss-sendMessage\">SEND</button></div></div>"
		Eosscontainer.innerHTML += Eossheader;
		Eosscontainer.innerHTML += Eossbody;
	
		var eoss_chat_head_button = document.getElementsByClassName('eoss_msg_head')[0];
		eoss_chat_head_button.addEventListener("click",function(e){
			var button = document.getElementsByClassName('eoss_msg_wrap')[0];
	    	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
	    		eossToggleShow();
	    		toggleclass();
	    	}else{
	    		eossToggleShow();
	    	}			
	    	
	    	scrollToLast();
		},false);
		
		if(cookiesBot == -1){
			createCookie(botId,true);
	    	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
	    		toggleclass();
	    	}
	    	eossToggleShow();
		}
		
		var eossTextArea = d.getElementById("eoss-text-area-chat");
		function eossTextAreaKeyPress(e){
		    var keyCode = e.keyCode;
		    if(keyCode == 13){
		    	e.preventDefault();
		    	var msg = eossTextArea.value;
				if(msg!=''){
					sendMessageToEossBot(msg);
					eossTextArea.value = "";
				}
		    }
		};
		
		eossTextArea.addEventListener("keypress", eossTextAreaKeyPress, false);
		
		if (Eosscontainer.dataset.greet) {
			addBotMessage(Eosscontainer.dataset.greet);
		}
		
		function addBotMessage(message){
			
			if(message != ""){
	 			var texts = message.split('\n\n');
	 			for (var i = 0; i < texts.length; i++) {
					var innerBody = d.getElementById("eoss_msg_body");
					var word = "<div class=\"eoss_msg_l\">" + texts[i] + "</div>";
					innerBody.innerHTML += word;	
	 			}
			}

		}
		
		function addMessage(message){
			if(message != ""){
				var innerBody = d.getElementById("eoss_msg_body");
				var word = "<div class=\"eoss_msg_r\">" + message + "</div>";
				innerBody.innerHTML += word;
			}
		}
		
		function sendMessageToEossBot(msg){
			var text = msg;
			var sessionId = readCookie("eoss_Session");
		 	addMessage(text);
		 	
		 	if(text){
		 		
		 		var http = new XMLHttpRequest();
		 		var url = frontUrl+"/messageService/"+accountId+"/"+botId;
		 		var params = "message="+text+"&sessionId="+sessionId;
		 		http.open("POST", url, true);
	
		 		//Send the proper header information along with the request
		 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	
		 		http.onreadystatechange = function() {//Call a function when the state changes.
		 		    if(http.readyState == 4 && http.status == 200) {
			 			var data = http.responseText;
			 			addBotMessage(data);
			 			scrollToLast();
		 		    }
		 		}
		 		http.send(params);	 		
		 		
		 	} 	
		}
	    function scrollToLast(){
	    	  var elem = d.getElementById('eoss_msg_body');
	    	  elem.scrollTop = elem.scrollHeight;    	
	    }
		function createCookie(name, value, days, path, domain, secure) {
			var cookieString;
			  if (days) {
			    var date = new Date();
			    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
			    var expires = date.toGMTString();
			  } else var expires = "";
			  cookieString = name + "=" + escape(value);
			  if (expires)
			    cookieString += "; expires=" + expires;
			  if (path)
			    cookieString += "; path=" + escape(path);
			  if (domain)
			    cookieString += "; domain=" + escape(domain);
			  if (secure)
			    cookieString += "; secure";
			  document.cookie = cookieString;
		}	
		
		function readCookie(name) {
		    var nameEQ = name + "=";
		    var ca = document.cookie.split(';');
		    for(var i=0;i < ca.length;i++) {
		        var c = ca[i];
		        while (c.charAt(0)==' ') c = c.substring(1,c.length);
		        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		    }
		    return null;
		}
		
		function setSession(){
				var url = frontUrl+"/messageService";
			 	var ajax = new XMLHttpRequest();
			 	ajax.open("GET", url, true);
			 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			 	ajax.send();
	
			 	ajax.onreadystatechange = function() {
			 	  	if (ajax.readyState == 4 && ajax.status == 200) {	
			 	  		var data = ajax.responseText;
			 	  		createCookie("eoss_Session",data);
			 		}
			 	}		
		}	
		
		function getChatlog(sessionId){		
			var url = frontUrl+"/chatLogService?sessionId="+sessionId+"&botId="+botId;
		 	var ajax = new XMLHttpRequest();
		 	ajax.open("GET", url, true);
		 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		 	ajax.send();
	
		 	ajax.onreadystatechange = function() {
		 	  	if (ajax.readyState == 4 && ajax.status == 200) {	
		 	  		var data = ajax.responseText;
		 			var json = JSON.parse(data);
		 			
		 			for(var i = 0; i < json.length; i++) {
		 			    var obj = json[i];
		 			   addMessage(obj.propertyMap.message.value.value);
		 			   addBotMessage(obj.propertyMap.replyMessage.value.value);

		 			}	
		 		}
		 	}	
		}	
		function addChatlog(msg){	
			var sessionId = readCookie("eoss_Session");			
			var url = frontUrl+"/chatLogService";
	 		var http = new XMLHttpRequest();

	 		var params = "message="+msg+"&botId="+botId+"&sessionId="+sessionId;
	 		http.open("POST", url, true);

	 		//Send the proper header information along with the request
	 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	 		http.onreadystatechange = function() {//Call a function when the state changes.
	 		    if(http.readyState == 4 && http.status == 200) {
	 		    	
	 		    }
	 		}
	 		http.send(params);	 	
		}			
		
	    var pusher = new Pusher(pusherKey, {
	        cluster: pusherCluster,
	        authEndpoint: frontUrl+'/authorize/'+accountId,
	        encrypted: true
	    });	
	    
	    var channel = pusher.subscribe("presence-"+botId);
	    pusher.connection.bind('connected', function () {
	
	        function handleMessage(data) {
	        	addChatlog(data.message)
	        	addBotMessage(data.message);
	        	scrollToLast();
	        }
	
	        // subscribe to new messages in the chat application
	        channel.bind('new_message', function (data) {
	            handleMessage(data);
	        });
	
	    });
	    //get a reference to the element
	    var eossSendbutton = d.getElementById('eoss-sendMessage');

	    //add event listener
	    eossSendbutton.addEventListener('click', function(event) {
	    	
            var msg = d.getElementById("eoss-text-area-chat").value;
            d.getElementById("eoss-text-area-chat").value="";
			if(msg!=''){
				sendMessageToEossBot(msg);
			}
	    });
	    
	    function toggleclass() {    
	        var elems = d.getElementsByClassName("eoss_msg_box");
	        for (var i = 0; i < elems.length; ++i) {
	            if (elems[i].className.indexOf("eoss_mobile") > -1)
	                elems[i].className = "eoss_msg_box";    
	            else
	                elems[i].className += " eoss_mobile";
	        }
	    }
	    
	    function eossToggleShow() {
	        var eossToggle = d.getElementsByClassName("eoss_msg_wrap")[0];
	        if (eossToggle.style.display === "none") {
	        	eossToggle.style.display = "block";
	        } else {
	        	eossToggle.style.display = "none";
	        }
	    } 
	}
}