(function (wx) {
	var WX = {wx: wx, isSign: false, debug: false};
	var bindShare = function bindShare(opts) {
		if (!opts) {
			return;
		}
		wx.onMenuShareAppMessage(opts);
		wx.onMenuShareTimeline(opts);
		wx.onMenuShareQQ(opts);
		wx.onMenuShareWeibo(opts);
	};
	
	// 验证当前地址
	function is_weixin(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			return true;
		} else {
			return false;
		}
	}
	
	
	var getSign = function getSign(cb) {
		
		
		 if(is_weixin()){
			$.ajax({
				 url: 'http://bank.wo.cn/WEIXIN/MakeSign?url=' + encodeURIComponent(window.location.href.split("#")[0]),
				 dataType:'json',
				 success: function (msg) {
					if(WX.debug){
						alert(msg.appId);
						alert(msg.nostr);
						alert(msg.sign);
						alert(window.location.href);
						alert(window.location.href.split('#')[0]);
					}
					wx.config({
						debug: WX.debug,
						appId: msg.appId,
						timestamp: msg.timstr,
						nonceStr: msg.nostr,
						signature: msg.sign,
						jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo"]
					});
					wx.ready(function () {
						WX.isSign = true;
						cb();
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
	WX.ready = function (opts) {
		if (WX.isSign) {
			bindShare(opts);
			return;
		}
		getSign(function () {
			bindShare(opts);
		});
	};
	
	
	//***************禁止分享****************/
	function onBridgeReady(){
		  WeixinJSBridge.call('hideOptionMenu');
	}
	
	WX.noShare = function(){
		if (typeof WeixinJSBridge == "undefined"){
			if( document.addEventListener ){
			 	document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){
			  document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
			  document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		}else{
			  onBridgeReady();
		}
	}
	
	window.WX = WX;
})(wx);