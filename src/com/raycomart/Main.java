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
	
	private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	public static void main(String[] args) {
		
		// FIXME 此处增加读取配置文件
		Constants.X_AREA_MIN = -133.33f;
		Constants.X_AREA_MAX = 133.33f;
		
		Constants.Y_AREA_MIN = 100f;
		Constants.Y_AREA_MAX = 300f;
		
		Constants.Z_AREA_MIN = -100f;
		Constants.Z_AREA_MAX = 100f;
		
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
