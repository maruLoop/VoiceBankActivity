$.ajax({
    url: "/json/recentActivity",
}).done(function(data){
	var values = {"recentActivities": []};
	values.recentActivities = data;
	template = Handlebars.compile($('#recent-activity-tmpl').html());
    $('#recent-activity').html(template(values));
}).fail(function(e){
    console.log('error!!!');
    console.log(e);
});


$.ajax({
    url: "/json/getNewcomers",
}).done(function(data){
	var values = {"newcomers": []};
	values.newcomers = data;
	template = Handlebars.compile($('#newcomer-tmpl').html());
    $('#newcomer').html(template(values));
}).fail(function(e){
    console.log('error!!!');
    console.log(e);
});