<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<%@ include file="/assets/fragment/i18n.jspf"%>
<html class="no-js" lang="">
<!--<![endif]-->
<head>
<title>Enterprise Open Source Solution</title>
<%@ include file="/assets/fragment/css.jspf"%>
</head>
<body>
	<!--[if lt IE 8]>
            <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
	<div class='preloader'>
		<div class='loaded'>&nbsp;</div>
	</div>
	<%@ include file="/assets/fragment/nav-bar.jspf"%>

	<section class="sections">
		<div class="row">
			<div class="container" id="chatroom">
				<div id="wrapper">
				    <div id="menu">
				        <p class="add">
				        	<% if (session.getAttribute("token") != null) { %>
							<a class="btn btn-default" style="margin: 0;font-weight: bold;" onclick="overlayOn('addBotChatRoom');updateBotList('botListInner');">
								Add Bot
							</a>
							<% } %>
							
				        </p>
				        <div style="clear:both"></div>
				    </div>
				     
				    <div id="chatbox">

				    </div>
				    <form id="chatRoom">
				        <textarea id="chatRoom_textArea" rows="3" cols="" style="width: 100%;border: none;"></textarea>
				        <button type="submit" style="width: 100%;" >Send</button>
				    </form>
				</div>		
			</div>
		</div>		
	</section>
	<!-- End of Contact Section -->
	
	<%@ include file="/assets/fragment/footer.jspf"%>
	<%@ include file="/assets/fragment/fbSDK.jspf"%>
	<div class="scrollup">
		<a href="#"><i class="fa fa-chevron-up"></i></a>
	</div>
	<%@ include file="/assets/fragment/overlay.jspf"%>
	<script type="text/javascript">
		var roomId = "001";
	</script>
	<%@ include file="/assets/fragment/javascript.jspf"%>
	
	<script type="text/javascript">
		var username = '<%= userName %>';
		$("#displayUn").html(username);
		var domain = '<%=domain%>';
		var roomEnt;

		$.when(refleshText(roomId)).done(function(){
			setInterval(function(){ refleshText(roomId); }, 1000);		
		});

	</script>	
	
	<div data-domain="https://shopbot-stg.appspot.com" data-title="Support ChatBot" data-botid="1522661824891" data-accountid="F67E4B90-B0CC-4006-872F-4F4AFE13F634" class="eoss_msg_box" id="eoss-chatbox-container" data-greet="Hello !!"></div>

<script src="https://js.pusher.com/4.1/pusher.min.js"></script><script>(function(d, s, id) { var js, ejs = d.getElementsByTagName(s)[0];if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;js.src = 'https://shopbot-stg.appspot.com/js/eoss-sdk-script.js';ejs.parentNode.insertBefore(js, ejs);}(document, 'script', 'eoss-sdk-script'));</script>
</html>
