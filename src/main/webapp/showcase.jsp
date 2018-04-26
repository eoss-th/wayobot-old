<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<% 

	String bot_title = (String) request.getAttribute("bot_title");
	String bot_greeting = (String) request.getAttribute("bot_greeting");
	String bot_botId = (String) request.getAttribute("bot_botId");
	String bot_accountId = (String) request.getAttribute("bot_accountId");
	String bot_viewCount = (String) request.getAttribute("bot_viewCount");
	String showcase_title = (String) request.getAttribute("showcase_title");
	String showcase_description = (String) request.getAttribute("showcase_description"); 
	String showcase_owner = (String) request.getAttribute("showcase_owner"); 
	String path = bot_accountId+"/"+bot_botId;
	if(bot_botId == null){
		response.sendRedirect("/index.jsp");
	}	
%>

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

	<section class="contact sections" style="padding-top: 20px;">
		<div class="container">
			<div class="row">
				<div class="col-md-12" style="text-align: center;padding:0;">	
					<h1><%= showcase_title %></h1><br>			
					<img alt="" src="<%=domain%>/bin/<%= bot_accountId %>/<%= bot_botId %>">	
					<p style="margin-top: 15px;"><%= showcase_description %></p>	
					<a href="/showncaseList?accountId=<%= bot_accountId %>">
						<p><%= showcase_owner %></p>
					</a>
					<p style="margin-top: 10px">View: <%= bot_viewCount %></p>	
					<div id="qrcode" style="width:100%; height:100%;">
					</div>	
				</div>			
			</div>
		</div>
	</section>
	
	<%@ include file="/assets/fragment/footer.jspf"%>
	<div class="scrollup">
		<a href="#"><i class="fa fa-chevron-up"></i></a>
	</div>
	
	<% 
		if (session.getAttribute("token") != null) {
			if(bot_accountId.equals(session.getAttribute("accountId")) ){
				session.setAttribute("botListSelected", bot_botId);
			}
		}	
	%>
	
	<%@ include file="/assets/fragment/overlay.jspf"%>
	<%@ include file="/assets/fragment/javascript.jspf"%>
	<script type="text/javascript">
		var username = '<%= userName %>';
		$("#displayUn").html(username);
		
		var domain = '<%=domain%>';
		var qrcode = new QRCode(document.getElementById("qrcode"), {
			width : 250,
			height : 250
		});
		makeCode('<%= path %>');
	</script>
	<%@ include file="/assets/fragment/fbSDK.jspf"%>
	<!-- Bot Box -->
	<div data-domain="<%=domain%>" data-title="<%= bot_title %>" data-botid="<%= bot_botId %>" data-accountid="<%= bot_accountId %>" class="eoss_msg_box" id="eoss-chatbox-container" data-greet="<%= bot_greeting %>"></div>
	<script src="https://js.pusher.com/4.1/pusher.min.js"></script><script>(function(d, s, id) { var js, ejs = d.getElementsByTagName(s)[0];if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;js.src = '<%=domain%>/js/eoss-sdk-script.js';ejs.parentNode.insertBefore(js, ejs);}(document, 'script', 'eoss-sdk-script'));</script>
</html>
