(function (wx) {
	var WX = {wx: wx, isSign: false, debug: false};
	
	// 验证当前地址
	function is_weixin(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			return true;
		} else {
			return false;
		}
	}
	
	
	var init = function() {	
		 if(is_weixin()){
			$.ajax({
				 url: 'http://www.sknhome.com/wx/makeSign?url=' + encodeURIComponent(window.location.href.split("#")[0]),
				 dataType: 'json',
				 async: false,
				 success: function (msg) {
					if(WX.debug){
						alert(msg.appId);
						alert(msg.nostr);
						alert(msg.sign);
						alert(msg.timestamp);
						alert(window.location.href);
						alert(window.location.href.split('#')[0]);
					}
					wx.config({
						debug: WX.debug,
						appId: msg.appId,
						timestamp: msg.timestamp,
						nonceStr: msg.nostr,
						signature: msg.sign,
						jsApiList: ["scanQRCode","chooseWXPay"]
					});
					wx.ready(function () {
						WX.isSign = true;
					});
					wx.error(function(res){
						if(WX.debug)
							alert(res.errMsg);
					    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
					});
				}
			});
		}		
	};
	
	WX.scanQRCode = function(opt){
		if(WX.isSign){
			wx.scanQRCode(opt);
		}
	}
	
	WX.chooseWXPay = function(opt){
		if(WX.isSign){
			wx.chooseWXPay(opt);
		}
	}

	init();
	window.WX = WX;
	
})(wx);