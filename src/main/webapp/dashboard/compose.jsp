<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
	<title>EOSS-Dashboard/Compose</title>
	<%@ include file="fragment/env-css.jspf" %>	
</head>
<body>
<%@ include file="fragment/env-param.jspf" %>	
<div class="wrapper">
	<%@ include file="fragment/sidebar.jspf" %>
    <div class="main-panel">
		<%@ include file="fragment/navbar.jspf" %>
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">                  	
                        <div class="card col-md-12" style="padding: 10px;">
                        	<div id="service-content" class="content" >
								<div class="tab">
								  <button class="tablinks" onclick="openTab(event, 'chatLog');getChatlog();"><fmt:message key="tab.unknown" /> <span id="unknowNo" style="color: red">( 0 )</span></button>
								  <button class="tablinks" onclick="openTab(event, 'Test')"><fmt:message key="tab.train" /></button>
								  <button class="tablinks active" onclick="openTab(event, 'Compose')"><fmt:message key="tab.compose" /></button>								 
								</div>
								
								<!-- Tab content -->
								<div id="Compose" class="tabcontent col-md-12">
									
									<div class="col-md-8">
										
								        <div id="chat_widget_input_container">
								            <form method="post" id="chat_widget_form">
								            	<br>
								            	<select id="head_select">
								            		<option value="type1">FAQ</option>
								            		<option value="type2">RAW</option>
								            	</select>								            	
								            	<div><input type="button" class="btn btn-default btn-block" value="<fmt:message key="btt.addExample" />" id="example"/></div>
								                <textarea class="form-control" id="chat_widget_input" rows="22" cols="50">Loading Please wait..</textarea>
								            </form>
								        </div>
								        									
									</div>
									<div class="col-md-2">
										<input type="button" class="btn btn-default btn-block" value="<fmt:message key="btt.clear" />" id="clearCompose"/>	
										<input type="button" class="btn btn-default btn-block" value="<fmt:message key="btt.import" />" id="importCompose"/>								
										
										<label id="eoss_loadingbar" style="width: 100%;text-align: center;">loading....</label>
									</div>

								</div>
								
								<div id="Test" class="tabcontent col-md-12">
									<div class="col-md-12">
										<div class="col-md-12">
									        <div>
												<div id="testChatBox">
												</div>
									        </div>
									        
									        <textarea class="form-control" id="testChatText"></textarea>	
									        <div class="col-md-2" style="padding: 10px 10px 0 0;">
									        	<button class="btn btn-default btn-block TestChat" id="TestChat"/><i class="pe-7s-paper-plane"></i><fmt:message key="btt.send" /></button>		
									        </div>				
									        <div class="col-md-2" style="padding: 10px 10px 0 0;">
									        	<button class="btn btn-default btn-block TestChat quickrep" name="Cancel"/><i class="pe-7s-back"></i><fmt:message key="btt.cancel" /></button>		
									        </div>										        							        
									        <div class="col-md-2" style="padding: 10px 10px 0 0;">
									        	<button class="btn btn-default btn-block TestChat quickrep" name="Next" /><i class="pe-7s-next"></i><fmt:message key="btt.next" /></button>										        	
									        </div>								        
									        <div class="col-md-2" style="padding: 10px 10px 0 0;">
									        	<button class="btn btn-default btn-block TestChat quickrep" name="No" /><i class="pe-7s-attention"></i><fmt:message key="btt.dislike" /></button>		
									        </div>	
									        <div class="col-md-2" style="padding: 10px 10px 0 0;">
									        	<button class="btn btn-default btn-block TestChat quickrep" name="Great" /><i class="pe-7s-like2"></i><fmt:message key="btt.likeAndPublish" /></button>		
									        </div>										        												        								       
										</div>
																		
									</div>
								</div>
								
								<div id="chatLog" class="tabcontent col-md-12" style="display: none;">
									<div class="col-md-12">
										<div class="col-md-12">
											<div class="row">
												<div class="col-md-2" style="float: right;margin-bottom: 10px;">
													<input type="button" onclick="getChatlog();" class="btn btn-default btn-block" value="<fmt:message key="btt.reflesh" />"/>
												</div>											
											</div>

									        <div>
												<div id="chatLogTextArea">
													
												</div>
									        </div>											
										</div>
									</div>									
								</div>				     						        	
					        </div>						
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="fragment/overlay-addbot.jspf" %>
<%@ include file="fragment/overlay-loader.jspf"%>
</body>
<%@ include file="fragment/env-js.jspf" %>	
<script src="js/compose.js"></script> 
<script type="text/javascript">
	$( document ).ready(function() {
		$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function(){
			overlayPopup('loader');
			selectedUserBot = $('#selectbotlist').val();
			fields = selectedUserBot.split('_|_');
			botId = fields[0];
			head_select = $("#head_select").val();
			$("#importCompose").show();
			$(".TestChat").show();
			getContextLog();
			getChatlog();
			overlayPopup('loader');
		});
	});
</script>	
</html>
