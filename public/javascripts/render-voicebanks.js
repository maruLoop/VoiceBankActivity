$.ajax({
    url: "/json/getAllVoicebanks",
}).done(function(data){
	var values = {"voicebanks": []};
	values.voicebanks = data;
	template = Handlebars.compile($('#all-voicebanks-tmpl').html());
    $('#voicebanks').html(template(values));
}).fail(function(e){
    console.log('error!!!');
    console.log(e);
});