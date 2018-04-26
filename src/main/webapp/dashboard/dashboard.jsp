<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
<title>EOSS-Dashboard/DashBoard</title>
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
								<div class="content showcase-btt"
									style="width: 100%; display: inline-block;">
									<a href="compose.jsp" class="col-md-2"
										style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i
										class="pe-7s-pen"></i><br><fmt:message key="sidbar.compose" />
										<p style="color: red;font-size: 14px;">
											Unknown
											<span id="unknowNo"> ( 0 )</span>
										</p>
										</button>
									</a> <a href="show-case.jsp" class="col-md-2"
										style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i
										class="pe-7s-albums"></i><br><fmt:message key="sidbar.showcase" /><br>
											<label class="switch" style="line-height: 50px;vertical-align: middle;z-index: -1;margin-top: 5px;margin-bottom: 0px;">							
											  <input type="checkbox" id="onOff" disabled="disabled">							
											  <span class="slider round" id="sliderSwitch">
											  </span>
											</label>
										</button>
									</a> <a href="push-service.jsp" class="col-md-2"
										style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i
										class="pe-7s-bell"></i><br><fmt:message key="sidbar.pushservice" />
										<p style="color: green;font-size: 14px;">
											Online ( <span id="userCount">0</span> )
										</p>
										</button>
									</a>
									<a href="chat-box.jsp" class="col-md-2"
										style="padding: 10px 10px 0 0;">
										<button class="btn btn-default btn-block" /> <i
										class="pe-7s-news-paper"></i><br><fmt:message key="sidbar.chatbotscript" />
										<p style="font-size: 20px;font-weight: bold;">
											&lt;/&gt;
										</p>
										</button>
									</a>									
								</div>

								<div class="header">
									<h4 class="title">Push Service Ussage</h4>
									<p class="category remainQuota"></p>
								</div>

								<div class="botlist content table-responsive table-full-width"
									style="width: 100%; display: inline-block; margin-left: 0px;">
									<table class="table table-hover table-striped">
										<thead>
											<tr>
												<th class="mobile">Bot Id</th>
												<th>Bot Name</th>
												<th class="mobile">LANGUAGE</th>
												<th class="mobile">Buy Date</th>
												<th class="mobile">Expire Date</th>
												<th></th>
											</tr>
										</thead>

										<tbody id="botlist">

										</tbody>
									</table>
								</div>

							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	<textarea rows="4" cols="4" id="testChatText" style="display: none;"></textarea>
	<textarea rows="4" cols="4" id="chatLogTextArea" style="display: none;"></textarea>
	<input type="checkbox" id="onOff" style="display: none;">

	<%@ include file="fragment/overlay-payment.jspf"%>
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
</body>
<%@ include file="fragment/env-js.jspf"%>
<script src="js/compose.js"></script>
<script src="js/notification.js"></script>
<script src="js/showcase.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function(){
			selectedUserBot = $('#selectbotlist').val();
			fields = selectedUserBot.split('_|_');
			botId = fields[0];
			appendBotlistTable(userBotList);
			getPushCount("/filter/service/v0_001/pusherCountService");
			getChatlog();
			getShowCase();
		});
	});
</script>
</html>
