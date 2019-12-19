package util.jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import play.Configuration;
import play.Logger;

public class Config extends Properties {

	private static final long serialVersionUID = 50440463580273222L;

	private static Config instance = null;
	private Long lastModifyTime;
	public static synchronized Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getProperty(String key, String defaultValue) {
		String val = getProperty(key);
		return (val == null || val.isEmpty()) ? defaultValue : val;
	}

	public String getString(String name, String defaultValue) {
		return this.getProperty(name, defaultValue);
	}

	public int getInt(String name, int defaultValue) {
		String val = this.getProperty(name);
		return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
	}

	public long getLong(String name, long defaultValue) {
		String val = this.getProperty(name);
		return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
	}

	public float getFloat(String name, float defaultValue) {
		String val = this.getProperty(name);
		return (val == null || val.isEmpty()) ? defaultValue : Float.parseFloat(val);
	}

	public double getDouble(String name, double defaultValue) {
		String val = this.getProperty(name);
		return (val == null || val.isEmpty()) ? defaultValue : Double.parseDouble(val);
	}

	public byte getByte(String name, byte defaultValue) {
		String val = this.getProperty(name);
		return (val == null || val.isEmpty()) ? defaultValue : Byte.parseByte(val);
	}
	public void reloadProperty(){
		try {
			String	filepath = null;
			InputStream in = null;
			filepath = Configuration.root().getString("app.dir")+"conf/redis_config.properties";
			File file = new File(filepath);
			Long modifyTime = file.lastModified();
			if(modifyTime>lastModifyTime){
					in = new FileInputStream(file);
					this.load(in);
					in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			Logger.error("reloadProperty",e);
		}
	}
	public Config() {
		
		try {
			String	filepath = null;
			filepath = Configuration.root().getString("app.dir")+"conf/redis_config.properties";
			File file = new File(filepath);
			lastModifyTime = file.lastModified();
			InputStream in = null;
			in = new FileInputStream(file);
			this.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			Logger.error("Config",e);
		}
	}
}
