//ajax change password

$("#changePassword").submit(function(e) {
			e.preventDefault();
			var data = $("#changePassword").serialize();
			var method = "POST";
			var url = "/filter/changePassword";
			if (data) {
				var ajax = new XMLHttpRequest();
				ajax.open(method, url, true);
				ajax.setRequestHeader("Content-type",
						"application/x-www-form-urlencoded");
				ajax.send(data);

				ajax.onreadystatechange = function() {
					if (ajax.readyState == 4 && ajax.status == 200) {
						var data = ajax.responseText;
						var json = JSON.parse(data);
						console.log(json);
						$("#changepass-err").html(json.message);
					}
				}
			}
});