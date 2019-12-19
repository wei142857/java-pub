
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ServiceDao.WxGzhService;

import com.avaje.ebean.Ebean;

import models.SubInstallOrders;
import models.SubMsg;
import models.SubOrders;
import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import util.AjaxHellper;
import util.ExcelGenerateHelper;
import util.ExcelUtils;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import views.html.page.SubInstallOrdersList;

@Security.Authenticated(Secured.class)
public class SubInstallOrdersAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SubInstallOrdersList.render() );
	}
	
	/**************************************************************	
	 *                增删改查的功能   ： AJAX方式
	 */	
	 private static SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd");
	//列表
	public static Result list(){
		//获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find SubInstallOrders where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String oid = StringUtil.getHttpParam(request(), "oid");
		if(oid==null)
			oid = "";	
		if ( !( oid .equals("") ) && !( oid .equals("undefined") ) ) 
			sql.append(" and ( oid= '" + oid + "'  )");
		
		String cid = StringUtil.getHttpParam(request(), "cid");
		if(cid==null)
			cid = "";	
		if ( !( cid .equals("") ) && !( cid .equals("undefined") ) ) 
			sql.append(" and ( cid= '" + cid + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String doorthickness = StringUtil.getHttpParam(request(), "doorthickness");
		if(doorthickness==null)
			doorthickness = "";	
		if ( !( doorthickness .equals("") ) && !( doorthickness .equals("undefined") ) ) 
			sql.append(" and ( doorthickness= '" + doorthickness + "'  )");
		
		String slicewidth = StringUtil.getHttpParam(request(), "slicewidth");
		if(slicewidth==null)
			slicewidth = "";	
		if ( !( slicewidth .equals("") ) && !( slicewidth .equals("undefined") ) ) 
			sql.append(" and ( slicewidth= '" + slicewidth + "'  )");
		
		String sliceheight = StringUtil.getHttpParam(request(), "sliceheight");
		if(sliceheight==null)
			sliceheight = "";	
		if ( !( sliceheight .equals("") ) && !( sliceheight .equals("undefined") ) ) 
			sql.append(" and ( sliceheight= '" + sliceheight + "'  )");
		
		String lockimg1 = StringUtil.getHttpParam(request(), "lockimg1");
		if(lockimg1==null)
			lockimg1 = "";	
		if ( !( lockimg1 .equals("") ) && !( lockimg1 .equals("undefined") ) ) 
			sql.append(" and ( lockimg1= '" + lockimg1 + "'  )");
		
		String lockimg2 = StringUtil.getHttpParam(request(), "lockimg2");
		if(lockimg2==null)
			lockimg2 = "";	
		if ( !( lockimg2 .equals("") ) && !( lockimg2 .equals("undefined") ) ) 
			sql.append(" and ( lockimg2= '" + lockimg2 + "'  )");
		
		String hook = StringUtil.getHttpParam(request(), "hook");
		if(hook==null)
			hook = "";	
		if ( !( hook .equals("") ) && !( hook .equals("undefined") ) ) 
			sql.append(" and ( hook= '" + hook + "'  )");
		
		String lockdirection = StringUtil.getHttpParam(request(), "lockdirection");
		if(lockdirection==null)
			lockdirection = "";	
		if ( !( lockdirection .equals("") ) && !( lockdirection .equals("undefined") ) ) 
			sql.append(" and ( lockdirection= '" + lockdirection + "'  )");
		
		String consigneename = StringUtil.getHttpParam(request(), "consigneename");
		if(consigneename==null)
			consigneename = "";	
		if ( !( consigneename .equals("") ) && !( consigneename .equals("undefined") ) ) 
			sql.append(" and ( consigneename= '" + consigneename + "'  )");
		
		String consigneearea = StringUtil.getHttpParam(request(), "consigneearea");
		if(consigneearea==null)
			consigneearea = "";	
		if ( !( consigneearea .equals("") ) && !( consigneearea .equals("undefined") ) ) 
			sql.append(" and ( consigneearea= '" + consigneearea + "'  )");
		
		String consigneeaddress = StringUtil.getHttpParam(request(), "consigneeaddress");
		if(consigneeaddress==null)
			consigneeaddress = "";	
		if ( !( consigneeaddress .equals("") ) && !( consigneeaddress .equals("undefined") ) ) 
			sql.append(" and ( consigneeaddress= '" + consigneeaddress + "'  )");
		
		String consigneephone = StringUtil.getHttpParam(request(), "consigneephone");
		if(consigneephone==null)
			consigneephone = "";	
		if ( !( consigneephone .equals("") ) && !( consigneephone .equals("undefined") ) ) 
			sql.append(" and ( consigneephone= '" + consigneephone + "'  )");
		
		String installname = StringUtil.getHttpParam(request(), "installname");
		if(installname==null)
			installname = "";	
		if ( !( installname .equals("") ) && !( installname .equals("undefined") ) ) 
			sql.append(" and ( installname= '" + installname + "'  )");
		
		String installarea = StringUtil.getHttpParam(request(), "installarea");
		if(installarea==null)
			installarea = "";	
		if ( !( installarea .equals("") ) && !( installarea .equals("undefined") ) ) 
			sql.append(" and ( installarea= '" + installarea + "'  )");
		
		String installaddress = StringUtil.getHttpParam(request(), "installaddress");
		if(installaddress==null)
			installaddress = "";	
		if ( !( installaddress .equals("") ) && !( installaddress .equals("undefined") ) ) 
			sql.append(" and ( installaddress= '" + installaddress + "'  )");
		
		String installphone = StringUtil.getHttpParam(request(), "installphone");
		if(installphone==null)
			installphone = "";	
		if ( !( installphone .equals("") ) && !( installphone .equals("undefined") ) ) 
			sql.append(" and ( installphone= '" + installphone + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String expressname = StringUtil.getHttpParam(request(), "expressname");
		if(expressname==null)
			expressname = "";	
		if ( !( expressname .equals("") ) && !( expressname .equals("undefined") ) ) 
			sql.append(" and ( expressname= '" + expressname + "'  )");
		
		String expressorderid = StringUtil.getHttpParam(request(), "expressorderid");
		if(expressorderid==null)
			expressorderid = "";	
		if ( !( expressorderid .equals("") ) && !( expressorderid .equals("undefined") ) ) 
			sql.append(" and ( expressorderid= '" + expressorderid + "'  )");
		
		String money = StringUtil.getHttpParam(request(), "money");
		if(money==null)
			money = "";	
		if ( !( money .equals("") ) && !( money .equals("undefined") ) ) 
			sql.append(" and ( money= '" + money + "'  )");
		
		String prepayid = StringUtil.getHttpParam(request(), "prepayid");
		if(prepayid==null)
			prepayid = "";	
		if ( !( prepayid .equals("") ) && !( prepayid .equals("undefined") ) ) 
			sql.append(" and ( prepayid= '" + prepayid + "'  )");
		
		String updatetime = StringUtil.getHttpParam(request(), "updatetime");
		if(updatetime==null)
			updatetime = "";	
		if ( !( updatetime .equals("") ) && !( updatetime .equals("undefined") ) ) 
			sql.append(" and ( updatetime= '" + updatetime + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubInstallOrders .class, sql.toString()).findRowCount();
		List<SubInstallOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubInstallOrders .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SubInstallOrders"+simp.format( new Date())+".xls";
			Logger.info(ulist.size()+"size++++++++++++++++++++++++"+fileName);
			File file=null;
			try{
				file= exportList(ulist,fileName);
			}catch(Exception e)
			{
				Logger.info("export file:"+e.toString());
			}

			return ok(file);
		}		
				
		int nPages = rowCount/nLimit;
		if( rowCount%nLimit>0 )
			nPages ++;
		PagedObject<SubInstallOrders> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SubInstallOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubInstallOrders .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//发货
	public static Result updateShip(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		String expressname = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressname");
		String expressorderid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressorderid");
		int execute = Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SubInstallOrders.class, "update sub_install_orders set expressname='"+expressname+"' ,expressorderid = '"+expressorderid+"', status = 3 where idd= '"+idd+"'").execute();
		if(execute==0) {
			return ok(Json.toJson(0));
		}
		SubInstallOrders sio = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubInstallOrders.class, "find SubInstallOrders where idd='"+idd+"'").findUnique();
		
		SubMsg sm = new SubMsg();
		sm.setOid(sio.getIdd());
		sm.setPhone(sio.getPhone());
		sm.setAct(1);//如果是sub_install_orders那么act=1
		sm.setContent("为您定制的导向片、锁体已发货，点击‘查看物流’查看物流状态");
		sm.setAddtime(new Date());
		Ebean.getServer(GlobalDBControl.getDB()).save(sm);
		WxGzhService.sendGzhMessage(sio.getPhone(), "尊敬的用户，您预约成功", "预约安装", "待派单", StringUtil.getDateTimeStr(new Date()), "为您定制的导向片、锁体已发货。中通物流单号："+sio.getExpressorderid());
		return ok(Json.toJson(execute));
	}
	//派单
	public static Result outOrder(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int execute = Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SubInstallOrders.class, "update sub_install_orders set status = 4 where idd= '"+idd+"'").execute();
		if(execute==0) {
			return ok(Json.toJson(0));
		}
		SubInstallOrders sio = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubInstallOrders.class, "find SubInstallOrders where idd='"+idd+"'").findUnique();
		SubMsg sm = new SubMsg();
		sm.setOid(sio.getIdd());
		sm.setPhone(sio.getPhone());
		sm.setContent("已问您分派安装师傅，请保持电话通畅，以方便安装师傅联系您");
		sm.setAct(1);
		sm.setAddtime(new Date());
		Ebean.getServer(GlobalDBControl.getDB()).save(sm);
		WxGzhService.sendGzhMessage(sio.getPhone(), "尊敬的用户，您预约成功", "预约安装", "已派单", StringUtil.getDateTimeStr(new Date()), "已为您分派安装师傅，请保持电话通畅，以方便安装师傅联系您");
		return ok(Json.toJson(execute));
	}
	//删除
	public static Result delete(int idd){
		String sql = "delete from SubInstallOrders where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SubInstallOrders .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SubInstallOrders subinstallorders = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubInstallOrders .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(subinstallorders) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String oid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "oid");
		
		
		
		String cid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "cid");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String doorthickness = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "doorthickness");
		
		
		
		String slicewidth = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "slicewidth");
		
		
		
		String sliceheight = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sliceheight");
		
		
		
		String lockimg1 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lockimg1");
		
		
		
		String lockimg2 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lockimg2");
		
		
		
		String hook = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "hook");
		
		
		
		String lockdirection = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lockdirection");
		
		
		
		String consigneename = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "consigneename");
		
		
		
		String consigneearea = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "consigneearea");
		
		
		
		String consigneeaddress = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "consigneeaddress");
		
		
		
		String consigneephone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "consigneephone");
		
		
		
		String installname = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installname");
		
		
		
		String installarea = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installarea");
		
		
		
		String installaddress = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installaddress");
		
		
		
		String installphone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installphone");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String expressname = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressname");
		
		
		
		String expressorderid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressorderid");
		
		
		
		String money = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "money");
		
		
		
		String prepayid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "prepayid");
		
		
		
		String updatetime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "updatetime");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SubInstallOrders subinstallorders ;
		if(  operation.equals("add") )	// 新增
			subinstallorders = new SubInstallOrders ();
		else			//修改
			subinstallorders = Ebean.getServer(GlobalDBControl.getDB())
					.find(SubInstallOrders .class).where().eq("idd", idd).findUnique();
		
		if( subinstallorders!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					subinstallorders. setPhone ( phone );
				 
				
				
			
				 
				
				
					subinstallorders. setOid ( StringTool.GetInt(oid,0) );
				
			
				 
				
				
					subinstallorders. setCid ( StringTool.GetInt(cid,0) );
				
			
				 
				
				
					subinstallorders. setStatus ( StringTool.GetInt(status,0) );
				
			
				 
				
				
					subinstallorders. setDoorthickness ( StringTool.GetInt(doorthickness,0) );
				
			
				 
				
				
					subinstallorders. setSlicewidth ( StringTool.GetInt(slicewidth,0) );
				
			
				 
				
				
					subinstallorders. setSliceheight ( StringTool.GetInt(sliceheight,0) );
				
			
				
					subinstallorders. setLockimg1 ( lockimg1 );
				 
				
				
			
				
					subinstallorders. setLockimg2 ( lockimg2 );
				 
				
				
			
				 
				
				
					subinstallorders. setHook ( StringTool.GetInt(hook,0) );
				
			
				 
				
				
					subinstallorders. setLockdirection ( StringTool.GetInt(lockdirection,0) );
				
			
				
					subinstallorders. setConsigneename ( consigneename );
				 
				
				
			
				
					subinstallorders. setConsigneearea ( consigneearea );
				 
				
				
			
				
					subinstallorders. setConsigneeaddress ( consigneeaddress );
				 
				
				
			
				
					subinstallorders. setConsigneephone ( consigneephone );
				 
				
				
			
				
					subinstallorders. setInstallname ( installname );
				 
				
				
			
				
					subinstallorders. setInstallarea ( installarea );
				 
				
				
			
				
					subinstallorders. setInstalladdress ( installaddress );
				 
				
				
			
				
					subinstallorders. setInstallphone ( installphone );
				 
				
				
			
				 
				
					subinstallorders. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				
					subinstallorders. setExpressname ( expressname );
				 
				
				
			
				
					subinstallorders. setExpressorderid ( expressorderid );
				 
				
				
			
				 
				
				
					subinstallorders. setMoney ( StringTool.GetInt(money,0) );
				
			
				
					subinstallorders. setPrepayid ( prepayid );
				 
				
				
			
				 
				
					subinstallorders. setUpdatetime ( StringUtil.getDate(updatetime) );
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( subinstallorders );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(subinstallorders) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SubInstallOrders> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
		
			sheet.setColumnWidth( 4 , 6000 );
		
			sheet.setColumnWidth( 5 , 6000 );
		
			sheet.setColumnWidth( 6 , 6000 );
		
			sheet.setColumnWidth( 7 , 6000 );
		
			sheet.setColumnWidth( 8 , 6000 );
		
			sheet.setColumnWidth( 9 , 6000 );
		
			sheet.setColumnWidth( 10 , 6000 );
		
			sheet.setColumnWidth( 11 , 6000 );
		
			sheet.setColumnWidth( 12 , 6000 );
		
			sheet.setColumnWidth( 13 , 6000 );
		
			sheet.setColumnWidth( 14 , 6000 );
		
			sheet.setColumnWidth( 15 , 6000 );
		
			sheet.setColumnWidth( 16 , 6000 );
		
			sheet.setColumnWidth( 17 , 6000 );
		
			sheet.setColumnWidth( 18 , 6000 );
		
			sheet.setColumnWidth( 19 , 6000 );
		
			sheet.setColumnWidth( 20 , 6000 );
		
			sheet.setColumnWidth( 21 , 6000 );
		
			sheet.setColumnWidth( 22 , 6000 );
		
			sheet.setColumnWidth( 23 , 6000 );
		
			sheet.setColumnWidth( 24 , 6000 );
		
			sheet.setColumnWidth( 25 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"idd"
					
				
					
					,"手机号"
				
					
					,"订单ID"
				
					
					,"使用兑换码ID"
				
					
					,"状态"
				
					
					,"门厚度"
				
					
					,"导向片宽度"
				
					
					,"导向片高度"
				
					
					,"门锁图1"
				
					
					,"门锁图2"
				
					
					,"2没有"
				
					
					,"门锁方向"
				
					
					,"收获人姓名"
				
					
					,"收获人地区"
				
					
					,"收获人地址"
				
					
					,"收获人手机号"
				
					
					,"安装联系人姓名"
				
					
					,"安装地址"
				
					
					,"安装详细地址"
				
					
					,"安装联系人手机号"
				
					
					,"添加时间"
				
					
					,"物流名称"
				
					
					,"物流订单"
				
					
					,"安装费用"
				
					
					,"预订单"
				
					
					,"支付时间"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SubInstallOrders info = (SubInstallOrders) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDoorthickness () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSlicewidth () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSliceheight () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLockimg1 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLockimg2 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getHook () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLockdirection () );
			
				helper.createStringCell( row , colIndex++, ""+info. getConsigneename () );
			
				helper.createStringCell( row , colIndex++, ""+info. getConsigneearea () );
			
				helper.createStringCell( row , colIndex++, ""+info. getConsigneeaddress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getConsigneephone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstallname () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstallarea () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstalladdress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstallphone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getExpressname () );
			
				helper.createStringCell( row , colIndex++, ""+info. getExpressorderid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMoney () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPrepayid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getUpdatetime () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SubInstallOrders" + System.currentTimeMillis() + numStra + ".xls";
		try {
			out = new FileOutputStream(fileName);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File downFile = new File(fileName);
		if (request().getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileNameChine = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameChine.getBytes("UTF-8")))) + "?=";    
        } else{
        	fileNameChine = java.net.URLEncoder.encode(fileNameChine, "UTF-8");
        }

		response().setHeader("Content-disposition","attachment;filename=" + fileNameChine);
		response().setContentType("application/msexcel");
		return downFile;
	}
	
	static String[]  headers = {
				
					
					
				
					"手机号"
					
				
					
					,"订单ID"
				
					
					,"使用兑换码ID"
				
					
					,"状态"
				
					
					,"门厚度"
				
					
					,"导向片宽度"
				
					
					,"导向片高度"
				
					
					,"门锁图1"
				
					
					,"门锁图2"
				
					
					,"2没有"
				
					
					,"门锁方向"
				
					
					,"收获人姓名"
				
					
					,"收获人地区"
				
					
					,"收获人地址"
				
					
					,"收获人手机号"
				
					
					,"安装联系人姓名"
				
					
					,"安装地址"
				
					
					,"安装详细地址"
				
					
					,"安装联系人手机号"
				
					
					,"添加时间"
				
					
					,"物流名称"
				
					
					,"物流订单"
				
					
					,"安装费用"
				
					
					,"预订单"
				
					
					,"支付时间"
				
		};
	static int MAX_LINES = 10000;
	public static Result upload(){
		String cid = AjaxHellper.getHttpParam(request(), "uploadidd");
		Integer ncid;
		int successNum = 0; //导入成功的数量
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file_excel = body.getFile("file_excel");
		
		if (file_excel != null) {
			String fileName = file_excel.getFilename();
			int rowNum = 0;//导入Excel表里的有效记录数
			File file = file_excel.getFile();
			
			File fileTemp = new File(ExcelUtils.getUploadPath());
			fileTemp.mkdir();
			
			String uploadFileName = ExcelUtils.getUploadPath() + ExcelUtils.addTimeTagFileName(fileName);
			File destFile = ExcelUtils.createFile(uploadFileName);
			
			ExcelUtils.copy(file, destFile);
			file.delete();//将临时文件删除掉
			
			Workbook workBook = null;
			FileInputStream fis = null;
			
			try {
				fis = new FileInputStream(new File(uploadFileName));
			} catch (FileNotFoundException e) {
				return ok(Json.toJson(successNum));
			}

			try {
				workBook = WorkbookFactory.create(fis);
			} catch (Exception e) {
				Logger.info(e.toString());
				return ok(Json.toJson(-1));
			} 
			
			Sheet sheet = workBook.getSheetAt(0);
			int lastRowNumber = sheet.getLastRowNum();
			Row rowTitle = sheet.getRow(0);
			if(rowTitle==null){
				return ok(Json.toJson(successNum));
			}
			// 表头
			for(int i=0;i < headers.length;i++){
				String cellValue = StringUtil.cellValueToString(rowTitle.getCell(i));
				if(!cellValue.equalsIgnoreCase(headers[i])){
					
				}
			}
			for (int i = 1; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				if (checkNullOrNot(row)) {
					break;
				}
				rowNum++;
			}
			
			if( rowNum > MAX_LINES )
				return ok( Json.toJson(successNum) );
			
			List<SubInstallOrders> list = new ArrayList<SubInstallOrders>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SubInstallOrders fc = new SubInstallOrders();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setOid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setCid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setDoorthickness ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setSlicewidth ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setSliceheight ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setLockimg1 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setLockimg2 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setHook ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setLockdirection ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setConsigneename ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setConsigneearea ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setConsigneeaddress ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setConsigneephone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setInstallname ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setInstallarea ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setInstalladdress ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setInstallphone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					
						fc. setExpressname ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setExpressorderid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setMoney ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setPrepayid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setUpdatetime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
                 list.add(fc);
                 if( list.size()>90 ){
                	 Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	 list.clear();
                 }
			 }
			 
			 if( list.size()>0 ){
				 successNum = Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 list.clear();
			 }
			 
			 return ok(Json.toJson(successNum));
		}
	
		return ok(Json.toJson(successNum));
	}
	
	//检查是否是一个完全的空行
	public static boolean checkNullOrNot(Row row){
		if(row==null)
			return true;
		int index=0;
		for(int i=0;i < headers.length;i++){
			String cellValue = StringUtil.cellValueToString(row.getCell(i));
			if(cellValue==null||cellValue.trim().equals("")){
				index++;
			}
		}
		return index == headers.length;
	}
	
}