var numberPerPage = 5;

$('#selectbotlist').on('change', function() {
	getOrderLog();
});
// get order log
function getOrderLog() {
	var ajax = new XMLHttpRequest();
	var fields = $("#selectbotlist").val();
	var botId = fields.split('_|_');
	ajax.open("GET", "/orderHistorySevice?botId=" + botId[0], true);
	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	ajax.send();

	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4 && ajax.status == 200) {
			var data = ajax.responseText;
			$("#orderList").empty();
			var json = JSON.parse(data);
			for (var i = 0; i < json.length; i++) {
				var obj = json[i];
				var status;
				if (obj.propertyMap.status == "0") {
					status = "Waiting for buyer funds";
				}
				if (obj.propertyMap.status == "1") {
					status = "Waiting for confirming";
				}
				if (obj.propertyMap.status == "100") {
					status = "Complete";
				}
				if (i < numberPerPage - 1) {
					$("#orderList").append(
							"<tr class=\"events-section__main-event\">"
									+ "<td>" + obj.key.name + "</td>" + "<td>"
									+ obj.propertyMap.invoice + "</td>"
									+ "<td>" + obj.propertyMap.botId + "</td>"
									+ "<td>" + obj.propertyMap.Price + "</td>"
									+ "<td>" + obj.propertyMap.buyTime
									+ "</td>" + "<td>" + status + "</td>"
									+ "</tr>");
				} else {
					$("#orderList").append(
							"<tr class=\"events-section__main-event not-visible\">"
									+ "<td>" + obj.key.name + "</td>" + "<td>"
									+ obj.propertyMap.invoice + "</td>"
									+ "<td>" + obj.propertyMap.botId + "</td>"
									+ "<td>" + obj.propertyMap.Price + "</td>"
									+ "<td>" + obj.propertyMap.buyTime
									+ "</td>" + "<td>" + status + "</td>"
									+ "</tr>");
				}

			}
			events = Array.prototype.slice.call(document
					.querySelectorAll(".events-section__main-event"));
			numberOfPages = getNumberOfPages();
			load();
		}
	}

}
var pageList = new Array();
var currentPage = 1;

var events = Array.prototype.slice.call(document
		.querySelectorAll(".events-section__main-event"));

function getNumberOfPages() {
	return Math.ceil(events.length / numberPerPage);
}

function nextPage() {
	currentPage += 1;
	loadList();
}

function previousPage() {
	currentPage -= 1;
	loadList();
}

function firstPage() {
	currentPage = 1;
	loadList();
}

function lastPage() {
	currentPage = numberOfPages;
	loadList();
}

function loadList() {
	var begin = ((currentPage - 1) * numberPerPage);
	var end = begin + numberPerPage;
	for (i = 0; i < pageList.length; i++) {
		pageList[i].classList.add("not-visible"); // make the old list invisible
	}
	pageList = events.slice(begin, end);
	drawList();
	check();
}

function drawList() {
	for (i = 0; i < pageList.length; i++) {
		pageList[i].classList.remove("not-visible");
	}
}

function check() {
	document.getElementById("next").disabled = currentPage == numberOfPages ? true
			: false;
	document.getElementById("previous").disabled = currentPage == 1 ? true
			: false;
	document.getElementById("first").disabled = currentPage == 1 ? true : false;
	document.getElementById("last").disabled = currentPage == numberOfPages ? true
			: false;
}

function load() {
	loadList();
}

var numberOfPages = getNumberOfPages();
