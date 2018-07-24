package com.raycomart.leap;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Controller.PolicyFlag;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.raycomart.Constants;
import com.raycomart.tuio.OSCMsgFactory;

/**
 * Leap 监听服务
 * 
 * @author Fenix
 *
 */
public class LeapListener extends Listener {
	
	private OSCPortOut oscPort;

	/**
	 * LeapMotion连接初始化
	 */
	@Override
	public void onConnect(Controller controller) {
		System.out.println("LeapMotion已连接...");
		
		// 激活手势
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);		// 横扫
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);		// 画圆
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);	// 点击屏幕
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);	// 代表手指点击键的动作。手指需要上下移动
        
        // 设置策略
        controller.setPolicyFlags(PolicyFlag.POLICY_BACKGROUND_FRAMES);		// 程序在后台运行时leap motion依然获取数据信息。在leap motion的控制面板中也要将“允许后台应用程序”的设置开启
        
        try {
        	// FIXME 此处改为读取配置文件，ip/端口
        	String host = "127.0.0.1";
        	int port = 3333;
			oscPort = new OSCPortOut(java.net.InetAddress.getByName(host), port);
		} catch (Exception e) {
			oscPort = null;
		}
	}

	/**
	 * 获取一帧数据时处理方法
	 */
	@Override
	public void onFrame(Controller controller) {
		
		// 获取一帧数据
        Frame frame = controller.frame();
        
        if (frame.hands().isEmpty() && frame.gestures().isEmpty()) {
            System.out.print(".");
            // FIXME 此处修改为使用配置文件
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            return;
        }
        
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());
        
        OSCBundle oscBundle = new OSCBundle();
        
        // TODO 此处需要优化，如何确定消息类型
        OSCMsgFactory msgFactory = OSCMsgFactory.getInstance(OSCMsgFactory.MSG_TYPE_2DCUR);
        
        OSCMessage aliveMsg = msgFactory.genAliveMsg(null);	// 生成alive消息
        oscBundle.addPacket(aliveMsg);

        // 获取手部信息，遍历
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "左手" : "右手";
            float posX = hand.palmPosition().getX();
            float posY = hand.palmPosition().getY();
            float posZ = hand.palmPosition().getZ();
            
            System.out.println("[" + hand.id() + "]" +handType 
                             + " 手掌位置:(" + posX  + "mm "
                             + posY + "mm "
                             + posZ + "mm)");
            
            // 判断是否超出区域
            if( (posX < Constants.X_AREA_MIN && posX > Constants.X_AREA_MAX)	// X轴检查
            		|| (posY < Constants.Y_AREA_MIN && posY > Constants.Y_AREA_MAX)
            		|| (posZ < Constants.Z_AREA_MIN && posZ > Constants.Z_AREA_MAX)) {
            	// 超出作用区域，抛弃数据
            	System.out.println("[" + hand.id() + "]" +handType + " 超出作用区域，忽略...");
            	continue;
            }
            
            // 写入alive消息中sessionid
            aliveMsg.addArgument(hand.id());
            
            // 生成手掌set消息
            OSCMessage setMsg = msgFactory.genSetMsg(hand);
            oscBundle.addPacket(setMsg);
            // 获取手掌法向量和手掌的方向
           /* Vector normal = hand.palmNormal();
            Vector direction = hand.direction();*/

            // 计算手掌的 俯仰,横滚, 航向角度
           /* System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");*/

            // 获取手臂信息
            /*Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());*/

            // 获取手指信息
            /*for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                }
            }*/
        }

        // 获取工具信息
       /* for(Tool tool : frame.tools()) {
            System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
        }*/

        // 获取手势
        GestureList gestures = frame.gestures();
        /*for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }*/

        OSCMessage frameMsg = msgFactory.genFseqMsg(frame.id());

		oscBundle.addPacket(frameMsg);

		sendOSC(oscBundle);
	}
	
	/**
	 * 发送TUIO报文
	 * 
	 * @param packet
	 */
	private void sendOSC(OSCPacket packet) {
		try {
			oscPort.send(packet);
		} catch (java.io.IOException e) {
		}
	}
	
	@Override
	public void onServiceConnect(Controller arg0) {
		System.out.println("LeapMotion服务启动...");
	}

	@Override
	public void onServiceDisconnect(Controller arg0) {
		System.out.println("LeapMotion服务关闭...");
	}
	
	@Override
	public void onDeviceChange(Controller arg0) {
		System.out.println("LeapMotion设备状态改变...");
	}

	@Override
	public void onDisconnect(Controller arg0) {
		System.out.println("LeapMotion已断开...");
	}

	@Override
	public void onExit(Controller arg0) {
		System.out.println("LeapMotion退出...");
	}

	@Override
	public void onFocusLost(Controller arg0) {
		System.out.println("LeapMotion程序失去焦点...");
	}

	@Override
	public void onInit(Controller arg0) {
		System.out.println("LeapMotion设备初始化...");
	}
}
