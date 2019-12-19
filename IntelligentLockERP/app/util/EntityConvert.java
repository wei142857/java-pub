package util;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;

import com.avaje.ebean.SqlRow;

public class EntityConvert {
	/**
	 * 将sqlrow转为实体
	 * @param sr
	 * @param clazz
	 * @return
	 */
	public static <T> T convert(SqlRow sr, Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
			Field[] flies = clazz.getDeclaredFields();
			for (Field f : flies) {
				f.setAccessible(true);
				if (String.class.getName().equals(f.getType().getName())) {
					if (sr.getString(f.getName()) != null)
						f.set(instance, sr.getString(f.getName()));
				} else if (Long.class.getName().equals(f.getType().getName())
						|| long.class.getName().equals(f.getType().getName())) {
					if (sr.getLong(f.getName()) != null)
						f.set(instance, sr.getLong(f.getName()));
				} else if (Integer.class.getName()
						.equals(f.getType().getName())
						|| int.class.getName().equals(f.getType().getName())) {
					if (sr.getInteger(f.getName()) != null)
						f.set(instance, sr.getInteger(f.getName()));
				}else if(BigInteger.class.getName().equals(f.getType().getName())){
					if (sr.getString(f.getName()) != null)
						f.set(instance, new BigInteger(sr.getString(f.getName())));
				} else if (Boolean.class.getName()
						.equals(f.getType().getName())
						|| boolean.class.getName()
								.equals(f.getType().getName())) {
					if (sr.getBoolean(f.getName()) != null)
						f.set(instance, sr.getBoolean(f.getName()));
				} else if (Date.class.getName().equals(f.getType().getName())) {
					try{
						if (sr.getUtilDate(f.getName()) != null)
							f.set(instance, sr.getUtilDate(f.getName()));
					}catch(Exception e){
						if (sr.getDate(f.getName()) != null)
							f.set(instance, sr.getDate(f.getName()));
					}
				} else if (Double.class.getName().equals(f.getType().getName())
						|| double.class.getName().equals(f.getType().getName())) {
					if (sr.getDouble(f.getName()) != null)
						f.set(instance, sr.getDouble(f.getName()));
				} else if (Float.class.getName().equals(f.getType().getName())
						|| float.class.getName().equals(f.getType().getName())) {
					if (sr.getFloat(f.getName()) != null)
						f.set(instance, sr.getFloat(f.getName()));
				}

			}
		} catch (IllegalAccessException e) {
			Logger.info(e.toString());
			e.printStackTrace();
		} catch (InstantiationException e) {
			Logger.info(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}
	public static <T> List<T> convert(List<SqlRow> list, Class<T> clazz) {
		List<T> reList = new ArrayList<T>();
		for(SqlRow sr:list){
			reList.add(convert(sr,clazz));
		}
		return reList;
	}
	
}
