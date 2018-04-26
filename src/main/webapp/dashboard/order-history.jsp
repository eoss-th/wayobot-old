<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
	<title>EOSS-Dashboard/Order History</title>
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
                        <div class="card">                    
							<div id="service-content" class="content">	
									<label>Your new Order will update in 15min if after 15min still not found your order please contact us</label>
									<div class="col-md-12">
										<button style="width: auto;float: right;" class="btn btn-default btn-block" onclick="getOrderLog('/filter/orderHistorySevice');"><fmt:message key="btt.reflesh" /></button>
									</div>
									
									<div class="col-md-12" style="margin-top: 10px;width: 100%;display: inline-block;">
										<button id="last" style="width: auto;float: right;margin-top: 0px;" class="btn btn-default btn-block" onclick="lastPage()"><fmt:message key="btt.last" /></button>
										<button id="next"  style="width: auto;float: right;margin-top: 0px;" class="btn btn-default btn-block" onclick="nextPage()"><fmt:message key="btt.next" /></button>
										<button id="previous"  style="width: auto;float: right;margin-top: 0px;" class="btn btn-default btn-block" onclick="previousPage()"><fmt:message key="btt.prev" /></button>
										<button id="first"  style="width: auto;float: right;margin-top: 0px;" class="btn btn-default btn-block" onclick="firstPage()"><fmt:message key="btt.first" /></button>
									</div>							
		 	                    	<div class="botlist content table-responsive table-full-width" style="width: 100%;display: inline-block;margin-left: 0px;">
			                    		<table class="table table-hover table-striped">
			                    			<thead>
			                    				<tr>
			                    					<th>Order Id</th>
			                    					<th>Invoice No.</th>
			                    					<th>Bot Id</th>
			                    					<th>Price</th>
			                    					<th>Buy Time</th>			                    					
			                    					<th>Status</th>
			                    					<th></th>
			                    				</tr>
			                    			</thead>
		
											<tbody id="orderList">
																				
											</tbody>
			                    		</table>
			                    		<div class="header"></div>
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
<script src="js/orderPagination.js"></script> 
<script type="text/javascript">
$(document).ready(function() {
	$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function(){
		getOrderLog();
	});
});
</script>	
</html>
