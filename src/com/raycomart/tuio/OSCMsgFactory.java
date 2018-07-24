package com.raycomart.tuio;

import java.util.List;

import com.illposed.osc.OSCMessage;
import com.leapmotion.leap.Hand;
import com.raycomart.Constants;

/**
* @author Zhouluning
* @version CreateTime：2018年7月20日 下午7:53:34
* 
* OSC消息工厂类
*/

public class OSCMsgFactory {
	
	/*
	 * 手掌类型定义
	 */
	public static final Integer LEFT_HAND = 1;
	public static final Integer RIGHT_HAND = 2;
	
	/*
	 * 消息类型定义
	 */
	public static final String MSG_TYPE_2DCUR = "2Dcur";		// 2D指针
	public static final String MSG_TYPE_2DOBJ = "2Dobj";		// 2D对象
	public static final String MSG_TYPE_2DBLOB = "2Dblb";		// 2DBLOB
	
	public static final String MSG_TYPE_3DCUR = "3Dcur";		// 3D指针
	public static final String MSG_TYPE_3DOBJ = "3Dobj";		// 3D对象
	public static final String MSG_TYPE_3DBLOB = "3Dblb";		// 3DBLOB
	
	private static String MSG_TYPE = null;
	
	private OSCMsgFactory() {
	}
	
	/**
	 * 实例化方法
	 * @param msgType
	 * @return
	 */
	public static OSCMsgFactory getInstance(String msgType) {
		MSG_TYPE = msgType;
		return new OSCMsgFactory();
	}
	
	/**
	 * 生成source消息
	 * @param email
	 * @return
	 */
	public OSCMessage genSoucreMsg(String email) {
		OSCMessage objMsg = new OSCMessage("/tuio/" + MSG_TYPE);
		objMsg.addArgument("source");
		objMsg.addArgument(email);
		
		return objMsg;
	}
	
	/**
	 * 生成alive消息
	 * @param listSid
	 * @return
	 */
	public OSCMessage genAliveMsg(List<Integer> listSid) {
		
		OSCMessage objMsg = new OSCMessage("/tuio/" + MSG_TYPE);
		objMsg.addArgument("alive");

		// 参数SID列表不为空，写入sessionid
		if(listSid != null) {
			for (Integer intSid : listSid) {
				objMsg.addArgument(intSid);
			}
		}
		return objMsg;
	}
	
	/**
	 * 生成fseq消息
	 * @param fid
	 * @return
	 */
	public OSCMessage genFseqMsg(Long fid) {
		OSCMessage objMsg = new OSCMessage("/tuio/" + MSG_TYPE);
		objMsg.addArgument("fseq");

		// frameid为空，默认写入-1
		if(fid == null) {
			objMsg.addArgument(-1);
		}
		else {
			objMsg.addArgument(Integer.parseInt(fid.toString()));
		}
		return objMsg;
	}

	/**
	 * 根据Leap手掌对象生成TUIO指针消息
	 * 
	 * /tuio/3Dcur set s x y z X Y Z m
	 * 
	 * @param hand
	 * @return
	 */
	public OSCMessage genSetMsg(Hand hand) {
		
		try {
			// 获取手掌类型
			Integer handType = hand.isLeft() ? LEFT_HAND : RIGHT_HAND;
			
			// 获取手掌坐标，单位(mm)
			float realX = hand.palmPosition().getX();
			float realY = hand.palmPosition().getY();
			float realZ = hand.palmPosition().getZ();
			
			// 根据Leap作用区域设定，映射为TUIO坐标
			float posX = mapCoordinateX(realX);
			float posY = mapCoordinateY(realY);
			float posZ = mapCoordinateZ(realZ);
				
			OSCMessage objMsg = new OSCMessage("/tuio/" + MSG_TYPE);
			objMsg.addArgument("set");
			
			objMsg.addArgument(hand.id());
			objMsg.addArgument(posX);	// x
			objMsg.addArgument(posY);	// y
//			objMsg.addArgument(posZ);	// z
			objMsg.addArgument(0f);		// X
			objMsg.addArgument(0f);		// Y
//			objMsg.addArgument(0f);		// Z
			objMsg.addArgument(0f);		// m
			
			return objMsg;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/**
	 * 转换LeapMotion X轴坐标至TUIO坐标
	 * @param pos
	 * @return
	 */
	private float mapCoordinateX(float pos) {
		return (float)((pos - Constants.X_AREA_MIN)/(Constants.X_AREA_MAX - Constants.X_AREA_MIN));
	}

	/**
	 * 转换LeapMotion Y轴坐标至TUIO坐标
	 * @param pos
	 * @return
	 */
	private float mapCoordinateY(float pos) {
		return (float)(1-(pos - Constants.Y_AREA_MIN)/(Constants.Y_AREA_MAX - Constants.Y_AREA_MIN));
	}

	/**
	 * 转换LeapMotion Z轴坐标至TUIO坐标
	 * @param pos
	 * @return
	 */
	private float mapCoordinateZ(float pos) {
		return (float)((pos - Constants.Z_AREA_MIN)/(Constants.Z_AREA_MAX - Constants.Z_AREA_MIN));
	}
}
