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
<title>WAYOBOT</title>
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

	<section class="contact sections" style="padding: 0px;padding-bottom: 10px;">
		<div style="width: 100%;padding: 0px;" id="showCase">
			<div class="col-md-2 col-sm-3">
					<div class="img-bg" onclick="<% if (session.getAttribute("token") != null) { %>addShowCase();<% }else{ %>overlayOn('register')<% } %>" style="cursor: pointer;">
						<div class="img-bg" style="display: table;border: 1px solid rgba(0, 0, 0, 0.15);" >
							<div class="description" style="font-size: 80px;text-align: center;font-weight: bold;color: rgba(0, 0, 0, 0.15);">
								+
							</div>
						</div>
					</div>
			</div>
		</div>		
	</section>
	<!-- End of Contact Section -->

	<%@ include file="/assets/fragment/footer.jspf"%>

	<div class="scrollup">
		<a href="#"><i class="fa fa-chevron-up"></i></a>
	</div>

	<!-- Chat Room Box -->
	<div class="eoss_msg_box mobile" id="">
		<% if (session.getAttribute("token") != null) { %><button class="plus-bot" onclick="overlayOn('addBotChatRoom');updateBotList('botListInner');">+</button><% } %>
		<div class="eoss_msg_head">Chat Room </div>
		<div class="eoss_msg_wrap" style="display: none;">
			<div class="eoss_msg_body" id="eoss_msg_body">
				<div class="border"></div>
			</div>
			<div class="eoss_msg_footer">
				<textarea id="eoss-text-area-chat" class="eoss_msg_input" rows="4"></textarea>
				<button id="eoss-sendMessage">SEND</button>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		var roomId = "<%=request.getParameter("accountId")%>";
		var username = '<%= userName %>';
		var domain = '<%=domain%>';
		var email = '<%=email%>';
		var cursor = null; 
		var flag = false;
	</script>
		
	<%@ include file="/assets/fragment/overlay.jspf"%>
	<%@ include file="/assets/fragment/javascript.jspf"%>
	<script type="text/javascript">
		$("#displayUn").html(username);
		var roomEnt;
		
		setInterval(function(){ refleshText(roomId); }, 1000);		
	
		
		$( window ).load( function() {
			if(flag == false){
				$.when(getUserBotList(getShowCase("<%=request.getParameter("accountId")%>"))).done(function(){
					setInterval(function(){ flag = false }, 2000);
				});
				
			}
			
			$(window).scroll(function() {
				   if($(window).scrollTop() + $(window).height() > $(document).height() - 10) {
					   if(flag == false){
					   		getShowCase("<%=request.getParameter("accountId")%>");
					   }
				   }
			});
		});
		
	</script>
	<%@ include file="/assets/fragment/fbSDK.jspf"%>
</body>
</html>
