package com.raycomart.leap;

import com.raycomart.tuio.TUIOSender;

/**
* @author Zhouluning
* @version CreateTime：2018年7月25日 上午11:01:18
* 
* 消息发送类工厂类
*
*/

public class MsgSenderFactory {
	
	private static MsgSender sender;

	/**
	 * 创建消息发送类
	 * 
	 * @param sendType
	 * @return
	 */
	public static MsgSender createSender() {
		
		// FIXME 此处增加读取配置文件
		String senderType = "TUIO";
		
		// 由于发送类型是由配置文件确定，所以一次初始化后不需要再进行初始化
		if(sender == null) {
			// 根据发送类型实例化不同的实现类
			switch (senderType) {
				case "TUIO":	// TUIO
					System.out.println("SENDER TYPE IS [TUIO]!");
					sender = new TUIOSender();
					break;
					
				case "UPD":		// UDP
					System.out.println("SENDER TYPE IS [UPD]!");
					
					break;
		
				default:
					break;
			}
		}
		
		return sender;
	}
}
