<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
<title>EOSS-Dashboard/Show Case</title>
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
								<div class="row" style="padding: 20px;margin: 0">
									<div class="col-md-5">
										<label id="errImg" style="color: red;width: 100%;text-transform: none;">										
										</label>
										<form id="fileForm">
											<label style="width: 200px;">Cover Image :</label>
											<input type='file' id='fileinput' accept="image/*" name ="image" style="display: inline-block;">								
										</form>
										<label style="width: 200px;">Size : </label><label style="width: 100px;text-transform: none;" id ="imgSize">0 Mb</label><label></label>
										<br>
										<label style="width: 200px;">Width Height : </label><label style="width: 100px;" id="imgWH">W:0 H:0</label>
										<br>									
										<form id="showCaseDetail">										
											<div class="col-md-12" style="padding: 0;">
													<div class="form-group">
														<label>title</label> 
														<input type="text" id="title" class="form-control" placeholder="Title" value="" required="required">
													</div>
											</div>
											<div class="col-md-12" style="padding: 0;">
													<div class="form-group">
														<label>Description</label> 
														<textarea id="description" rows="4" cols=""  placeholder="Description" style="width: 100%;" required="required"></textarea>
													</div>
											</div>																													
											<br>
											<label style="width: 200px;line-height: 50px;">Publish:</label>
											<label class="switch" style="line-height: 50px;vertical-align: middle;">							
											  <input type="checkbox" id="onOff">							
											  <span class="slider round" id="sliderSwitch">
											  </span>
											</label>
										</form>									
									</div>
									<div class="col-md-5" style="text-align: center;">
										<div style="width: auto;display: inline-block;margin: auto;">
											<div style="border: 1px solid;min-width: 200px;min-height: 200px;max-width: 100%;">
												<img id="showcaseImage" style="width: 100%;" onerror="this.onerror=null;this.src='';">
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
	<%@ include file="fragment/overlay-payment.jspf"%>
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
</body>
<%@ include file="fragment/env-js.jspf"%>
<script src="js/showcase.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$.when(getUserBotList("/service/v0_001/userBotService","#selectbotlist")).done(function() {
			selectedUserBot = $('#selectbotlist').val();
			fields = selectedUserBot.split('_|_');
			botId = fields[0];
			getShowCase();
			$('#fileinput').change(function(){
			    var input, file;

			    if (!window.FileReader) {
			        alert("The file API isn't supported on this browser yet.");
			        return;
			    }
			    
			    input = document.getElementById('fileinput');
			    if (!input) {
			        bodyAppend("p", "Um, couldn't find the fileinput element.");
			    }
			    else if (!input.files) {
			        bodyAppend("p", "This browser doesn't seem to support the `files` property of file inputs.");
			    }
			    else if (!input.files[0]) {
			        bodyAppend("p", "Please select a file before clicking 'Load'");
			    }
			    else {
			        file = input.files[0]; 
			        var sizeInMb = file.size / (1024 * 1024);
			        var reader = new FileReader();
			        
			        reader.onload = function (e) {
			            var img = new Image;
			            img.onload = function() {
			            	showImgDetail(sizeInMb,img.width,img.height);
			            	if(img.width == img.height && img.width >= 512){	
			            		overlayPopup('loader');
			            		uploadImg();			     
			            		$("#errImg").hide();
			            	}else{
			            		$("#errImg").text("Image size minimal is 512*512 pixel and must be square shape");
			            		$("#errImg").show();
			            	}
			            	$("#showcaseImage").attr("src",e.target.result);
			            };
			            img.src = reader.result;
			         };
			         reader.readAsDataURL(input.files[0]);
			         
			    }
			});	
	});
});
</script>
</html>
