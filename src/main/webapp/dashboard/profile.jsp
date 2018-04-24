<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="fragment/i18n.jspf"%>
<!doctype html>
<html lang="en">
<head>
<title>EOSS-Dashboard/Profile</title>
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
								<div class="header">
									<h4 class="title">Profile</h4>
								</div>
								<div class="content">
									<form>
										<div class="row">
											<div class="col-md-3">
												<div class="form-group">
													<label>name</label> <input type="text"
														class="form-control" placeholder="Name"
														readonly="readonly" value="<%=userName%>">
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">
													<label>Email</label> <input type="text"
														class="form-control" placeholder="Name"
														readonly="readonly" value="<%=userEmail%>">
												</div>
											</div>
										</div>
										<button type="submit" class="btn btn-info btn-fill pull-right" style="display: none;">Update
											Profile</button>
										<div class="clearfix"></div>
									</form>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="card">
								<div class="header">
									<h4 class="title">Change Password</h4>
									<div class="content">
										<form id="changePassword">
											<h6 id="changepass-err"></h6>
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">
														<label for="">Old Password</label> <input name="oldPassword" type="password"
															class="form-control" placeholder="Old Password" required="required">
													</div>
													<div class="form-group">
														<label for="">New Password</label> <input name="newPassword" type="password"
															class="form-control" placeholder="New Password" required="required">
													</div>
													<div class="form-group">
														<label for="">Confirm Password</label> <input
															name="confirmPassword" type="password" class="form-control"
															placeholder="Confirm Password" required="required">
													</div>
												</div>
											</div>
											<button type="submit" class="btn btn-info btn-fill pull-right">
												<fmt:message key="btt.changePassword" />
											</button>
											<div class="clearfix"></div>
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
	<%@ include file="fragment/overlay-payment.jspf"%>
	<%@ include file="fragment/overlay-addbot.jspf"%>
	<%@ include file="fragment/overlay-loader.jspf"%>
</body>
<%@ include file="fragment/env-js.jspf"%>
<script src="js/profile.js"></script> 
<script type="text/javascript">
	$(document).ready(
			function() {
				$.when(
						getUserBotList("/service/v0_001/userBotService",
								"#selectbotlist")).done(function() {
					appendBotlistTable(userBotList);
				});
			});
</script>
</html>
