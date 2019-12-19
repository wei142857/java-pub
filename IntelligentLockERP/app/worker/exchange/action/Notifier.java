package worker.exchange.action;

import java.util.Map;

import models.SmartExchanges;
import models.SmartInterface;
import Service.unicom.dto.IFResponse;
/**
 * 通知充值结果
 * @author xuml
 *
 */
public interface Notifier {
	IFResponse callback(SmartExchanges record,SmartInterface si,Map<String,Object> ext);
}
