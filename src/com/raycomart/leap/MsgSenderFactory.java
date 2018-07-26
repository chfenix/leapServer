package com.raycomart.leap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.raycomart.Constants;
import com.raycomart.tuio.TUIOSender;
import com.raycomart.udp.UDPSender;

/**
* @author Zhouluning
* @version CreateTime：2018年7月25日 上午11:01:18
* 
* 消息发送类工厂类
*
*/

public class MsgSenderFactory {
	
	private static Logger log = LogManager.getLogger();
	
	private static MsgSender sender;

	/**
	 * 创建消息发送类
	 * 
	 * @param sendType
	 * @return
	 */
	public static MsgSender createSender() {
		
		// 读取服务类型
		String msgType = Constants.get("messageType").toUpperCase();
		
		// 由于发送类型是由配置文件确定，所以一次初始化后不需要再进行初始化
		if(sender == null) {
			// 根据发送类型实例化不同的实现类
			log.info("sender message type is [" + msgType + "]!");
			switch (msgType) {
				case "TUIO":	// TUIO
					
					sender = new TUIOSender();
					break;
					
				case "UDP":		// UDP
					sender = new UDPSender();
					break;
		
				default:
					log.error("Unsupport message type!");
					break;
			}
		}
		
		return sender;
	}
}
