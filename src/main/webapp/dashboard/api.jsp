<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
<title>EOSS-Dashboard/API</title>
<%@ include file="fragment/env-css.jspf"%>
</head>
<body>
	<%@ include file="fragment/env-param.jspf"%>
	<div class="wrapper">
		<%@ include file="fragment/sidebar.jspf"%>
		<div class="main-panel">
			<%@ include file="fragment/navbar.jspf"%>

			<div class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12">
							<div class="card">
								<div class="content">
									<div class="row">
										<div class="col-md-12">
											<h3>Public API</h3>
											<div class="form-group">
												<label> API Path </label> 
												<input type="text" id="apiPath" readonly="readonly" class="form-control apiPath">
											</div>
											<span class="col-md-2" style="display: inline-block;float: right;padding: 0;z-index: 10;">						               
							                	<input type="button" onclick="copyToclipBoard('apiPath')" class="btn btn-default btn-block" value="<fmt:message key="btt.copy" />" id="copy"/>
							            	</span>	
										</div>
										<div class="col-md-12">
											<h4>Post Request</h4>
											<p>
												Every query to the API must include the following POSTed fields:
												<ul>
													<li>message - Send message to chat with Bot</li>
													<li>sessionId - unique parameter use for crate Session</li>
												</ul>												
											</p>
											
											<h4>Return Parameter</h4>
											<p>
												Api will return text/plain / UTF-8 text and will divide to 3 case:
												<ul>
													<li>Success - In case Bot known answer of message that you send Bot will reply with answer message that you config in <a href="/dashboard/compose.jsp">compose</a></li>
													<li>Unknown - In case Bot can't Answer message that you send to API or can't find Answer in context that you compose Bot will reply with Unknown word that is config in <a href="/dashboard/chat-box.jsp">Chat Box</a></li>
													<li>Bot expire - Incase bot expire bot will reply with message <b>This bot is expire</b></li>
												</ul>												
											</p>
											
											<h4>Curl Example</h4>
											<p>
												<div class="form-group">
													<label>Curl</label> 
													<input type="text" id="curl" readonly="readonly" class="form-control curl">
												</div>
												<span class="col-md-2" style="display: inline-block;float: right;padding: 0;z-index: 10;">						               
								                	<input type="button" onclick="copyToclipBoard('curl')" class="btn btn-default btn-block" value="<fmt:message key="btt.copy" />" id="copy"/>
								            	</span>																									
											</p>	
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
	<%@ include file="fragment/overlay-payment.jspf"%>
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
	
</body>
<%@ include file="fragment/env-js.jspf"%>
<script src="js/api-generate.js"></script> 
<script type="text/javascript">
	$(document).ready(function() {
		$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function(){
			generateApiPath($('#selectbotlist').val());
		});
	});
</script>
</html>
