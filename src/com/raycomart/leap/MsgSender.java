package com.raycomart.leap;

import java.util.List;

import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;

/**
* @author Zhouluning
* @version CreateTime：2018年7月25日 上午10:32:21
* 
* 发送消息抽象类
*/

public abstract class MsgSender {

	/**
	 * 发送消息
	 * 
	 * @param frameId		frameId
	 * @param hands			手对象列表
	 * @param gestures		手势对象列表
	 */
	public abstract void send(long frameId,List<Hand> hands,List<Gesture> gestures);
}
