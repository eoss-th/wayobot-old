$( document ).ready(function() {
	var lang = navigator.language;
	var jsondata;
	
	if(lang != "th"){
		  jsondata = '{"title":"Build your own Bots"}';  
		 }else{
		  jsondata = '{"title":"สร้างบอทของท่าน"}';  
		 }


	
	
	var jsonLang = JSON.parse(jsondata);
	var title_content = document.getElementById("title-content");

	title_content.innerHTML = jsonLang.title;

});