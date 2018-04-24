	var greeting;
	var unknown;
	function checksel() {
		if($('#selectbotlist').val() != null){
		    		  		
			clearInterval(checkselect);
		}
	}
	
	var checkselect = setInterval(doGet("/service/v0_001/userBotService"), 500);
	// update service list
	function doGet(url) {
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
			 			    if(obj.propertyMap.notiNumber == selectSession){
			 			    	$("#selectbotlist").append("<option value=\""+obj.propertyMap.notiNumber+"_|_"+obj.propertyMap.botId+"_|_"+obj.propertyMap.country+"\" selected>"+obj.propertyMap.botId+"</option>");
			 			    }else{
			 			    	$("#selectbotlist").append("<option value=\""+obj.propertyMap.notiNumber+"_|_"+obj.propertyMap.botId+"_|_"+obj.propertyMap.country+"\">"+obj.propertyMap.botId+"</option>");
			 			    }	
			 			    
			 			    if(obj.propertyMap.BuyFlag == false){
					 			$("#botlist").append("<tr>"
				 						+"<td class=\"mobile\">"+ obj.key.name +"</td>"
							   				+"<td>"+ obj.propertyMap.botId +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.country +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.buyDate +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.expireDate +"</td>"
							   				+"<td><button class=\"btn btn-default btn-block "+obj.key.name+"\" disabled=\"disabled\">Payment in process</button></td>"
					   						+"</tr>");				 			    	
			 			    }else{
					 			$("#botlist").append("<tr>"
				 						+"<td class=\"mobile\">"+ obj.key.name +"</td>"
							   				+"<td>"+ obj.propertyMap.botId +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.country +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.buyDate +"</td>"
							   				+"<td class=\"mobile\">"+ obj.propertyMap.expireDate +"</td>"
							   				+"<td><button class=\"btn btn-default btn-block "+obj.key.name+"\" onclick=\"getBuyFlag($(this).val())\" value=\""+obj.key.name+"\">Renew</button></td>"
					   						+"</tr>");				 			    	
			 			    }

			 			}		 						 			  	 	  							
		 		}
		 	}

	}
	

	function setAttr(select) {
		var fields = select.split('_|_');
 		var http = new XMLHttpRequest();
 		var url = "/setAttr";
 		var params = "select="+fields[0];
 		http.open("POST", url, true);

 		//Send the proper header information along with the request
 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		http.onreadystatechange = function() {//Call a function when the state changes.
 		    if(http.readyState == 4 && http.status == 200) {
	 			var data = http.responseText;
 		    }
 		}
 		http.send(params);	
	}
	
	function openTab(evt, tabName) {
	    // Declare all variables
	    var i, tabcontent, tablinks;
	    getContextLog();
	    // Get all elements with class="tabcontent" and hide them
	    tabcontent = document.getElementsByClassName("tabcontent");
	    for (i = 0; i < tabcontent.length; i++) {
	        tabcontent[i].style.display = "none";
	    }

	    // Get all elements with class="tablinks" and remove the class "active"
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }

	    // Show the current tab, and add an "active" class to the button that opened the tab
	    document.getElementById(tabName).style.display = "block";
	    evt.currentTarget.className += " active";
	    
	} 	
	
	$( "#addNewBotButton" ).click(function() {
		addNewBot();
	});	
	
	function addNewBot() {
		
		if($("#countryLang").val() && $("#addBotName").val() && $("#addBotGreet").val() && $("#addUnknownWord").val() && $("#addBotTitle").val()){
			$(".alert_newbot").remove();
	 		var http = new XMLHttpRequest();
	 		var url = "/service/v0_001/userBotService";
	 		var params = "country="+$("#countryLang").val()+"&botId="+$("#addBotName").val()+"&aci="+aci+"&greeting="+$("#addBotGreet").val()+"&unknown="+$("#addUnknownWord").val()+"&title="+$("#addBotTitle").val();
	 		http.open("PUT", url, true);

	 		//Send the proper header information along with the request
	 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	 		http.onreadystatechange = function() {//Call a function when the state changes.
	 		    if(http.readyState == 4 && http.status == 200) {
		 			var data = http.responseText;
		 			if(data == "success"){
		 				location.reload();
		 			}else{
		 				$(".alert_newbot").remove();
		 				$(".overlaybot").prepend("<p class=\"alert_newbot\" style=\"color:red;\">Please in put all the field</p>");
		 			}
	 		    }
	 		}
	 		http.send(params);			
		}else{
			$(".alert_newbot").remove();
			$(".overlaybot").prepend("<p class=\"alert_newbot\" style=\"color:red;\">Please in put all the field</p>");
		}
	
	} 	
	
    function ToggleShow() {
        var x = document.getElementById("overlaynewbot");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    } 	

    function popPay(value) {
        var x = document.getElementById("overlaynewPay");
        if (x.style.display === "none") {
                x.style.display = "block";
                $(".customPay").val(value+"_"+aci);
                $(".paylabel").text("Renew for bot ID :"+value);
                $(".success_url").val(domain+"/billing?botId="+value);     
                
        } else {
            x.style.display = "none";
        }
    } 
    
	function getBuyFlag(value) {
 		var http = new XMLHttpRequest();
 		var url = "/getBuyFlagService?botId="+value;
 		http.open("GET", url, true);

 		//Send the proper header information along with the request
 		http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

 		http.onreadystatechange = function() {//Call a function when the state changes.
 		    if(http.readyState == 4 && http.status == 200) {
	 			var data = http.responseText;
	 			if(data == "true"){
	 				popPay(value);
	 			}else{
	        		$("."+value).attr("disabled","disabled");
	        		$("."+value).html("Payment in process");
	 			}
	 			
 		    }
 		}
 		http.send();	
	} 	
	
$( document ).ready(function() {
	
	getPushCount("/service/v0_001/pusherCountService");
	
	$( "#logout" ).click(function() {
		  $("#logoutForm").click();
	});
	
	
	function getPushCount(url) {
	 	var ajax = new XMLHttpRequest();
	 	ajax.open("GET", url, true);
	 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	 	ajax.send();

	 	ajax.onreadystatechange = function() {
	 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
	 			var data = ajax.responseText;
	 			addCountNumber(data);
	 		}
	 	}
	}	
	function addCountNumber(data) {
		var json = JSON.parse(data);
		$(".remainQuota").html("Remain "+json.count+"/"+json.maxCount+" message you can send today");
		
	}

}); 