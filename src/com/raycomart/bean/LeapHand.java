package com.raycomart.bean;

/**
* @author Zhouluning
* @version CreateTime：2018年7月26日 下午12:04:46
* LeapMotion手对象
*/

public class LeapHand {
	
	private Integer handId;

	/*
	 *  手部类型（左手右手）
	 */
	private String type;
	
	/*
	 * 手掌坐标
	 */
	private float palmX;
	private float palmY;
	private float palmZ;
	

	public Integer getHandId() {
		return handId;
	}

	public void setHandId(Integer handId) {
		this.handId = handId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPalmX() {
		return palmX;
	}

	public void setPalmX(float palmX) {
		this.palmX = palmX;
	}

	public float getPalmY() {
		return palmY;
	}

	public void setPalmY(float palmY) {
		this.palmY = palmY;
	}

	public float getPalmZ() {
		return palmZ;
	}

	public void setPalmZ(float palmZ) {
		this.palmZ = palmZ;
	}
	
	
}
