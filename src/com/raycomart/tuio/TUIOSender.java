package com.raycomart.tuio;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.raycomart.Constants;
import com.raycomart.leap.MsgSender;

/**
* @author Zhouluning
* @version CreateTime：2018年7月25日 上午11:21:35
* 
* TUIO消息发送类
*/

public class TUIOSender extends MsgSender {
	
	private Logger log = LogManager.getLogger();
	
	private static OSCPortOut oscPort;
	
	public TUIOSender() {
		try {
        	String host = Constants.get("clientHost").trim();
        	int port = Integer.parseInt(Constants.get("clientPort").trim());
        	log.info("发送TUIO消息至 " + host + ":" + port);
			oscPort = new OSCPortOut(java.net.InetAddress.getByName(host), port);
		} catch (Exception e) {
			e.printStackTrace();
			oscPort = null;
		}
	}

	@Override
	public void send(long frameId, List<Hand> hands, List<Gesture> gestures) {
		try {
			OSCBundle oscBundle = new OSCBundle();
			
			// TODO 此处需要优化，如何确定消息类型
			OSCMsgFactory msgFactory = OSCMsgFactory.getInstance(OSCMsgFactory.MSG_TYPE_2DCUR);
			
			OSCMessage aliveMsg = msgFactory.genAliveMsg(null);	// 生成alive消息
	        oscBundle.addPacket(aliveMsg);
	        
	        // 遍历手部信息生成set消息
	        if(hands != null) {
	        	for (Hand hand : hands) {
	        		// 写入alive消息中sessionid
	                aliveMsg.addArgument(hand.id());
	                
	                // 生成手掌set消息
	                OSCMessage setMsg = msgFactory.genSetMsg(hand);
	                oscBundle.addPacket(setMsg);
				}
	        }
	        
	        // 遍历手势信息生成set消息
	        if(gestures != null) {
	        	for (Gesture gesture : gestures) {
					
				}
	        }
	        
	        OSCMessage frameMsg = msgFactory.genFseqMsg(frameId);

			oscBundle.addPacket(frameMsg);
			
			sendOSC(oscBundle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 发送TUIO报文
	 * 
	 * @param packet
	 */
	private void sendOSC(OSCBundle packet) {
		try {
			
			if(packet.getPackets() != null && packet.getPackets().length > 0) {
				for (int i = 0; i < packet.getPackets().length; i++) {
					OSCMessage oneMsg = (OSCMessage)packet.getPackets()[i];
					StringBuffer sbPackets = new StringBuffer();
					sbPackets.append(oneMsg.getAddress() + " ");
					for (int j = 0; j < oneMsg.getArguments().length; j++) {
						Object arg = oneMsg.getArguments()[j];
						sbPackets.append(arg + " ");
					}
					log.info(sbPackets.toString());
				}
			}
			
			oscPort.send(packet);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
