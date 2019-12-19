function toptitle(){
	$("#return_left_img").click(function(){//返回上一个页面的方法
		//window.location.href=document.referrer;
		window.history.go(-1)
	})
}