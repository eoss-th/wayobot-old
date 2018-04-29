"use strict";

jQuery(document).ready(function ($) {

	$(window).load(function () {
		$(".loaded").fadeOut();
		$(".preloader").delay(1000).fadeOut("slow");
	});
    /*---------------------------------------------*
     * Mobile menu
     ---------------------------------------------*/
    $('#navbar-collapse').find('a[href*=#]:not([href=#])').click(function () {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html,body').animate({
                    scrollTop: (target.offset().top - 40)
                }, 1000);
                if ($('.navbar-toggle').css('display') != 'none') {
                    $(this).parents('.container').find(".navbar-toggle").trigger("click");
                }
                return false;
            }
        }
    });
	
	
	/*---------------------------------------------*
     * For Price Table
     ---------------------------------------------*/
	 
	checkScrolling($('.cd-pricing-body'));
	$(window).on('resize', function(){
		window.requestAnimationFrame(function(){checkScrolling($('.cd-pricing-body'))});
	});
	$('.cd-pricing-body').on('scroll', function(){ 
		var selected = $(this);
		window.requestAnimationFrame(function(){checkScrolling(selected)});
	});

	function checkScrolling(tables){
		tables.each(function(){
			var table= $(this),
				totalTableWidth = parseInt(table.children('.cd-pricing-features').width()),
		 		tableViewport = parseInt(table.width());
			if( table.scrollLeft() >= totalTableWidth - tableViewport -1 ) {
				table.parent('li').addClass('is-ended');
			} else {
				table.parent('li').removeClass('is-ended');
			}
		});
	}

	//switch from monthly to annual pricing tables
	bouncy_filter($('.cd-pricing-container'));

	function bouncy_filter(container) {
		container.each(function(){
			var pricing_table = $(this);
			var filter_list_container = pricing_table.children('.cd-pricing-switcher'),
				filter_radios = filter_list_container.find('input[type="radio"]'),
				pricing_table_wrapper = pricing_table.find('.cd-pricing-wrapper');

			//store pricing table items
			var table_elements = {};
			filter_radios.each(function(){
				var filter_type = $(this).val();
				table_elements[filter_type] = pricing_table_wrapper.find('li[data-type="'+filter_type+'"]');
			});

			//detect input change event
			filter_radios.on('change', function(event){
				event.preventDefault();
				//detect which radio input item was checked
				var selected_filter = $(event.target).val();

				//give higher z-index to the pricing table items selected by the radio input
				show_selected_items(table_elements[selected_filter]);

				//rotate each cd-pricing-wrapper 
				//at the end of the animation hide the not-selected pricing tables and rotate back the .cd-pricing-wrapper
				
				if( !Modernizr.cssanimations ) {
					hide_not_selected_items(table_elements, selected_filter);
					pricing_table_wrapper.removeClass('is-switched');
				} else {
					pricing_table_wrapper.addClass('is-switched').eq(0).one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function() {		
						hide_not_selected_items(table_elements, selected_filter);
						pricing_table_wrapper.removeClass('is-switched');
						//change rotation direction if .cd-pricing-list has the .cd-bounce-invert class
						if(pricing_table.find('.cd-pricing-list').hasClass('cd-bounce-invert')) pricing_table_wrapper.toggleClass('reverse-animation');
					});
				}
			});
		});
	}
	function show_selected_items(selected_elements) {
		selected_elements.addClass('is-selected');
	}

	function hide_not_selected_items(table_containers, filter) {
		$.each(table_containers, function(key, value){
	  		if ( key != filter ) {	
				$(this).removeClass('is-visible is-selected').addClass('is-hidden');

			} else {
				$(this).addClass('is-visible').removeClass('is-hidden is-selected');
			}
		});
	}



    /*---------------------------------------------*
     * STICKY scroll
     ---------------------------------------------*/

    $.localScroll();



    /*---------------------------------------------*
     * Counter 
     ---------------------------------------------*/

//    $('.statistic-counter').counterUp({
//        delay: 10,
//        time: 2000
//    });




    /*---------------------------------------------*
     * WOW
     ---------------------------------------------*/

//        var wow = new WOW({
//            mobile: false // trigger animations on mobile devices (default is true)
//        });
//        wow.init();


    /* ---------------------------------------------------------------------
     Carousel
     ---------------------------------------------------------------------= */

//    $('.testimonials').owlCarousel({
//        responsiveClass: true,
//        autoplay: false,
//        items: 1,
//        loop: true,
//        dots: true,
//        autoplayHoverPause: true
//
//    });



// scroll Up

    $(window).scroll(function(){
        if ($(this).scrollTop() > 600) {
            $('.scrollup').fadeIn('slow');
        } else {
            $('.scrollup').fadeOut('slow');
        }
    });
    $('.scrollup').click(function(){
        $("html, body").animate({ scrollTop: 0 }, 1000);
        return false;
    });	


    //End
    
    //ajax register
	$("#registerForm").submit(function(e) {
		$(".loaded").show();
		$(".preloader").show();
		e.preventDefault();
		var data = $("#registerForm").serialize();
		var method = "PUT";
		var url = "/filter/service/v0_001/account";
	 	if(data){
		 	var ajax = new XMLHttpRequest();
		 	ajax.open(method, url, true);
		 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		 	ajax.send(data+"&country="+$("#countryLang").val());
		 	
		 	ajax.onreadystatechange = function() {
		 	  	if (ajax.readyState == 4 && ajax.status == 200) {
		 			$(".loaded").fadeOut();
		 			$(".preloader").delay(1000).fadeOut("slow");		 	  		
		 			var data = ajax.responseText;
		 			var json = JSON.parse(data);
		 			if(json.status == "success"){
		 				$("#registerFormWrapper").html("");
		 				$("#registerFormWrapper").append("<div class=\single_features_right\"><h2>Success</h2></div>" +
		 						"<a onclick=\"overlayOn('overlay');overlayOff('register')\" class=\"btn btn-default\">Please Go to login</a>")
		 			}else if(json.status == "fail"){
		 				if(json.message == "A03"){
		 					$(".emailErr").html("This Email is already in use");
		 					$(".passErr").html("");
		 					$(".userErr").html("");
		 					$('html, body').animate({
		 						scrollTop : $(".emailErr").offset().top
		 					}, 1000, function() {
		 						$("#email-form").focus();
		 					});
		 				}
		 				if(json.message == "A02"){
		 					$(".userErr").html("This Username is already in use");
		 					$(".passErr").html("");
		 					$(".emailErr").html("");
		 					$('html, body').animate({
		 						scrollTop : $(".userErr").offset().top
		 					}, 1000, function() {
		 						$("#user-form").focus();
		 					});
		 				}
		 				if(json.message == "A04"){
		 					$(".passErr").html("Comfirm password is not correct");
		 					$(".emailErr").html("");
		 					$(".userErr").html("");
		 					$('html, body').animate({
		 						scrollTop : $(".passErr").offset().top
		 					}, 1000, function() {
		 						$("#confirm-form").focus();
		 					});
		 				}
		 				if(json.message == "A05"){
		 					$(".passErr").html("Password lenght must have alleast 8 character and contain 1 Uppercase letter, 1 Lowercase Letter,1 Special Letter");
		 					$(".emailErr").html("");
		 					$(".userErr").html("");		 					
		 					$('html, body').animate({
		 						scrollTop : $(".passErr").offset().top
		 					}, 1000, function() {
		 						$("#pass-form").focus();
		 					});
		 				}
		 			}
		 		}
		 	}	
	 	} 	
	});

    //ajax forget
	$("#forget").submit(function(e) {
		e.preventDefault();
		var data = $("#forget").serialize();
		var method = "POST";
		var url = "/filter/forgetPassword";
	 	if(data){
		 	var ajax = new XMLHttpRequest();
		 	ajax.open(method, url, true);
		 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		 	ajax.send(data);

		 	ajax.onreadystatechange = function() {
		 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
		 			var data = ajax.responseText;
		 			var json = JSON.parse(data);

		 			$("#forget-err").html(json.message);

		 		}
		 	}	
	 	} 
	});	
	
	$('#logout').click(function () {
		$("#logoutForm").click()
	});
	
	$("#signIn").submit(function(e) {
		overlayOn('loader');
		e.preventDefault();
		var data = $("#signIn").serialize();
		var method = "POST";
		var url = "/filter/service/signIn";
	 	if(data){
		 	var ajax = new XMLHttpRequest();
		 	ajax.open(method, url, true);
		 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		 	ajax.send(data);

		 	ajax.onreadystatechange = function() {
		 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
		 			var data = ajax.responseText;
		 			var json = JSON.parse(data);
		 			if(json.status == "success"){
		 				location.reload();
		 			}else{
		 				$("#login-err").html(json.message);
		 			}
		 		}
		 	  	overlayOff('loader');
		 	}	
	 	} 	
	});

	$("#regis_now").click(function(e) {
		e.preventDefault();
		$('html, body').animate({
			scrollTop : $("#register_form").offset().top
		}, 1000, function() {
			$("#accName").focus();
		});
	});
	
	$("#productlistprice").click(function(e) {
		e.preventDefault();
		$('html, body').animate({
			scrollTop : $("#price").offset().top
		}, 1000, function() {

		});
	});
	
	$("#contactlist").click(function(e) {
		e.preventDefault();
		$('html, body').animate({
			scrollTop : $("#footer-menu").offset().top
		}, 1000, function() {

		});
	});		
	
	$(".buyscroll").click(function(e) {
		e.preventDefault();
		$('html, body').animate({
			scrollTop : $("#register_form").offset().top
		}, 1000, function() {

		});
	});		
	
});

function overlayOn(id) {
    document.getElementById(id).style.display = "block";
    $("#"+id).css("height:",$(window).height()+"px;");
    if(id != "loader"){
    	$('html, body').animate({
    		scrollTop : $("#"+id).offset().top
    	}, 500, function() {

    	});   	
    }
}

function overlayOff(id) {
    document.getElementById(id).style.display = "none";
}
var muaySprites = ["muay_sleeping.png", "muay_listening.png", "muay_smiling.png", "muay_speaking.png"];
var spriteIndex = 0;

function updateFrame() {
 
 var sprite = muaySprites[spriteIndex];
 $("#home").css("background", "url(assets/images/" + sprite +") no-repeat center center");
 spriteIndex ++;
 if (spriteIndex>muaySprites.length-1) {
  spriteIndex = 0;
 }
 
}


function makeCode (value) {		
	var path = value;		
	qrcode.makeCode(path);
}

function getUserBotList(url,elem) {
	overlayOn("loader");
	return $.ajax({
		url : url,
		cache : false,
		success : function(json) {
			$("#"+elem).empty();
			for (var i = 0; i < json.length; i++) {
				var obj = json[i];

				var flag = checkForValue(roomEnt,obj.key.name);
				
				if(flag){
					$("#"+elem).append("<div class=\"col-sm-12 col-xs-12 listbot\">"
							+"<input id=\""+obj.key.name+"\" checked=\"checked\" class=\"checked\" value=\""+obj.key.name+"\" type=\"checkbox\">"
							+"<label>"+obj.propertyMap.title+"</label>"
							+"</div>"
							);					
				}else{
					$("#"+elem).append("<div class=\"col-sm-12 col-xs-12 listbot\">"
							+"<input class=\"checked\" value=\""+obj.key.name+"\" type=\"checkbox\">"
							+"<label>"+obj.propertyMap.title+"</label>"
							+"</div>"
							);									
				}

			}
			overlayOff("loader");
		}
	});
}

function addBotToRoom(roomId,botPath) {
	var url = "/filter/chatRoomService";
	overlayOn("loader");
	return $.ajax({
		url : url,
		cache : false,
		type: 'PUT',
		data : {roomId: roomId,botPath: botPath},
		success : function(json) {
			
			if(json.message == "err01"){
				alert("Bot in room is at maximum number");
				$("#"+botPath).attr("checked",false);
			}
			overlayOff("loader");
		}
	});
}

function removeBotFromRoom(roomId,botPath) {
	var url = "/filter/chatRoomService";
	overlayOn("loader");
	return $.ajax({
		url : url,
		cache : false,
		type: 'POST',
		data : {roomId: roomId,botPath: botPath},
		success : function(json) {
			overlayOff("loader");
		}
	});
}

function getChatRoom(roomId) {
	var url = "/filter/chatRoomService?roomId="+roomId;
	overlayOn("loader");
	return $.ajax({
		url : url,
		cache : false,
		success : function(json) {
			if(json != null){
				if(json.propertyMap.botPath != null){
					roomEnt = JSON.parse(json.propertyMap.botPath);
				}else{
					roomEnt = null;
				}
			}
			overlayOff("loader");
		}
	});
}

$(document).on('change', '.checked', function() {
    if(this.checked) {
    	addBotToRoom(roomId,$(this).val());
    }else{
    	removeBotFromRoom(roomId,$(this).val());
    }
});

$("#chatRoom").submit(function(e) {
	e.preventDefault();
	var message = $("#chatRoom_textArea").val();
	$("#chatRoom_textArea").val("");
	if(message){
		sendMessageToChatRoom(roomId,message);
	}
});

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

function sendMessageToChatRoom(roomId,message) {
	if(message){
		var url = "/filter/boardcast";
		return $.ajax({
			url : url,
			cache : false,
			type: 'POST',
			data : {roomId: roomId,message: message},
			success : function(json) {
				appendMessageChatRoom(json);
				
				overlayOff("loader");
			}
		});
	}
}

var roomTimestamp = {};

function refleshText(roomId) {
 var url = "/filter/boardcast";
 return $.ajax({
  url : url,
  cache : false,
  type : 'POST',
  data : {
   roomId : roomId
  },
  success : function(json) {
   
  if (json.length==0) return;
   var jsonTimestamp = json[json.length-1].timestamp;
   var lastRoomTimestamp = roomTimestamp.roomId;
   if (!lastRoomTimestamp) lastRoomTimestamp = 0;
   
   if (jsonTimestamp>lastRoomTimestamp) {
    appendMessageChatRoom(json);
    roomTimestamp.roomId = jsonTimestamp;
   }
  }
 });

}


function appendMessageChatRoom(json) {
		$("#eoss_msg_body").empty();
		for (var i = 0; i < json.length; i++) {
			if(json[i].name == email){
				addMessageRight(json[i]);
			}else{
				addMessageLeft(json[i]);
			}
		}
		scrollToLast();
}

function checkForValue(json,value) {
	if(json != null){
	    for (var i = 0; i < json.length; i++) {
	    	var array = json[i].split("/");
	    	if(value == array[1]){
	    		return true;
	    	}
	    }		
	}
    return false;
}

function updateBotList(elem) {
	$.when(getChatRoom(roomId)).done(function(){
		getUserBotList("/filter/showcaseListChatRoom",elem);
	});
}		

function getShowCase(accountId){
	overlayOn('loader');
	scrollflag = true;
	var http = new XMLHttpRequest();
	if(accountId == null){
		
		if(cursor == null){
			var url = "/filter/getShowcaseList";
		}else{
			var url = "/filter/getShowcaseList?cursor=" + cursor;
		}	
		
	}else{
		
		if(cursor == null){
			var url = "/filter/getShowcaseList?accountId="+accountId;
		}else{
			var url = "/filter/getShowcaseList?cursor=" + cursor+"&accountId="+accountId;
		}		
		
	}

	
	http.open("GET", url, true);

	// Send the proper header information along with the request
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

	http.onreadystatechange = function() {// Call a function when the state
											// changes.
		if (http.readyState == 4 && http.status == 200) {
			var data = http.responseText;
			var main = JSON.parse(data);
			var json = JSON.parse(main.status);
			cursor = main.message;
			
			for (var i = 0; i < json.length; i++) {
				var obj = json[i];
				$("#showCase").append("<div class=\"col-md-2 col-sm-3\">"
									+"<a href=\""+'/filter/bot/'+obj.propertyMap.accountId+'/'+obj.key.name+"\">"
										+"<div class=\"img-bg\" style=\"background-image:url('"+'/bin/'+obj.propertyMap.accountId+'/'+obj.key.name+"');background-size:100%;\">"
											+"<div class=\"img-bg\" style=\"background-color:rgba(0, 0, 0, 0.15);display: table;\" >"
												+"<div class=\"description\" style=\"text-align:left\">"
													+"<h3>"+ obj.propertyMap.title +"</h3>"
													+"<p style=\"max-height: 50px;\">"+ obj.propertyMap.description +"</p>"
													+"<p>View: "+ obj.propertyMap.viewCount +"</p>"
												+"</div>"
											+"</div>"																		
										+"</div>"
										+"</a>"
									+"</div>")
			}
			overlayOff('loader');
			scrollflag = false;
		}
	}
	http.send();
}


//Chat Room
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
    var eossToggle = document.getElementsByClassName("eoss_msg_wrap")[0];
    if (eossToggle.style.display === "none") {
    	eossToggle.style.display = "block";
    } else {
    	eossToggle.style.display = "none";
    }
} 

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

function scrollToLast(){
	  var elem = document.getElementById('eoss_msg_body');
	  elem.scrollTop = elem.scrollHeight;    	
}

//get a reference to the element
var eossSendbutton = document.getElementById('eoss-sendMessage');

//add event listener
eossSendbutton.addEventListener('click', function(event) {
    var msg = document.getElementById("eoss-text-area-chat").value;
    document.getElementById("eoss-text-area-chat").value="";
	if(msg!=''){
		sendMessageToChatRoom(roomId,msg);
	}
});


function addMessageLeft(message){
	
	if(message != ""){
	    	var str = message.name;
	        var n = str.search("/");
			var innerBody = document.getElementById("eoss_msg_body");
			var src = "/bin/"+message.name;
			var word;
			if(n == -1){
				src = "/assets/images/guest.png";
				word = "<div class=\"eoss_msg_l\">" +
				"<img src=\""+src+"\">" + 
				message.message + 
				"</div>";				
			}else{
				word = "<div class=\"eoss_msg_l\">" +
				"<a href=\"/filter/bot/"+message.name+"\"><img src=\""+src+"\"></a>" + 
				message.message + 
				"</div>";					
			}
			
			innerBody.innerHTML += word;	
	}

}

function addMessageRight(message){
	if(message != ""){
		var innerBody = document.getElementById("eoss_msg_body");
		var word = "<div class=\"eoss_msg_r\">" + message.message + "</div>";
		innerBody.innerHTML += word;
	}
}
var cookiesBot = document.cookie.indexOf(roomId+'=');
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

if(cookiesBot == -1){
	createCookie(roomId,true);
	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		toggleclass();
	}
	eossToggleShow();
}

var eossTextArea = document.getElementById("eoss-text-area-chat");
function eossTextAreaKeyPress(e){
    var keyCode = e.keyCode;
    if(keyCode == 13){
    	e.preventDefault();
    	var msg = eossTextArea.value;
		if(msg!=''){
			sendMessageToChatRoom(roomId,msg);
			eossTextArea.value = "";
		}
    }
};

eossTextArea.addEventListener("keypress", eossTextAreaKeyPress, false);


function addShowCase(){
	window.location.href = "/dashboard/show-case.jsp";
}