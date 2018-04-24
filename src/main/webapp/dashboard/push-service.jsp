<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
	<title>EOSS-Dashboard/Push service</title>
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
                        <div class="card col-md-8" style="padding: 10px;">
                        	<div id="service-content" class="content" >
								<label class="remainQuota">
	
								</label><br>
								<div>
									<label>Bot Name:</label>
									<label id="botName" style="text-transform: none;"></label>
									<br>
									<label>Online User:</label>
									<label id="userCount"></label>
								</div>
						        <div id="chat_widget_input_container">
						            <form method="post" id="chat_widget_form">
						                <textarea class="form-control" id="chat_widget_input" rows="4" cols="50"></textarea>
						                <br>
						                <div class="col-md-2" style="padding: 0;float: none;">						               
						                	<input type="button" class="btn btn-default btn-block" value="<fmt:message key="btt.notification" />" id="chat_widget_button"/>
						                </div>
						            </form>
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
<script src="js/notification.js"></script> 

</html>
