package com.raycomart;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
		
		// LeapMotion作用区域
		Constants.X_AREA_MIN = Float.parseFloat(Constants.get("XMin").trim());
		Constants.X_AREA_MAX = Float.parseFloat(Constants.get("XMax").trim());
		
		Constants.Y_AREA_MIN = Float.parseFloat(Constants.get("YMin").trim());
		Constants.Y_AREA_MAX = Float.parseFloat(Constants.get("YMax").trim());
		
		Constants.Z_AREA_MIN = Float.parseFloat(Constants.get("ZMin").trim());
		Constants.Z_AREA_MAX = Float.parseFloat(Constants.get("ZMax").trim());
		
		// 是否转换坐标
		Constants.MAP_COORDINATE = new Boolean(Constants.get("mapCoordinate"));
		
		Timer timer = new Timer();
		timer.schedule(new TimerAlive(), 10 * 1000, 60 * 1000);
		
		// 启动LeapMotion监听，必须放到最后执行
		startLeapListener();
	}
	
	
	/**
	 * 启动LeapSDK监听
	 */
	private static void startLeapListener() {
		
        LeapListener listener = new LeapListener();
        Controller controller = new Controller();
        controller.addListener(listener);

        // 检测输入，保持程序
        log.info("输入EXIT 关闭服务");
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()) {
            String in = scan.next().toString();
            if("EXIT".equals(in.toUpperCase())) {
            	log.info("LeapServer已关闭!");
            	scan.close();
                break;
            }
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
	}
	
	static class TimerAlive extends TimerTask {
	    @Override
	    public void run() {
	        log.info("LeapServer is alive!");
	    }
	}
}
