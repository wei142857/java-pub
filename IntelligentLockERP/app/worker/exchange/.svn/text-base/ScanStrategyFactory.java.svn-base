package worker.exchange;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;
import worker.exchange.builders.AStrategy;
import worker.scan.ClasspathPackageScanner;

public class ScanStrategyFactory{
	//存储所有策略
	private final ConcurrentHashMap<String, AStrategy> strategies = new ConcurrentHashMap<String, AStrategy>();
	public ScanStrategyFactory(){
		
		ClasspathPackageScanner scan = new ClasspathPackageScanner("worker.exchange.strategy");
		
		try {
			List<Class<?>> classes = scan.getClasses();
			for (Class<?> beanClazz : classes) {
				AStrategy stra = (AStrategy)beanClazz.newInstance();
				strategies.put(beanClazz.getSimpleName(), stra);
			}
			Logger.info("ScanPurchaseWork start");
			for(String name:strategies.keySet()){
				Logger.info(name);
			}
			
		} catch (Exception e) {
			Logger.error("ScanPurchaseWork error",e);
		}
	}
	public static ScanStrategyFactory instance;
		
	public static ScanStrategyFactory getInstance(){
		if(instance == null)
			instance = new ScanStrategyFactory();
		return instance;
	}
	
	public AStrategy getStrategy(String name){
		return strategies.get(name);
	}
}
