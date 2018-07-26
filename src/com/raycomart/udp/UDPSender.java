package com.raycomart.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.raycomart.Constants;
import com.raycomart.Utils;
import com.raycomart.bean.LeapFrame;
import com.raycomart.bean.LeapHand;
import com.raycomart.leap.MsgSender;

/**
* @author Zhouluning
* @version CreateTime：2018年7月26日 上午11:16:07
* 
* UDP报文发送类
*/

public class UDPSender extends MsgSender {
	
	private Logger log = LogManager.getLogger();
	
	private String host;
	private int port;
	
	public UDPSender() {
		host = Constants.get("clientHost").trim();
    	port = Integer.parseInt(Constants.get("clientPort").trim());
    	log.info("发送UDP消息至 " + host + ":" + port);
	}

	@Override
	public void send(long frameId, List<Hand> hands, List<Gesture> gestures) {
		try {
			// 实例化Frame对象
			LeapFrame leapFrame = new LeapFrame(frameId);
			
			if(hands != null) {
				// 遍历手部信息，生成报文对象
				for (Hand hand : hands) {
					LeapHand leapHand = new LeapHand();
					leapHand.setHandId(hand.id());
					// 手部类型
					String handType = hand.isLeft() ? Constants.HAND_TYPE_LEFT : Constants.HAND_TYPE_RIGHT;
					leapHand.setType(handType);
					
					// 手掌坐标
					float posX = hand.palmPosition().getX();
					float posY = hand.palmPosition().getY();
					float posZ = hand.palmPosition().getZ();
					
					if(Constants.MAP_COORDINATE) {
						// 需要进行坐标转换
						posX = Utils.mapCoordinateX(posX);
						posY = Utils.mapCoordinateY(posY);
						posZ = Utils.mapCoordinateZ(posZ);
					}
					
					leapHand.setPalmX(posX);
					leapHand.setPalmY(posY);
					leapHand.setPalmZ(posZ);
					
					leapFrame.addHand(leapHand);
				}
			}
			
			 // 遍历手势信息生成set消息
	        if(gestures != null) {
	        	for (Gesture gesture : gestures) {
					
				}
	        }
	        
	        // 生成Json报文
	        Gson gson = new Gson();
	        String strData = gson.toJson(leapFrame);
	        log.info(strData);
	        
	        sendUdp(strData);	// 发送UDP数据
	        
			
		} catch (Exception e) {
			// FIXME 所有exception改成使用log输出
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送UDP数据
	 * @param data
	 */
	private void sendUdp(String data) {
        try {
        	//1、创建socket对象，通过DatagramSocket对象
			DatagramSocket ds = new DatagramSocket();

			//2、确定数据，并封装成数据包
			byte[]  byteData = data.getBytes();
			DatagramPacket dp = new DatagramPacket(byteData, byteData.length, InetAddress.getByName(host), port);

			//3、通过scoket服务，将已有的数据发送出去，通过send方法
			ds.send(dp);
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
