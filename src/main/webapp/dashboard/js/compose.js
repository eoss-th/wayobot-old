var selectedUserBot;
var fields;
var head_select;
var botId;
// get context log
function getContextLog() {
	overlayPopup('loader');
	var http = new XMLHttpRequest();
	var url = "/messageService/"+accountId+"/"+botId;
	var params = "message=&sessionId=" + userEmail + "&cmd=get&header=" + head_select;
	http.open("POST", url, true);
	//Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {//Call a function when the state changes.
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
			if (data.length != 0) {
				$("#chat_widget_input").val(data);
				$("#clearCompose").show();
				$("#example").hide();
				getChatlog();
			} else {
				$("#clearCompose").hide();
				$("#example").show();
				$("#chat_widget_input").val("");
			}
			$("#clearCompose").attr("disabled", false);
			overlayPopup('loader');
		}
	}
	http.send(params);
}

//get Chat log
function getChatlog(){	
	var textArea = document.getElementById("chatLogTextArea");
	var url = "/adminChatLogService?botId="+botId+"&readFlag=false";
 	var ajax = new XMLHttpRequest();
 	ajax.open("GET", url, true);
 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
 	ajax.send();

 	ajax.onreadystatechange = function() {
 	  	if (ajax.readyState == 4 && ajax.status == 200) {	
 	  		textArea.innerHTML = "";
 	  		var data = ajax.responseText;
 			var json = JSON.parse(data);
 			$("#unknowNo").text("( "+json.length+" )");
 			for(var i = 0; i < json.length; i++) {
 			    var obj = json[i];
 			   textArea.innerHTML += 'Q:'+ obj.propertyMap.message.value.value +"<br>";
 			   textArea.innerHTML += 'A:'+ obj.propertyMap.replyLog.value.value +"<br>";	   
 			}	
 		}
 	}	
}	

//import context
$('#importCompose').click(function () {
  	var chat_widget_input = $('#chat_widget_input'),
    chat_widget_button = $('#chat_widget_button'),
    message = chat_widget_input.val(); //get the value from the text input

    // trigger a server-side endpoint to send the message via Pusher
	var text = message;

 	if(text){
 		overlayPopup('loader');
 		$("#importCompose").attr("disabled", true);
 		var http = new XMLHttpRequest();
 		var url = "/messageService/"+accountId+"/"+botId;
 		var params = "message="+text+"&sessionId="+userEmail+"&cmd=import&header="+head_select;
 		http.open("POST", url, true);

 		//Send the proper header information along with the request
 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		http.onreadystatechange = function() {//Call a function when the state changes.
 		    if(http.readyState == 4 && http.status == 200) {
	 			var data = http.responseText;
	 			$("#importCompose").removeAttr("disabled");
	 			overlayPopup('loader');
	 			$("#clearCompose").show();
	 			$("#example").hide();
	 			$("#clearCompose").attr("disabled", false);
	 			getChatlog();
 		    }
 		}
 		http.send(params);		
 	} 
});

// clear context
$('#clearCompose').click(function () {
    if(confirm("Your context file and data will be remove doyou want to continue !!")){
    	overlayPopup('loader');
		$("#importCompose").attr("disabled", true);
		$("#clearCompose").attr("disabled", true);
	
	  	var chat_widget_input = $('#chat_widget_input'),
        chat_widget_button = $('#chat_widget_button'),
        message = chat_widget_input.val(); //get the value from the text input

	    // trigger a server-side endpoint to send the message via Pusher
		var text = message;

	 		var http = new XMLHttpRequest();
	 		var url = "/messageService/"+accountId+"/"+botId;
	 		var params = "message="+text+"&sessionId="+userEmail+"&cmd=clear&header="+head_select;
	 		http.open("POST", url, true);

	 		//Send the proper header information along with the request
	 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	 		http.onreadystatechange = function() {//Call a function when the state changes.
	 		    if(http.readyState == 4 && http.status == 200) {
		 			var data = http.responseText;
		 			
					$("#chat_widget_input").val("");
		 			$("#clearCompose").hide();
		 			$("#importCompose").attr("disabled", false);
		 			overlayPopup('loader');
		 			$("#example").show();
	 		    }
	 		}
	 		http.send(params);		
	 		
	 	
    }
    else{
        return false;
    }
});

// Train chat send message
$('#TestChat').click(function () {
	var msg = $("#testChatText").val();
	$("#testChatText").val('');
	if(msg!=''){
		sendMessageToEossBot(msg);
	}
})
// Train chat send message
function sendMessageToEossBot(msg){
	var text = msg;
	$('.TestChat').prop('disabled', true);
	$("#testChatBox").append("<span>Send : "+msg+"</span>");
	$('#testChatBox').scrollTop($('#testChatBox')[0].scrollHeight);
 	if(text){
 		
 		var http = new XMLHttpRequest();
 		var url = "/messageService/"+accountId+"/"+botId;
 		var params = "message="+text+"&sessionId="+userEmail+"&cmd=";
 		http.open("POST", url, true);

 		//Send the proper header information along with the request
 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		http.onreadystatechange = function() {//Call a function when the state changes.
 		    if(http.readyState == 4 && http.status == 200) {
	 			var data = http.responseText;
	 			$("#testChatBox").append("<span>Reply : "+data+"</span>");
	 			$('#testChatBox').scrollTop($('#testChatBox')[0].scrollHeight);
	 			
 		    }
 		   $('.TestChat').prop('disabled', false);
 		}
 		http.send(params);	 		
 		
 	} 	
}	

//initial text area
var eossTextArea = document.getElementById("testChatText");
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

// on select import method
$( "#head_select" ).change(function() {
	selectedUserBot =  $("#selectbotlist").val();
	head_select = $(this).val();
	fields = selectedUserBot.split('_|_');
	botId = fields[0];
	$("#importCompose").show();
	$("#saveCompose").hide();
	$("#chat_widget_input").val("");
	getContextLog();
	getChatlog();
});		

//on select bot change
$( "#selectbotlist" ).change(function() {
	selectedUserBot =  $(this).val();
	fields = selectedUserBot.split('_|_');
	botId = fields[0];
	$("#importCompose").show();
	$("#saveCompose").hide();
	$("#chat_widget_input").val("");
	getContextLog();
	getChatlog();
});	

// on click like, cancel, great, no
$('.quickrep').click(function () {
	var msg = $(this).attr('name');
	sendMessageToEossBot(msg);
});

// add example
$('#example').click(function () {
	var head_select = $("#head_select").val();
	if(head_select =="type1"){
		$("#chat_widget_input").val(inputQA);
	}else{
		$("#chat_widget_input").val(inputRaw);
	}
});		

