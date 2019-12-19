//充值码兑换流量页使用
(function($){
$.confirmf_p = function(params){
		
		if($('#confirmOverlay').length){
			// A confirm is already shown on the page:
			return false;
		}
		
		var buttonHTML = '';
		$.each(params.buttons,function(name,obj){
			
			// Generating the markup for the buttons:
		
			buttonHTML += '<a href="#" class="button '+obj['class']+'">'+name+'<span></span></a>';
			
			if(!obj.action){
				obj.action = function(){};
			}
		});
		
		var markup = [
			'<div id="confirmOverlay">',
			'<div id="confirmBox">',
			'<span style="text-align:center"><h1>',params.title,'</h1></span>',
			'<p>',params.message,'</p>',
			'<div id="confirmButtons">',
			buttonHTML,
			'</div></div></div>'
		].join('');
		
		$(markup).hide().appendTo('body').fadeIn();
		
		var buttons = $('#confirmBox .button'),
			i = 0;

		$.each(params.buttons,function(name,obj){
			buttons.eq(i++).click(function(){				
				if(name=="确认"){
					document.getElementById("form1").action=params.url;
					$('#form1').submit();
				}
				$.confirmf_p.hide();
				return false;
			});
		});
	}

	$.confirmf_p.hide = function(){
		$('#confirmOverlay').fadeOut(function(){
			$(this).remove();
		});
	}	
})(jQuery);	