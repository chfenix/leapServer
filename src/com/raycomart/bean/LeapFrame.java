package com.raycomart.bean;

import java.util.ArrayList;
import java.util.List;

/**
* @author Zhouluning
* @version CreateTime：2018年7月26日 下午12:01:01
* 
* Frame
*/

public class LeapFrame {

	private Long frameId;
	
	private List<LeapHand> hands;
	
	public LeapFrame(Long frameId) {
		this.frameId = frameId;
	}

	public Long getFrameId() {
		return frameId;
	}

	public void setFrameid(Long frameId) {
		this.frameId = frameId;
	}

	public void addHand(LeapHand hand) {
		if(hands == null) {
			hands = new ArrayList<LeapHand>();
		}
		hands.add(hand);
	}
	
	
	
}
