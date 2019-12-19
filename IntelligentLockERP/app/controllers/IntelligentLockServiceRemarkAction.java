
package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import models.*;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;
import util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import play.libs.Json;
import play.mvc.*;
import play.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class IntelligentLockServiceRemarkAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockServiceRemarkList.render() );
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
		sql.append("find IntelligentLockServiceRemark where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String promoter = StringUtil.getHttpParam(request(), "promoter");
		if(promoter==null)
			promoter = "";	
		if ( !( promoter .equals("") ) && !( promoter .equals("undefined") ) ) 
			sql.append(" and ( promoter= '" + promoter + "'  )");
		
		String ordernumber = StringUtil.getHttpParam(request(), "ordernumber");
		if(ordernumber==null)
			ordernumber = "";	
		if ( !( ordernumber .equals("") ) && !( ordernumber .equals("undefined") ) ) 
			sql.append(" and ( ordernumber= '" + ordernumber + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String installprincipal = StringUtil.getHttpParam(request(), "installprincipal");
		if(installprincipal==null)
			installprincipal = "";	
		if ( !( installprincipal .equals("") ) && !( installprincipal .equals("undefined") ) ) 
			sql.append(" and ( installprincipal= '" + installprincipal + "'  )");
		
		String ordertime = StringUtil.getHttpParam(request(), "ordertime");
		if(ordertime==null)
			ordertime = "";	
		if ( !( ordertime .equals("") ) && !( ordertime .equals("undefined") ) ) 
			sql.append(" and ( ordertime= '" + ordertime + "'  )");
		
		String deliverytime = StringUtil.getHttpParam(request(), "deliverytime");
		if(deliverytime==null)
			deliverytime = "";	
		if ( !( deliverytime .equals("") ) && !( deliverytime .equals("undefined") ) ) 
			sql.append(" and ( deliverytime= '" + deliverytime + "'  )");
		
		String logistics = StringUtil.getHttpParam(request(), "logistics");
		if(logistics==null)
			logistics = "";	
		if ( !( logistics .equals("") ) && !( logistics .equals("undefined") ) ) 
			sql.append(" and ( logistics= '" + logistics + "'  )");
		
		String arrivaltime = StringUtil.getHttpParam(request(), "arrivaltime");
		if(arrivaltime==null)
			arrivaltime = "";	
		if ( !( arrivaltime .equals("") ) && !( arrivaltime .equals("undefined") ) ) 
			sql.append(" and ( arrivaltime= '" + arrivaltime + "'  )");
		
		String guidecustommadetime = StringUtil.getHttpParam(request(), "guidecustommadetime");
		if(guidecustommadetime==null)
			guidecustommadetime = "";	
		if ( !( guidecustommadetime .equals("") ) && !( guidecustommadetime .equals("undefined") ) ) 
			sql.append(" and ( guidecustommadetime= '" + guidecustommadetime + "'  )");
		
		String guidesigningtime = StringUtil.getHttpParam(request(), "guidesigningtime");
		if(guidesigningtime==null)
			guidesigningtime = "";	
		if ( !( guidesigningtime .equals("") ) && !( guidesigningtime .equals("undefined") ) ) 
			sql.append(" and ( guidesigningtime= '" + guidesigningtime + "'  )");
		
		String reservationtime = StringUtil.getHttpParam(request(), "reservationtime");
		if(reservationtime==null)
			reservationtime = "";	
		if ( !( reservationtime .equals("") ) && !( reservationtime .equals("undefined") ) ) 
			sql.append(" and ( reservationtime= '" + reservationtime + "'  )");
		
		String ordersuctime = StringUtil.getHttpParam(request(), "ordersuctime");
		if(ordersuctime==null)
			ordersuctime = "";	
		if ( !( ordersuctime .equals("") ) && !( ordersuctime .equals("undefined") ) ) 
			sql.append(" and ( ordersuctime= '" + ordersuctime + "'  )");
		
		String contact = StringUtil.getHttpParam(request(), "contact");
		if(contact==null)
			contact = "";	
		if ( !( contact .equals("") ) && !( contact .equals("undefined") ) ) 
			sql.append(" and ( contact= '" + contact + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String addr = StringUtil.getHttpParam(request(), "addr");
		if(addr==null)
			addr = "";	
		if ( !( addr .equals("") ) && !( addr .equals("undefined") ) ) 
			sql.append(" and ( addr= '" + addr + "'  )");
		
		String channel = StringUtil.getHttpParam(request(), "channel");
		if(channel==null)
			channel = "";	
		if ( !( channel .equals("") ) && !( channel .equals("undefined") ) ) 
			sql.append(" and ( channel= '" + channel + "'  )");
		
		String type = StringUtil.getHttpParam(request(), "type");
		if(type==null)
			type = "";	
		if ( !( type .equals("") ) && !( type .equals("undefined") ) ) 
			sql.append(" and ( type= '" + type + "'  )");
		
		String model = StringUtil.getHttpParam(request(), "model");
		if(model==null)
			model = "";	
		if ( !( model .equals("") ) && !( model .equals("undefined") ) ) 
			sql.append(" and ( model= '" + model + "'  )");
		
		String spec = StringUtil.getHttpParam(request(), "spec");
		if(spec==null)
			spec = "";	
		if ( !( spec .equals("") ) && !( spec .equals("undefined") ) ) 
			sql.append(" and ( spec= '" + spec + "'  )");
		
		String num = StringUtil.getHttpParam(request(), "num");
		if(num==null)
			num = "";	
		if ( !( num .equals("") ) && !( num .equals("undefined") ) ) 
			sql.append(" and ( num= '" + num + "'  )");
		
		String material = StringUtil.getHttpParam(request(), "material");
		if(material==null)
			material = "";	
		if ( !( material .equals("") ) && !( material .equals("undefined") ) ) 
			sql.append(" and ( material= '" + material + "'  )");
		
		String thickness = StringUtil.getHttpParam(request(), "thickness");
		if(thickness==null)
			thickness = "";	
		if ( !( thickness .equals("") ) && !( thickness .equals("undefined") ) ) 
			sql.append(" and ( thickness= '" + thickness + "'  )");
		
		String opendirection = StringUtil.getHttpParam(request(), "opendirection");
		if(opendirection==null)
			opendirection = "";	
		if ( !( opendirection .equals("") ) && !( opendirection .equals("undefined") ) ) 
			sql.append(" and ( opendirection= '" + opendirection + "'  )");
		
		String hook = StringUtil.getHttpParam(request(), "hook");
		if(hook==null)
			hook = "";	
		if ( !( hook .equals("") ) && !( hook .equals("undefined") ) ) 
			sql.append(" and ( hook= '" + hook + "'  )");
		
		String guidecustommade = StringUtil.getHttpParam(request(), "guidecustommade");
		if(guidecustommade==null)
			guidecustommade = "";	
		if ( !( guidecustommade .equals("") ) && !( guidecustommade .equals("undefined") ) ) 
			sql.append(" and ( guidecustommade= '" + guidecustommade + "'  )");
		
		String guidetype = StringUtil.getHttpParam(request(), "guidetype");
		if(guidetype==null)
			guidetype = "";	
		if ( !( guidetype .equals("") ) && !( guidetype .equals("undefined") ) ) 
			sql.append(" and ( guidetype= '" + guidetype + "'  )");
		
		String guidesize = StringUtil.getHttpParam(request(), "guidesize");
		if(guidesize==null)
			guidesize = "";	
		if ( !( guidesize .equals("") ) && !( guidesize .equals("undefined") ) ) 
			sql.append(" and ( guidesize= '" + guidesize + "'  )");
		
		String installationprice = StringUtil.getHttpParam(request(), "installationprice");
		if(installationprice==null)
			installationprice = "";	
		if ( !( installationprice .equals("") ) && !( installationprice .equals("undefined") ) ) 
			sql.append(" and ( installationprice= '" + installationprice + "'  )");
		
		String guidecustommadeprice = StringUtil.getHttpParam(request(), "guidecustommadeprice");
		if(guidecustommadeprice==null)
			guidecustommadeprice = "";	
		if ( !( guidecustommadeprice .equals("") ) && !( guidecustommadeprice .equals("undefined") ) ) 
			sql.append(" and ( guidecustommadeprice= '" + guidecustommadeprice + "'  )");
		
		String measureprice = StringUtil.getHttpParam(request(), "measureprice");
		if(measureprice==null)
			measureprice = "";	
		if ( !( measureprice .equals("") ) && !( measureprice .equals("undefined") ) ) 
			sql.append(" and ( measureprice= '" + measureprice + "'  )");
		
		String emptyrunprice = StringUtil.getHttpParam(request(), "emptyrunprice");
		if(emptyrunprice==null)
			emptyrunprice = "";	
		if ( !( emptyrunprice .equals("") ) && !( emptyrunprice .equals("undefined") ) ) 
			sql.append(" and ( emptyrunprice= '" + emptyrunprice + "'  )");
		
		String totalprice = StringUtil.getHttpParam(request(), "totalprice");
		if(totalprice==null)
			totalprice = "";	
		if ( !( totalprice .equals("") ) && !( totalprice .equals("undefined") ) ) 
			sql.append(" and ( totalprice= '" + totalprice + "'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		String repair = StringUtil.getHttpParam(request(), "repair");
		if(repair==null)
			repair = "";	
		if ( !( repair .equals("") ) && !( repair .equals("undefined") ) ) 
			sql.append(" and ( repair= '" + repair + "'  )");
		
		String bearer = StringUtil.getHttpParam(request(), "bearer");
		if(bearer==null)
			bearer = "";	
		if ( !( bearer .equals("") ) && !( bearer .equals("undefined") ) ) 
			sql.append(" and ( bearer= '" + bearer + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockServiceRemark .class, sql.toString()).findRowCount();
		List<IntelligentLockServiceRemark> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockServiceRemark .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockServiceRemark"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockServiceRemark> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockServiceRemark> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockServiceRemark .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockServiceRemark where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockServiceRemark .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockServiceRemark intelligentlockserviceremark = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockServiceRemark .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockserviceremark) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String promoter = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "promoter");
		
		
		
		String ordernumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ordernumber");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String installprincipal = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installprincipal");
		
		
		
		String ordertime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ordertime");
		
		
		
		String deliverytime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deliverytime");
		
		
		
		String logistics = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "logistics");
		
		
		
		String arrivaltime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "arrivaltime");
		
		
		
		String guidecustommadetime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidecustommadetime");
		
		
		
		String guidesigningtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidesigningtime");
		
		
		
		String reservationtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "reservationtime");
		
		
		
		String ordersuctime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ordersuctime");
		
		
		
		String contact = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "contact");
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String addr = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addr");
		
		
		
		String channel = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "channel");
		
		
		
		String type = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "type");
		
		
		
		String model = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "model");
		
		
		
		String spec = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "spec");
		
		
		
		String num = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "num");
		
		
		
		String material = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "material");
		
		
		
		String thickness = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "thickness");
		
		
		
		String opendirection = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "opendirection");
		
		
		
		String hook = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "hook");
		
		
		
		String guidecustommade = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidecustommade");
		
		
		
		String guidetype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidetype");
		
		
		
		String guidesize = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidesize");
		
		
		
		String installationprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "installationprice");
		
		
		
		String guidecustommadeprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "guidecustommadeprice");
		
		
		
		String measureprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measureprice");
		
		
		
		String emptyrunprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "emptyrunprice");
		
		
		
		Double totalprice = Double.parseDouble(installationprice) + Double.parseDouble(guidecustommadeprice) + Double.parseDouble(measureprice) + Double.parseDouble(emptyrunprice);
		
		
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		
		
		String repair = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "repair");
		
		
		
		String bearer = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "bearer");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		IntelligentLockServiceRemark intelligentlockserviceremark ;
		if(  operation.equals("add") )	// 新增
			intelligentlockserviceremark = new IntelligentLockServiceRemark ();
		else			//修改
			intelligentlockserviceremark = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockServiceRemark .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockserviceremark!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					intelligentlockserviceremark. setPromoter ( promoter );
				 
				
				
			
				
					intelligentlockserviceremark. setOrdernumber ( ordernumber );
				 
				
				
			
				
					intelligentlockserviceremark. setStatus ( status );
				 
				
				
			
				
					intelligentlockserviceremark. setInstallprincipal ( installprincipal );
				 
				
				
			
				 
				
					intelligentlockserviceremark. setOrdertime ( StringUtil.getDate(ordertime) );
				
				
			
				 
				
					intelligentlockserviceremark. setDeliverytime ( StringUtil.getDate(deliverytime) );
				
				
			
				
					intelligentlockserviceremark. setLogistics ( logistics );
				 
				
				
			
				 
				
					intelligentlockserviceremark. setArrivaltime ( StringUtil.getDate(arrivaltime) );
				
				
			
				 
				
					intelligentlockserviceremark. setGuidecustommadetime ( StringUtil.getDate(guidecustommadetime) );
				
				
			
				 
				
					intelligentlockserviceremark. setGuidesigningtime ( StringUtil.getDate(guidesigningtime) );
				
				
			
				 
				
					intelligentlockserviceremark. setReservationtime ( StringUtil.getDate(reservationtime) );
				
				
			
				 
				
					intelligentlockserviceremark. setOrdersuctime ( StringUtil.getDate(ordersuctime) );
				
				
			
				
					intelligentlockserviceremark. setContact ( contact );
				 
				
				
			
				
					intelligentlockserviceremark. setPhone ( phone );
				 
				
				
			
				
					intelligentlockserviceremark. setAddr ( addr );
				 
				
				
			
				
					intelligentlockserviceremark. setChannel ( channel );
				 
				
				
			
				
					intelligentlockserviceremark. setType ( type );
				 
				
				
			
				
					intelligentlockserviceremark. setModel ( model );
				 
				
				
			
				
					intelligentlockserviceremark. setSpec ( spec );
				 
				
				
			
				 
				
				
					intelligentlockserviceremark. setNum ( StringTool.GetInt(num,0) );
				
			
				
					intelligentlockserviceremark. setMaterial ( material );
				 
				
				
			
				
					intelligentlockserviceremark. setThickness ( thickness );
				 
				
				
			
				
					intelligentlockserviceremark. setOpendirection ( opendirection );
				 
				
				
			
				
					intelligentlockserviceremark. setHook ( hook );
				 
				
				
			
				
					intelligentlockserviceremark. setGuidecustommade ( guidecustommade );
				 
				
				
			
				
					intelligentlockserviceremark. setGuidetype ( guidetype );
				 
				
				
			
				
					intelligentlockserviceremark. setGuidesize ( guidesize );
				 
				
					
					
					intelligentlockserviceremark.setInstallationprice(StringTool.GetDouble(installationprice, 0d));
				 
				
				
			
					intelligentlockserviceremark.setGuidecustommadeprice(StringTool.GetDouble(guidecustommadeprice,0d));
				
				
			
				 
					intelligentlockserviceremark.setMeasureprice(StringTool.GetDouble(measureprice,0d));
				
			
				 
				
					intelligentlockserviceremark.setEmptyrunprice(StringTool.GetDouble(emptyrunprice,0d));
			
				 
					
					intelligentlockserviceremark.setTotalprice(totalprice);
				
			
					
					intelligentlockserviceremark.setRemark ( remark );
				 
					
					intelligentlockserviceremark.setRepair(StringTool.GetDouble(repair,0d));		
			
				
					intelligentlockserviceremark. setBearer ( bearer );
				 
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockserviceremark );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockserviceremark) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockServiceRemark> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			sheet.setColumnWidth( 26 , 6000 );
		
			sheet.setColumnWidth( 27 , 6000 );
		
			sheet.setColumnWidth( 28 , 6000 );
		
			sheet.setColumnWidth( 29 , 6000 );
		
			sheet.setColumnWidth( 30 , 6000 );
		
			sheet.setColumnWidth( 31 , 6000 );
		
			sheet.setColumnWidth( 32 , 6000 );
		
			sheet.setColumnWidth( 33 , 6000 );
		
			sheet.setColumnWidth( 34 , 6000 );
		
			sheet.setColumnWidth( 35 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"IDD"
					
				
					
					,"订单引流人"
				
					
					,"订单号"
				
					
					,"订单状态"
				
					
					,"订单安装负责人"
				
					
					,"锁下单时间"
				
					
					,"发货时间"
				
					
					,"物流名称"
				
					
					,"锁到货时间"
				
					
					,"导向片定制时间"
				
					
					,"导向片签收时间"
				
					
					,"预约安装时间"
				
					
					,"订单完成时间"
				
					
					,"用户联系人"
				
					
					,"用户联系电话"
				
					
					,"安装地址"
				
					
					,"渠道"
				
					
					,"需安装产品类型"
				
					
					,"需安装产品型号"
				
					
					,"安装产品规格"
				
					
					,"安装数量"
				
					
					,"门材质"
				
					
					,"门厚度"
				
					
					,"开门方向"
				
					
					,"天地钩"
				
					
					,"是否定制导向片"
				
					
					,"导向片类型"
				
					
					,"导向片尺寸"
				
					
					,"安装费"
				
					
					,"定制导向片费用"
				
					
					,"测量"
				
					
					,"空跑费"
				
					
					,"总价"
				
					
					,"备注"
				
					
					,"维修费用"
				
					
					,"承担方"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockServiceRemark info = (IntelligentLockServiceRemark) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPromoter () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOrdernumber () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstallprincipal () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOrdertime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDeliverytime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLogistics () );
			
				helper.createStringCell( row , colIndex++, ""+info. getArrivaltime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidecustommadetime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidesigningtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getReservationtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOrdersuctime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getContact () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddr () );
			
				helper.createStringCell( row , colIndex++, ""+info. getChannel () );
			
				helper.createStringCell( row , colIndex++, ""+info. getType () );
			
				helper.createStringCell( row , colIndex++, ""+info. getModel () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSpec () );
			
				helper.createStringCell( row , colIndex++, ""+info. getNum () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMaterial () );
			
				helper.createStringCell( row , colIndex++, ""+info. getThickness () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOpendirection () );
			
				helper.createStringCell( row , colIndex++, ""+info. getHook () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidecustommade () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidetype () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidesize () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInstallationprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGuidecustommadeprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasureprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getEmptyrunprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTotalprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRepair () );
			
				helper.createStringCell( row , colIndex++, ""+info. getBearer () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "IntelligentLockServiceRemark" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"订单引流人"
					
				
					
					,"订单号"
				
					
					,"订单状态"
				
					
					,"订单安装负责人"
				
					
					,"锁下单时间"
				
					
					,"发货时间"
				
					
					,"物流名称"
				
					
					,"锁到货时间"
				
					
					,"导向片定制时间"
				
					
					,"导向片签收时间"
				
					
					,"预约安装时间"
				
					
					,"订单完成时间"
				
					
					,"用户联系人"
				
					
					,"用户联系电话"
				
					
					,"安装地址"
				
					
					,"渠道"
				
					
					,"需安装产品类型"
				
					
					,"需安装产品型号"
				
					
					,"安装产品规格"
				
					
					,"安装数量"
				
					
					,"门材质"
				
					
					,"门厚度"
				
					
					,"开门方向"
				
					
					,"天地钩"
				
					
					,"是否定制导向片"
				
					
					,"导向片类型"
				
					
					,"导向片尺寸"
				
					
					,"安装费"
				
					
					,"定制导向片费用"
				
					
					,"测量"
				
					
					,"空跑费"
				
					//excel不要总价列
//					,"总价"
				
					
					,"备注"
				
					
					,"维修费用"
				
					
					,"承担方"
				
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
			
//			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.CHINA);
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			
			List<IntelligentLockServiceRemark> list = new ArrayList<IntelligentLockServiceRemark>();
			 for (int i = 2; i <= lastRowNumber; i++) {//起始行定义
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 if(StringUtil.cellValueToString(row.getCell(4)).trim()=="") {
                	 break;
     			 }
                 IntelligentLockServiceRemark fc = new IntelligentLockServiceRemark();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setPromoter ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setOrdernumber ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setStatus ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
					
						fc. setInstallprincipal ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
						
						
						try {
							fc. setOrdertime ( format.parse(StringUtil.cellValueToString(row.getCell(j))));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setDeliverytime ( format.parse(StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					
						fc. setLogistics ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setArrivaltime ( format.parse( StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setGuidecustommadetime ( format.parse( StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setGuidesigningtime ( format.parse( StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setReservationtime ( format.parse( StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					 
					
						try {
							fc. setOrdersuctime ( format.parse( StringUtil.cellValueToString(row.getCell(j) )));
						} catch (ParseException e) {
							LogUtil.writeLog("时间转换异常："+e.getMessage(), "ServiceRemarkAction");
							e.printStackTrace();
						}
					
					
					
						j=j+1;
					
				
					
						fc. setContact ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setAddr ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setChannel ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setType ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setModel ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setSpec ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setNum ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setMaterial ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setThickness ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setOpendirection ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setHook ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setGuidecustommade ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setGuidetype ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setGuidesize ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
						fc.setInstallationprice(StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)), 0d));
						 
						
						
						
					
					
					
						j=j+1;
					
				
					 
					
						fc.setGuidecustommadeprice(StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d));
						
						
						
						
					
					
						j=j+1;
					
				
					 
					
					
					
						fc.setMeasureprice(StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d));
						
						
						
						
						j=j+1;
					
				
					 
					
					
						fc.setEmptyrunprice(StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d));
						
						
						
					
						//j=j+1;
					
						//excel不要总价列
					 
						Double totalprice = StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(27)),0d)+
								StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(28)),0d)+
								StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(29)),0d)+
								StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(30)),0d);
						
						fc.setTotalprice(totalprice);
					
					
					
						j=j+1;
					
				
					
						fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
						fc.setRepair(StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d));
						
					
					
						j=j+1;
					
				
					
						fc. setBearer ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
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