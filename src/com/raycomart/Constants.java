package com.raycomart;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* @author Zhouluning
* @version CreateTime：2018年7月20日 下午8:26:20
* 
* 常量类
*/

public class Constants extends Properties {
	
	private static Logger log = LogManager.getLogger();
	
	private static final long serialVersionUID = -6399397700644610347L;

	public static Constants P = null;
	
	/*
	 * properties文件
	 */
	public static final String PFILE_CONFIG = "config.txt";	

	/**
	 * LeapMotion作用区域
	 */
	// X轴
	public static float X_AREA_MIN = 0f;		// -X
	public static float X_AREA_MAX = 0f;		// +X
	
	// Y轴
	public static float Y_AREA_MIN = 0f;		// -Y
	public static float Y_AREA_MAX = 0f;		// +Y
	
	// Z轴
	public static float Z_AREA_MIN = 0f;		// -Z
	public static float Z_AREA_MAX = 0f;		// +Z
	
	/*
	 * 手类型
	 */
	public static final String HAND_TYPE_RIGHT = "R";	// 右手
	public static final String HAND_TYPE_LEFT = "L";	// 左手
	
	/*
	 * 是否转换坐标为0-1
	 */
	public static Boolean MAP_COORDINATE = false; 
	
	
	/**
	 * 载入所有配置文件
	 */
	public static synchronized void initProperties() {
		try {
			if(P != null) {
				return;
			}
			
			log.info("loading properties..............");
			P = new Constants();
			String[] arrProFiles = {PFILE_CONFIG};
			
			for (String proFile : arrProFiles) {
				log.info("load properties file:" + proFile);
				P.load(Constants.class.getResourceAsStream("/" + proFile));
			}
			
		} catch (Exception e) {
			log.error("System Error!",e);
		}
	}
	
	public static String get(String key) {
		String value = "";
		try {
			if (key == null || P == null) {
				return "";
			}
			if (P.containsKey(key))
				value = new String(P.getProperty(key).getBytes("UTF-8"),
						"UTF-8").trim();
			else
				value = "";
		} catch (UnsupportedEncodingException e) {
			log.error("System Error!",e);
		}
		return value;
	}
}
