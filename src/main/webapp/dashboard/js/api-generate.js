
function generateApiPath(data) {
	var fiels = data.split('_|_');
	var botId = fiels[0];
	$("#apiPath").val(domain+"/api_v1/"+accountId+"/"+botId);
	$("#curl").val('curl -d "&message=Hello&sessionId='+ accountId +'" -X POST '+ domain +'/api_v1/'+ accountId +'/'+ botId +' -H "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"');
}

$(document).ready(function() {
	$('#selectbotlist').on('change', function() {
		var data = $(this).val();
		generateApiPath(data);
	});
});