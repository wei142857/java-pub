package app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import models.SmartAppMsg;
import models.SmartAppUser;
import play.mvc.Result;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.ReturnJson;
import app.dto.SysMessageListDto;
import app.service.MessageService;
import app.service.UserService;
import app.util.AppUtil;

public class MessageAction extends BaseAction{
	public static Result getSysMessageList(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String pageno = request().getQueryString("pageno");
		String pagesize = request().getQueryString("pagesize");
		Integer npageno = StringTool.GetInt(pageno, 0);
		Integer npagesize = StringTool.GetInt(pagesize, 0);
		if(StringUtil.isNull(token) ||npageno<=0 || npagesize<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SysMessageListDto data = new SysMessageListDto();
			List<SysMessageListDto.Item> items = new ArrayList<SysMessageListDto.Item>();
			TreeMap<String,Object> params = new TreeMap<String,Object>();
			params.put("userid", user.getIdd());
			List<SmartAppMsg> messages = MessageService.findMessages(params, npageno-1, npagesize);
			Integer count = MessageService.findMessagesCountByCondition(params);
			for(SmartAppMsg message:messages){
				SysMessageListDto.Item item = new SysMessageListDto.Item();
				item.setMsgcontent(message.getMsg());
				item.setMsgdate(StringUtil.getDateTimeStr(message.getAddtime()));
				item.setMsgid(message.getIdd());
				items.add(item);
			}
			data.setPageno(npageno);
			data.setTotalpages(AppUtil.getTotalPage(count, npagesize));
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
}
