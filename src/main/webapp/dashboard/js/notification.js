
var socket_id = null;
var channel_name = null;
var notinum;
var userCount = 0;

var pusher = new Pusher(pusherKey, {
    cluster: pusherCluster,
    authEndpoint: '/authorize/'+accountId,
    encrypted: true
});	

var channel = pusher.subscribe("presence-test");
pusher.connection.bind('connected', function () {
	$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function(){
		notinum = $("#selectbotlist").val();
		var fields = notinum.split('_|_');
		connectPusher(fields[0],fields[1]);
		getPushCount("/filter/service/v0_001/pusherCountService");
		$("#chat_widget_button").show();		
	});
});

$('#selectbotlist').on('change', function() {
	notinum = $(this).val();
	var fields = notinum.split('_|_');
	connectPusher(fields[0],fields[1])
});

function connectPusher(data,botName) {
	channel = pusher.unsubscribe(channel_name);
	channel_name = "presence-"+data;
	channel = pusher.subscribe(channel_name);
    channel.bind('pusher:subscription_succeeded', function (members) {
    	$("#botName").text(botName);
    	userCount = members.count-1;
    	updateCount();	
    });

    // presence channel receive events when members are added / removed
    channel.bind('pusher:member_added', function (members) {
    	userCount = userCount+1;
    	updateCount();
    });
    channel.bind('pusher:member_removed', function (members) {
    	userCount = userCount-1;
    	updateCount();
    });	
}

function updateCount(){
	$("#userCount").text(userCount);

}
// submit the message to /chat
$('#chat_widget_button').click(function () {
	$("#chat_widget_button").attr("disabled", true);
	$("#chat_widget_button").val(sending_text);
    var chat_widget_input = $('#chat_widget_input'),
        chat_widget_button = $('#chat_widget_button'),
        message = chat_widget_input.val(); //get the value from the text input
    var data = JSON.stringify({
        message: message,
        channel_id: channel_name,
        socket_id: socket_id,
        accountId: accountId
    }); 
    // trigger a server-side endpoint to send the message via Pusher
    $.post('/filter/message', data,
        function (msg) {

            if (msg.status == "SUCCESS") {
            	$(".remainQuota").html("Remain "+msg.count+"/"+msg.maxCount+" message you can send today");
            } else {
                alert("Error sending chat message : " + msg.status);
            }
        	$("#chat_widget_button").attr("disabled", false);
        	$("#chat_widget_button").val(notification_text);
        }, "json");

});