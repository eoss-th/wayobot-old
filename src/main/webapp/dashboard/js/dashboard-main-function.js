var userBotList;

//get userbot list and append to elem
function getUserBotList(url,elem) {
	overlayPopup('loader');
	return $.ajax({
		url : url,
		cache : false,
		success : function(json) {
			$(elem).empty();
			userBotList = json;
			for (var i = 0; i < userBotList.length; i++) {
				var obj = userBotList[i];
				console.log(botListSelected);
				if (obj.propertyMap.notiNumber == botListSelected) {
					$(elem).append(
							"<option value=\"" + obj.propertyMap.notiNumber
									+ "_|_" + obj.propertyMap.botId + "_|_"
									+ obj.propertyMap.country + "\" selected>"
									+ obj.propertyMap.botId + "</option>");
				} else {
					$(elem).append(
							"<option value=\"" + obj.propertyMap.notiNumber
									+ "_|_" + obj.propertyMap.botId + "_|_"
									+ obj.propertyMap.country + "\">"
									+ obj.propertyMap.botId + "</option>");
				}				
			}
			overlayPopup('loader');
		}
	});
}

//append botlist to botlist table dashboard.jsp
function appendBotlistTable(userBotList) {
	for (var i = 0; i < userBotList.length; i++) {
		var obj = userBotList[i];
		
		if (obj.propertyMap.BuyFlag == false) {
			$("#botlist")
					.append(
							"<tr>"
									+ "<td class=\"mobile\">"
									+ obj.key.name
									+ "</td>"
									+ "<td>"
									+ obj.propertyMap.botId
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.country
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.buyDate
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.expireDate
									+ "</td>"
									+ "<td><button class=\"btn btn-default btn-block "
									+ obj.key.name
									+ "\" disabled=\"disabled\">"+paymentProcess_text+"</button></td>"
									+ "</tr>");
		} else {
			$("#botlist")
					.append(
							"<tr>"
									+ "<td class=\"mobile\">"
									+ obj.key.name
									+ "</td>"
									+ "<td>"
									+ obj.propertyMap.botId
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.country
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.buyDate
									+ "</td>"
									+ "<td class=\"mobile\">"
									+ obj.propertyMap.expireDate
									+ "</td>"
									+ "<td><button class=\"btn btn-default btn-block "
									+ obj.key.name
									+ "\" onclick=\"getBuyFlag($(this).val())\" value=\""
									+ obj.key.name
									+ "\">"+renew_text+"</button></td>"
									+ "</tr>");
		}		
		
	}
}

// Set Select userBot to session
function setAttr(selected) {
	var attr = selected.split('_|_');
	var http = new XMLHttpRequest();
	var url = "/filter/setAttr";
	var params = "botListSelected=" + attr[0];
	http.open("POST", url, true);

	// Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {// Call a function when the state
											// changes.
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
		}
	}
	http.send(params);
}

//popup overlay
function overlayPopup(elm) {
	var element = document.getElementById(elm);
	if (element.style.display === "none") {
		element.style.display = "block";
	}else{
		element.style.display = "none";
	}
}


//get buying status if status can't buy disable renew button
function getBuyFlag(value) {
	var http = new XMLHttpRequest();
	var url = "/filter/getBuyFlagService?botId=" + value;
	http.open("GET", url, true);

	// Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {// Call a function when the state
											// changes.
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
			if (data == "true") {
				getPaymentButton(value);
			} else {
				$("." + value).attr("disabled", "disabled");
				$("." + value).html("Payment in process");
			}

		}
	}
	http.send();
}

// copy elem to clipBoard
function copyToclipBoard(elem) {
	/* Get the text field */
	var copyText = document.getElementById(elem);

	/* Select the text field */
	copyText.select();

	/* Copy the text inside the text field */
	document.execCommand("Copy");

	/* Alert the copied text */
	alert("Copied the text: " + copyText.value);
}

//add newbot to datastore
function addNewBot() {
	overlayPopup('loader');
	if ($("#countryLang").val() && $("#addBotName").val()
			&& $("#addBotGreet").val() && $("#addUnknownWord").val()
			&& $("#addBotTitle").val()) {
		$(".alert_newbot").remove();
		var http = new XMLHttpRequest();
		var url = "/service/v0_001/userBotService";
		var params = "country=" + $("#countryLang").val() + "&botId="
				+ $("#addBotName").val() + "&accountId=" + accountId + "&greeting="
				+ $("#addBotGreet").val() + "&unknown="
				+ $("#addUnknownWord").val() + "&title="
				+ $("#addBotTitle").val();
		http.open("PUT", url, true);

		// Send the proper header information along with the request
		http.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");

		http.onreadystatechange = function() {// Call a function when the
												// state changes.
			if (http.readyState == 4 && http.status == 200) {
				var data = http.responseText;
				if (data == "success") {
					location.reload();
				} else {
					$(".alert_newbot").remove();
					$(".overlaybot")
							.prepend(
									"<p class=\"alert_newbot\" style=\"color:red;\">Please in put all the field</p>");
				}
			}
			overlayPopup('loader');
		}
		http.send(params);
	} else {
		$(".alert_newbot").remove();
		$(".overlaybot")
				.prepend(
						"<p class=\"alert_newbot\" style=\"color:red;\">Please in put all the field</p>");
	}

}

// get pusher count
function getPushCount(url) {
	var ajax = new XMLHttpRequest();
	ajax.open("GET", url, true);
	ajax.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded");
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
	$(".remainQuota").html(
			"Remain " + json.count + "/" + json.maxCount
					+ " message you can send today");

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

	// Show the current tab, and add an "active" class to the button that opened
	// the tab
	document.getElementById(tabName).style.display = "block";
	evt.currentTarget.className += " active";

}

function getPaymentButton(botId) {
	var ajax = new XMLHttpRequest();
	ajax.open("GET", "/filter/service/v0_001/botService", true);
	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	ajax.send();

	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4 && ajax.status == 200) {
			var data = ajax.responseText;
			var json = JSON.parse(data);
			$("#paymentButtonList").empty();
			$(".paylabel").text("Renew for bot ID :" + botId);
			for (var i = 0; i < json.length; i++) {
				var obj = json[i];
				var button = '<div class="form-group overlayBotCenter">'
								+'<label>Renew for '+obj.key.name+'</label>' 
								+'<form action="https://www.coinpayments.net/index.php" method="post">'
									+'<input type="hidden" name="cmd" value="_pay_simple">'
									+'<input type="hidden" class="customPay" name="custom" value="'+botId + "_" + accountId+'">'
									+'<input type="hidden" name="reset" value="1">'
									+'<input type="hidden" name="merchant" value="'+ merchantId +'">'
									+'<input type="hidden" name="item_name" value="'+ obj.key.name +'">'
									+'<input type="hidden" name="item_desc" value="'+ obj.propertyMap.info +'">'
									+'<input type="hidden" name="item_number" value="'+ obj.propertyMap.age +'">'
									+'<input type="hidden" name="currency" value="USD">'
									+'<input type="hidden" name="amountf" value="'+ obj.propertyMap.price +'">'
									+'<input type="hidden" name="want_shipping" value="0">'
									+'<input type="hidden" class="success_url" name="success_url" value="'+ domain +'/billing?botId='+botId+'">'
									+'<input type="hidden" name="cancel_url" value="'+domain+'/dashboard/dashboard.jsp">'
									+'<input type="image" src="https://www.coinpayments.net/images/pub/buynow-grey.png" alt="Buy Now with CoinPayments.net">'
								+'</form>'
							 +'</div>';

				$("#paymentButtonList").append(button);
				
			}
			overlayPopup("overlay-payment");
		}
	}
}	

$("#logoutFormId").submit(function(e) {
	e.preventDefault();
	
 	var ajax = new XMLHttpRequest();
 	ajax.open("POST", "/filter/service/signOut", true);
 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
 	ajax.send();

 	ajax.onreadystatechange = function() {
 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
 			var data = ajax.responseText;
 			if(data == "success"){
 				fbLogoutUser();
 			}
 		}
 	}	
});

$( document ).ready(function() {
	var pagename = location.pathname.substring(location.pathname.lastIndexOf("/") + 1)+"";
	pagename = pagename.replace(".jsp","");
	$("."+pagename).addClass("active");
	$("#PageName").html(pagename.replace("-"," "));
});
