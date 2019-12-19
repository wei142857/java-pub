package worker.exchange.action;

import java.util.Map;

import models.SmartExchanges;
import models.SmartInterface;
import Service.unicom.dto.IFResponse;
/**
 * 查询充值结果
 * @author xuml
 *
 */
public interface Monitor {
	IFResponse doQuery(SmartExchanges record,SmartInterface si,Map<String,Object> ext);
	IFResponse doQuery(SmartExchanges record,SmartInterface si);
}
