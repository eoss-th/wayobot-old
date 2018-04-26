var greetingWord;
var value;
var fields;
var locale;
var title;
var language;
function getContext(userBotId) {
	overlayPopup('loader');
	var url = "/filter/getContext?userBotId=" + userBotId;
	var ajax = new XMLHttpRequest();
	ajax.open("GET", url, true);
	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	ajax.send();

	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4 && ajax.status == 200) {
			var data = ajax.responseText;
			var json = JSON.parse(data);
			$("#greet").val(json.map.greeting);
			$("#unknown").val(json.map.unknown);
			$("#title").val(json.map.title);
			$('#countryLangContext').val(json.map.language.toUpperCase());
			locale = json.map.language.toUpperCase();
			generateBox($('#selectbotlist').val());
		}
		overlayPopup('loader');
	}
}

function generateBox(val) {
	value = val;
	fields = value.split('_|_');
	botName = fields[1];
	botId = fields[0];
	greetingWord = $("#greet").val();
	title = $("#title").val();
	$("#test-conn-top")
			.text(
					"<div data-domain=\""
							+ domain
							+ "\" data-title=\""+ title +"\" data-botid=\""
							+ botId
							+ "\" data-accountid=\""
							+ accountId
							+ "\" class=\"eoss_msg_box\" id=\"eoss-chatbox-container\" data-greet=\""
							+ greetingWord
							+ "\"></div>\n\n<script src=\""
							+ pusherVer
							+ "\"></script><script>(function(d, s, id) { var js, ejs = d.getElementsByTagName(s)[0];if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;js.src = '"
							+ domain
							+ "/js/eoss-sdk-script.js';ejs.parentNode.insertBefore(js, ejs);}(document, 'script', 'eoss-sdk-script'));</script>");

	makeCode(botId);
} 

var qrcode = new QRCode(document.getElementById("qrcode"), {
	width : 250,
	height : 250
});

function makeCode (value) {		
	var path = accountId+'/'+value;		
	qrcode.makeCode(path);
}

$('#update').click(function () {
	if($("#greet").val() && $("#unknown").val() &&$("#title").val()){
		
		overlayPopup("loader");
		$("#greetForm p").hide();
		fields = value.split('_|_');
 		var http = new XMLHttpRequest();
 		var url = "/filter/getContext";
 		var params = "userBotId="+fields[0]+"&greeting="+$("#greet").val()+"&unknown="+$("#unknown").val()+"&title="+$("#title").val()+"&language="+$("#countryLangContext").val();
 		http.open("POST", url, true);

 		//Send the proper header information along with the request
 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		http.onreadystatechange = function() {//Call a function when the state changes.
 		    if(http.readyState == 4 && http.status == 200) {
	 			var data = http.responseText;
	 			console.log(data);
	 			if(data == "success"){
	 				location.reload();	 				
	 			}else{
	 				$("#greetForm p").show();
	 			}
	 			overlayPopup("loader");
 		    }
 		}
 		http.send(params);			
	}else{
		$("#greetForm p").show();
	}
			
});

$(document).ready(function(){
	
	$('#selectbotlist').on('change', function() {
		
		value = $(this).val();
		fields = value.split('_|_');
		console.log(fields[0]);
		getContext(fields[0]);
	});	
	
});