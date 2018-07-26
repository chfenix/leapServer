package com.raycomart;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leapmotion.leap.Controller;
import com.raycomart.leap.LeapListener;

/**
 * Jar包入口类
 * 
 * @author Zhouluning
 *
 */
public class Main {
	
	private static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		// 载入配置文件
		Constants.initProperties();
		
		Constants.X_AREA_MIN = Float.parseFloat(Constants.get("XMin").trim());
		Constants.X_AREA_MAX = Float.parseFloat(Constants.get("XMax").trim());
		
		Constants.Y_AREA_MIN = Float.parseFloat(Constants.get("YMin").trim());
		Constants.Y_AREA_MAX = Float.parseFloat(Constants.get("YMax").trim());
		
		Constants.Z_AREA_MIN = Float.parseFloat(Constants.get("ZMin").trim());
		Constants.Z_AREA_MAX = Float.parseFloat(Constants.get("ZMax").trim());
		
		// 启动LeapMotion监听
		startLeapListener();
	}
	
	
	/**
	 * 启动LeapSDK监听
	 */
	private static void startLeapListener() {
		
        LeapListener listener = new LeapListener();
        Controller controller = new Controller();
        controller.addListener(listener);

        // FIXME 此处修改为后台服务
        log.info("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
	}
}
