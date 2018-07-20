package com.raycomart.leap;

import java.util.Random;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

public class LeapListener extends Listener {

	@Override
	public void onServiceConnect(Controller arg0) {
		System.out.println("服务启动");
	}

	@Override
	public void onServiceDisconnect(Controller arg0) {
		System.out.println("服务关闭");
	}

	@Override
	public void onConnect(Controller controller) {
		System.out.println("LeapMotion已连接...");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	@Override
	public void onDeviceChange(Controller arg0) {
		super.onDeviceChange(arg0);
	}

	@Override
	public void onDisconnect(Controller arg0) {
		System.out.println("LeapMotion已断开...");
	}

	@Override
	public void onExit(Controller arg0) {
		super.onExit(arg0);
	}

	@Override
	public void onFocusLost(Controller arg0) {
		System.out.println("程序失去焦点");
	}

	float testx = 0.5f;
	float testy = 0.5f;
	
	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		// Get hands
		for (Hand hand : frame.hands()) {
			String handType = hand.isLeft() ? "左手" : "右手";
			
			float posX = hand.palmPosition().getX(); 
			float posY = hand.palmPosition().getY();
			float posZ = hand.palmPosition().getZ();
			
			System.out.println("[" + hand.id() + "]" + handType + " 手掌位置：" 
			+"(X:" + posX
			+ " Y:" + posY
			+ " Z:" + posZ + ")");
			
			
		}

		if (frame.hands().isEmpty() ) {
			System.out.println("未检测到物体");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInit(Controller arg0) {
		System.out.println("LeapMotion设备初始化...");
	}

	
	public static double MysigMoid(double value) {
        //Math.E=e;Math.Pow(a,b)=a^b
        double ey = Math.pow(Math.E, -value);
        double result = 1 / (1 + ey);
        return result;
    }
}
