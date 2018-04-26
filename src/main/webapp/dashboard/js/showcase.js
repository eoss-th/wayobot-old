var botId;
var fields;
var selectedUserBot;
function showImgDetail(size,width,height){
	$("#imgWH").text("W:"+ width +" H:" + height);
	$("#imgSize").text(size.toFixed(2)+" Mb");
}

function uploadImg(){	
	var file_data = $('#fileinput').prop('files')[0];   
	var file_extendsion = file_data.name.split('.').pop();

    $.ajax({
    	url: "/bin/"+accountId+"/"+botId,
        type: "POST",
        data: new FormData(document.getElementById("fileForm")),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false
      }).done(function(data) {
    	  overlayPopup('loader');
    	  $("#errImg").hide();
    	  $('#fileinput').val('');
      }).fail(function(jqXHR, textStatus) {
          //alert(jqXHR.responseText);
          alert('File upload failed ...');
          overlayPopup('loader');
          $("#errImg").hide();
      });   

}

function getShowCase() {
	document.getElementById('onOff').checked = false;
	var http = new XMLHttpRequest();
	var url = "/filter/addShowCase?botId="+botId;
	http.open("GET", url, true);
	// Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
			var main = JSON.parse(data);
			if(main.status == "true"){
				var message = JSON.parse(main.message);
				$("#showcaseImage").attr("src",domain+"/bin/"+accountId+"/"+botId);
				$("#title").val(message.propertyMap.title);
				document.getElementById("onOff").checked = message.propertyMap.publish;
				if(message.propertyMap.publish == true){
					$("#showcaseText").text("Shown");
					$("#showcaseText").parent().attr("style","color:green;font-weight:bold");
				}else{
					$("#showcaseText").text("Hidden");
					$("#showcaseText").parent().attr("style","color:red;font-weight:bold");
				}
				$("#description").val(message.propertyMap.description);
				$("#errImg").hide();
			}else{
				$("#errImg").text("Please upload image cover");
				$("#errImg").show();
				$("#showcaseImage").removeAttr("src");
				document.getElementById("onOff").checked = false;
				$("#showcaseText").text("Hidden");
				$("#showcaseText").parent().attr("style","color:red;font-weight:bold");
				$("#title").val("");
				$("#description").val("");
			}
		}
	}
	http.send();	
}

$('#onOff').change(function() {
	var publish = document.getElementById('onOff').checked;

	var http = new XMLHttpRequest();
	var url = "/filter/addShowCase";
	var params = "botId=" + botId + "&publish=" + publish+"&title="+$("#title").val()+"&description="+$("#description").val();
	http.open("POST", url, true);
	// Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {// Call a function when the state
											// changes.
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
			var main = JSON.parse(data);
			if(main.status != "success"){
				alert(main.message);
				document.getElementById('onOff').checked = false;
			}
		}
	}
	http.send(params);
});


// on select bot change
$( "#selectbotlist" ).change(function() {
	$('#fileinput').val('');
	selectedUserBot =  $(this).val();
	fields = selectedUserBot.split('_|_');
	botId = fields[0];
	getShowCase();
});	