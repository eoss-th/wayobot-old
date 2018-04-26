
<!doctype html>
<html lang="en">
<head>
<title>Add Bot</title>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta
	content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0'
	name='viewport' />
<meta name="viewport" content="width=device-width" />
<!-- Bootstrap core CSS     -->
<link href="../css/bootstrap.min.css" rel="stylesheet" />
<!-- Animation library for notifications   -->
<link href="../css/animate.min.css" rel="stylesheet" />
<!--  Light Bootstrap Table core CSS    -->
<link href="../css/light-bootstrap-dashboard.css?v=1.4.0" rel="stylesheet" />
<!--  CSS for Demo Purpose, don't include it in your project     -->
<link href="../css/demo.css" rel="stylesheet" />
<!--     Fonts and icons     -->
<link href="../css/pe-icon-7-stroke.css" rel="stylesheet" />
</head>
<body>


	<div class="wrapper">

		<div class="sidebar" data-color="purple"
			data-image="../assets/img/sidebar-5.jpg">

			<!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


			<div class="sidebar-wrapper">
				<div class="logo">
					<a href="https://shopbot-stg.appspot.com" class="simple-text">
						Add Bot </a>
				</div>
			</div>
		</div>

		<div class="main-panel">

			<div class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-5">
							<div class="card" style="padding: 15px;">
								<h4 style="margin: 0px;">Add new Bot</h4>
								<form id="addBot-form">
									<div>
										<label>Bot Id </label><input type="text" name="botId"
											required="required">
									</div>
									<div>
										<label>Bot Price </label><input type="text" name="price"
											required="required">
									</div>
									<div>
										<label>Bot Info </label>
										<textarea name="info" rows="5" required="required"></textarea>
									</div>
									<div>
										<label>Bot Age</label><input type="text" required="required"
											name="age"></input>
									</div>
									<div class="btn-default-wrapper">
										<button type="button" id="addBot" class="btn-default">Add
											new bot</button>
									</div>

								</form>
							</div>
						</div>
						<div class="col-md-7">
							<div class="card" style="padding: 15px;">
								<h4 style="margin: 0px;">Bot list</h4>
								<div class="botlist">
									<table>
										<thead>
											<tr>
												<th>Bot Id</th>
												<th>Bot Price</th>
												<th>Bot Info</th>
												<th>Bot Age</th>
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

	<div data-botId="trial" class="eoss_msg_box"
		id="eoss-chatbox-container"
		data-greet="Hello word from eoss-th company"></div>
</body>

<script src="../js/assets/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="../js/assets/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
	
	getBotlist();
	
	$("#addBot").click(function() {
		var data = $("#addBot-form").serialize();
	 	var ajax = new XMLHttpRequest();
	 	ajax.open("PUT", "/filter/service/v0_001/botService", true);
	 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	 	ajax.send(data);

	 	ajax.onreadystatechange = function() {
	 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
	 			var data = ajax.responseText;
	 			var json = JSON.parse(data);
				if(json.status =="success"){
					getBotlist();
				}				
	 		}
 		}
	});

	function getBotlist() {
	 	var ajax = new XMLHttpRequest();
	 	ajax.open("GET", "/filter/service/v0_001/botService", true);
	 	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	 	ajax.send();

	 	ajax.onreadystatechange = function() {
	 	  	if (ajax.readyState == 4 && ajax.status == 200) {		 
	 			var data = ajax.responseText;
	 			var json = JSON.parse(data);
	 			console.log(json);
	 			$("#botlist").empty();
	 			for(var i = 0; i < json.length; i++) {
	 			    var obj = json[i];
	 			   $("#botlist").append("<tr>"
	 					   				+"<td>"+ obj.key.name +"</td>"
	 					   				+"<td>"+ obj.propertyMap.price +"</td>"
	 					   				+"<td>"+ obj.propertyMap.info +"</td>"
	 					  				+"<td>"+ obj.propertyMap.age +"</td>"
	 			   						+"</tr>");	 			  
	 			}	
	 		}
 		}
	}	
	</script>

</html>
