$(function () {
    $('.selectpicker').selectpicker({
        'selectedText': 'cat'
    });
    
    showVoicebanks(page);    
});

var showVoicebanks = function(page){
	$.ajax({
	    url: "/json/getAllVoicebanks",
	    data: {
	    	page: page,
	    	pageSize: $pageSize.val(),
	    	sortCode: $sortOrder.find(':selected').data('sort'),
	    	orderCode: $sortOrder.find(':selected').data('order')
	    	},
	    beforeSend: function(){
			            $('#voicebanks').html($("img").attr("src","../images/ajax-loader.gif"));
		            }
	}).done(function(data){
		var values = data;
		page = data.pageNow; // Global
		
		var pagesCount = Math.ceil(values.voicebanksCount / $pageSize.val());
		var pagesCountArray = new Array();
		for(var i=0; i<pagesCount; i++){
			pagesCountArray[i] = {page:i};
			if(values.pageNow == i){
				pagesCountArray[i].active = true;
			}
		}
		values.pagesCount = pagesCountArray;
		if(page-1>=0){
			values.prev = { exist: true, page: page-1 };
		}
		if(page+1<pagesCount){
			values.next ={ exist: true, page: page+1 }; 
		}
		
		template = Handlebars.compile($('#all-voicebanks-tmpl').html());
	    $('#voicebanks').html(template(values));
	    history.pushState("","","/voicebanks?page="+page);
	}).fail(function(e){
	    console.log('error!!!');
	    console.log(e);
	}).always(function(){
	    $('.page-button').on('click', function(){
	    	showVoicebanks($(this).data('page'));
	    });
	    
	    $pageSize.on('change', function(){
	    	showVoicebanks(0);
	    });
	    
	    $sortOrder.on('change', function(){
	    	showVoicebanks(0);
	    })
	});
}