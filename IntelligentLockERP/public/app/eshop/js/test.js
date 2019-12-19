var sknapp = (function(){
	return navigator.userAgent.toLowerCase().indexOf('sknhome') !== -1
})();
var wx= (function(){
	return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
})();
	
if(wx){
	$(".ali_pay_box").css({"display":"none"});
}
/* 订单页面总信息 */
var order = new Vue({
	el: '#pp1',
	data: {
		pro:null,/* 页面商品信息 */
		addr:null,/* 页面地址信息 */
		vip:false,/* 用户是否是会员 */
		pro_num:1,/* 购买的数量 */
		bounty_count:0,/* 奖励金数量 */
		bounty_user_count:0,/* 使用奖励金数量 */
		smsCode:"",
		stock:0,/* 库存剩余 */
		smsSta: false,
		use_bounty: false,/* 是否启用奖励金 */
		payType: 1//默认微信
	},
	methods:{
		pay(){
			if(this.smsCode!=""){
				alert("开始调用支付")
				result.pay()
			}else{
				alert("请输入短信验证码")
			}
		},
		close(){
			$("#modal-overlay").css({'visibility': 'hidden'});
		},
		getSms(){
			$.ajax({
				url: '/v1/getEShopPayCode',
				data: '',
				type: 'GET',
				dataType: 'json',
				success : function(data){
					console.log(data)
				},
				error: function(err){
					console.log(err)
				}
			})
			setTime($("#sms_input"))
		},
		getUseBountyPrice(){
			//计算用户可使用最大奖励金数量 并将其返回
			var totalPrice = this.pro_num*(this.vip?this.pro.vipprice:this.pro.saleprice);
			var bountyPrice = this.bounty_count*0.1;
			if(bountyPrice>=totalPrice){//奖励金总额大于商品价格
				this.bounty_user_count = totalPrice*10;//最大使用奖励金数量
				return totalPrice;//最多可抵折扣价
			}else{//没有超出限额
				this.bounty_user_count = this.bounty_count;//最大使用奖励金数量
				return this.bounty_user_count/10;//最多可抵折扣价
			}
		},
		updatePayType(val){
			this.payType = val;
		},
		add(){
			if(this.pro_num<result.stock){
				this.pro_num++
				if(this.vip){
					if(this.use_bounty){
						result.total_price = ((this.pro.vipprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
						result.total_price = (this.pro.vipprice*this.pro_num).toFixed(2);
					}
  				}else{
					if(this.use_bounty){
						result.total_price = ((this.pro.saleprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
						result.total_price = (this.pro.saleprice*this.pro_num).toFixed(2);
					}
  				}
			}
		},
		reduce(){
			if(this.pro_num>1){
				this.pro_num--
				if(this.vip){
					if(this.use_bounty){
						result.total_price = ((this.pro.vipprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
						result.total_price = (this.pro.vipprice*this.pro_num).toFixed(2);
					}
  				}else{
  					if(this.use_bounty){
						result.total_price = ((this.pro.saleprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
						result.total_price = (this.pro.saleprice*this.pro_num).toFixed(2);
					}
  				}
			}
		},
		addrPage(){
			if(this.addr==null){
				window.location.href="/public/app/eshop/EShopAddAddress.html";
				
			}else{
				window.location.href="/public/app/eshop/EShopAddresses.html?pid="+this.pro.idd;
			}
		}
	},
	watch : {
			pro_num : function(count){
				if(count==""||count==0){/* 判断input是否没填写数据 */
	  				$("#count_val").val(this.pro_num=count=1);
	  			}
	  			if(count>result.stock){/* 判断input填写的数据书否大于999 */
	  				$("#count_val").val(this.pro_num=count=result.stock);
	  			}
  			this.pro_num=count
	  			if(this.vip){
	  				if(this.use_bounty){
							result.total_price = ((this.pro.vipprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
							result.total_price = (this.pro.vipprice*this.pro_num).toFixed(2);
					}
  				}else{
  					if(this.use_bounty){
						result.total_price = ((this.pro.saleprice*this.pro_num)-this.bounty_user_count/10).toFixed(2);
					}else{
						result.total_price = (this.pro.saleprice*this.pro_num).toFixed(2);
					}
  				}
			},
			use_bounty : function(use){
				if(use){//使用奖励金
					var totalPrice = this.pro_num*(this.vip?this.pro.vipprice:this.pro.saleprice);
  					var bountyPrice = this.bounty_count*0.1;
  					if(bountyPrice>=totalPrice){//奖励金总额大于商品价格
  						this.bounty_user_count = totalPrice*10;//最大使用奖励金数量
  						$("#pay").css({'visibility': 'hidden'})
  					}
  					
					result.total_price -= (this.bounty_user_count/10).toFixed(2);
				}else{
					$("#pay").css({'visibility': 'visible'})
					if(this.vip){
	  					result.total_price = (this.pro.vipprice*this.pro_num).toFixed(2);
	  				}else{
	  					result.total_price = (this.pro.saleprice*this.pro_num).toFixed(2);
	  				}
				}
			}
		}
})
/* 订单结果信息 */
var result = new Vue({
	el: '#pp2',
	data: {
		total_price:0,/* 总价 */
		stock:0/* 当前库存 */
	},
	methods:{
		showBox(){
			if(order.use_bounty){
				$("#modal-overlay").css({'visibility': 'visible'})
			}else{
				this.pay()
			}
		},
		pay(){
			$("#modal-overlay").css({'visibility': 'hidden'})
			alert("支付被调用")
			
			alert("使用奖励金数量为："+Math.ceil(order.bounty_user_count)+"折合人民币："+(Math.ceil(order.bounty_user_count)/10))
			
			if(order.addr==null){
				alert("请添加地址")
				return;
			}
			var request = GetRequest();
			var totalPrice = order.pro_num*(order.vip?order.pro.vipprice:order.pro.saleprice);
			var bountyPrice = order.bounty_count*0.1;
			if(bountyPrice>=totalPrice&&order.use_bounty){//超出
				$.ajax({
					type : 'get',
					dataType : 'json',
					async : false,
					url : '/v1/buyEShopProduct_Wx_Wap',
					data: 
					{
						timestamp:new Date().getTime(),
						pid:GetRequest()['idd'],
						amount:order.pro_num,
						bvalue:order.use_bounty?(Math.ceil(order.bounty_user_count)):0,
						consigneeId:order.addr['idd'],
						smsCode:order.smsCode
					},
					success : function(rsp) {
						if(rsp){
							if(rsp.code==0){
								window.location.href = "/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid;
							}else{
								alert(rsp.message)
							}
						}
					}
				});
			}else if(is_weixin()){/* 微信浏览器中发起的支付 */
				$.ajax({
					type : 'get',
					dataType : 'json',
					async : false,
					url : '/v1/buyEShopProduct_Wx_Web',
					data: 
					{
						timestamp:new Date().getTime(),
						pid:GetRequest()['idd'],
						amount:order.pro_num,
						bvalue:order.use_bounty?(Math.ceil(order.bounty_user_count)):0,
						consigneeId:order.addr['idd'],
						smsCode:order.smsCode
					},
					success : function(rsp) {
						if(rsp){
						alert(rsp.code)
							if(rsp.code==0){
								function onBridgeReady(){
								   WeixinJSBridge.invoke(
									  'getBrandWCPayRequest', 
									  {
									      "appId":rsp.data.appId,
										  "timeStamp":rsp.data.timeStamp,
										  "nonceStr":rsp.data.nonceStr,
										  "package":rsp.data.package,
										  "signType":rsp.data.signType,
										  "paySign":rsp.data.paySign
									  },
									  function(res){
										  if(res.err_msg == "get_brand_wcpay_request:ok" ){
								  		  	// 使用以上方式判断前端返回,微信团队郑重提示：
										  	//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
										    window.location.href = "/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid;
										  } 
									   }); 
								}
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
							}else if(rsp.code==3){
								window.location.href = "/wx/toOAuth";	
							}else{
								alert(rsp.message)
							}
						}
					}
				});
			}else if(sknapp){/* sknapp内发起的支付 */
				alert('跳转链接为:skn://payforshop?pid='+(GetRequest()['idd'])+'&amount='+(order.pro_num)+'&consigneeid='+(order.addr['idd'])+'&bvalue='+(order.use_bounty?Math.ceil(order.bounty_user_count):0)+'&paychannel='+(order.payType))
				if(order.use_bounty){//判断是否启用奖励金
					window.location.href = 'skn://payforshop?pid='+(GetRequest()['idd'])+'&amount='+(order.pro_num)+'&consigneeid='+(order.addr['idd'])+'&bvalue='+(order.use_bounty?(Math.ceil(order.bounty_user_count)):0)+'&paychannel='+(order.payType)+"&smscode="+order.smsCode;
				}else{
					window.location.href = 'skn://payforshop?pid='+(GetRequest()['idd'])+'&amount='+(order.pro_num)+'&consigneeid='+(order.addr['idd'])+'&bvalue='+(order.use_bounty?(Math.ceil(order.bounty_user_count)):0)+'&paychannel='+(order.payType);
				}
			}else{/* 非微信网页支付 */
				if(order.payType==1){//微信支付
					$.ajax({
						type : 'get',
						dataType : 'json',
						async : false,
						url : '/v1/buyEShopProduct_Wx_Wap',
						data: 
						{
							timestamp:new Date().getTime(),
  							pid:GetRequest()['idd'],
  							amount:order.pro_num,
  							bvalue:order.use_bounty?(Math.ceil(order.bounty_user_count)):0,
  							consigneeId:order.addr['idd'],
  							smsCode:order.smsCode
						},
						success : function(rsp) {
							if(rsp){
								if(rsp.code==0){
									/* var suc = rsp.data.mweb_url+"&redirect_url="+encodeURIComponent("http://www.sknhome.com/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid+"orderStatus=0");
									var fai = rsp.data.mweb_url+"&redirect_url="+encodeURIComponent("http://www.sknhome.com/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid+"orderStatus=1");
									doLoad(suc,fai,rsp.data.orderid) */
  									if(rsp.data.mweb_url){
  										window.location.href = rsp.data.mweb_url+"&redirect_url="+encodeURIComponent("http://www.sknhome.com/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid);
  									}else{
  										window.location.href = "/public/app/eshop/EShopPayResult.html?orderid="+rsp.data.orderid;
  									}
								}else{
  									alert(rsp.message)
  								}
							}
						}
					});
				}else if(order.payType==2){//支付宝支付
					/* window.location.href = '/v1/buyEShopProduct_Ali_Wap?pid='+(GetRequest()['idd'])+'&amount='+(order.pro_num)+'&consigneeId='+(order.addr['idd'])+'&bvalue='+(order.use_bounty?order.bounty_count:0)+'&paychannel='+(order.payType); */
					$.ajax({
						type : 'get',
						dataType : 'json',
						async : false,
						url : '/v1/buyEShopProduct_Ali_Wap',
						data: 
						{
							timestamp:new Date().getTime(),
  							pid:GetRequest()['idd'],
  							amount:order.pro_num,
  							bvalue:order.use_bounty?(Math.ceil(order.bounty_user_count)):0,
  							consigneeId:order.addr['idd'],
  							smsCode:order.smsCode
						},
						success : function(rsp) {
							if(rsp){
								if(rsp.code==0){
									document.write(rsp.data.form);
								}else{
									alert(rsp.message)
								}
							}
						}
					});

				}

			}
			
		}
	}
})
window.onload=function(){
	var idd = GetRequest()['idd'];
	/* 初始化页面信息 */
	$.ajax({
		url: '/v1/findProInfo',
		data: {idd:idd},
		type: 'POST',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			order.pro = data;
			result.stock=order.stock=data.stock;
			
		},
		error: function(err){
			console.log(err)
		}
	})
	/* 判断用户是否是会员 */
	$.ajax({
		url: '/v1/checkVip',
		data: {},
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			if(data.data==1){
				order.vip=true
				result.total_price = ((order.pro.vipprice)*order.pro_num).toFixed(2);
			}else{
				result.total_price = ((order.pro.saleprice)*order.pro_num).toFixed(2);
			}
		},
		error: function(err){
			console.log(err)
		}
	})
	/* 获取用户奖励金数量 */
	$.ajax({
		url: '/v1/getBountyCount',
		data: {},
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			order.bounty_count = data.data
		},
		error: function(err){
			console.log(err)
		}
	})
	
	/* 获取用户地址信息 */
	$.ajax({
		url: '/v1/findAddr',
		data: {},
		type: 'POST',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			var arr = data.data;
			if(data.code==202){
				window.location.href="/public/app/eshop/EShopLogin.html";
			}
			if(arr.length==0){
				console.log("用户需要先添加地址")
			}else{
				var flag = true;
				arr.forEach((item,index,arr)=> {
				    var addr = arr[index];
				    if(typeof(GetRequest()['addrid']) == 'undefined'){/* 判断是都是地址选择携带的地址 */
					    if(addr['isdefault']==1){
					    	order.addr = addr;
							flag=false;
					    }
				    }else{
				    	if(addr['idd']==GetRequest()['addrid']){
					    	order.addr = addr;
							flag=false;
					    }
				    }
				})
				if(flag){/* 说明用户没有默认地址  则获取第一位*/
					order.addr = arr[0];
				}
			}
		},
		error: function(err){
			console.log(err)
		}
	})
}

//判断是否是微信浏览器
function is_weixin(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger"){
		return true;
	}else{
		return false;
	}
}
//60s倒计时实现逻辑
var countdown = 60;
function setTime(obj) {
    if (countdown == 0) {
        obj.prop('disabled', false);
        obj.text("点击获取验证码");
        countdown = 60;//60秒过后button上的文字初始化,计时器初始化;
        return;
    } else {
        obj.prop('disabled', true);
        obj.text("("+countdown+"s)后重新发送") ;
        countdown--;
    }
    setTimeout(function() { setTime(obj) },1000) //每1000毫秒执行一次
}
	/* 获取连接中携带的参数 */
function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
  		var str = url.substr(1);
  		strs = str.split("&");
  		for(var i = 0; i < strs.length; i ++) {
  			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
  		}
	}
	return theRequest;
}