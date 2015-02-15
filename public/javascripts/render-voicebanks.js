$(function () {
    $('.selectpicker').selectpicker({
        'selectedText': 'cat'
    });
    
    $(window).on('popstate', function(jqevent) {
      var state = jqevent.originalEvent.state; // stateオブジェクト
      page = state;
      showVoicebanksWithoutPushState(state);
    });
    
    history.replaceState(0, null);
//    history.replaceState(0);
    showVoicebanks(page);
});

var renderVoicebanks = function(values){
	var pagesCount = Math.ceil(values.voicebanksCount / $pageSize.val());
	var pagesCountArray = new Array();
	for(var i=0; i<pagesCount; i++){
		pagesCountArray[i] = {page:i};
		if(values.pageNow == i){
			pagesCountArray[i].active = true;
		}
	}
	values.pagesCount = pagesCountArray;
	if(values.pageNow-1>=0){
		values.prev = { exist: true, page: values.pageNow-1 };
	}
	if(values.pageNow+1<pagesCount){
		values.next ={ exist: true, page: values.pageNow+1 }; 
	}
	
	template = Handlebars.compile($('#all-voicebanks-tmpl').html());
    $('#voicebanks').html(template(values));
}

var showVoicebanksWithoutPushState = function(pageNum){
	$.ajax({
	    url: "/json/getAllVoicebanks",
	    data: {
	    	page: pageNum,
	    	pageSize: $pageSize.val(),
	    	sortCode: $sortOrder.find(':selected').data('sort'),
	    	orderCode: $sortOrder.find(':selected').data('order')
	    	},
	    beforeSend: function(){
			            $('#voicebanks').html($("img").attr("src","../images/ajax-loader.gif"));
		            }
	}).done(function(data){
		page = data.pageNow; // Global
		renderVoicebanks(data);
//		history.pushState(data.pageNow, null,"/voicebanks?page="+data.pageNow);
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
	    });
	});
	
}

var showVoicebanks = function(pageNum){
	$.ajax({
	    url: "/json/getAllVoicebanks",
	    data: {
	    	page: pageNum,
	    	pageSize: $pageSize.val(),
	    	sortCode: $sortOrder.find(':selected').data('sort'),
	    	orderCode: $sortOrder.find(':selected').data('order')
	    	},
	    beforeSend: function(){
			            $('#voicebanks').html($("img").attr("src","../images/ajax-loader.gif"));
		            }
	}).done(function(data){
		renderVoicebanks(data);
		if(page != data.pageNow){
			history.pushState(data.pageNow, null,"/voicebanks?page="+data.pageNow);
		}
		page = data.pageNow; // Global
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
	    });
	});
}