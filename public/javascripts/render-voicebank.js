$.ajax({
    url: "/json/voicebank/"+voicebankId,
}).done(function(data){
	var values = data;
	template = Handlebars.compile($('#voicebank-tmpl').html());
    $('#voicebank').html(template(values));
}).fail(function(e){
    $('#voicebank').html('CANNOT GET VOICEBANK ACTIVITY');
    console.log('error!!!');
    console.log(e);
});