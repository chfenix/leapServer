package com.raycomart;

/**
* @author Zhouluning
* @version CreateTime：2018年7月26日 下午12:38:16
* 
* 工具类
*/

public class Utils {

	/**
	 * 转换LeapMotion X轴坐标至0-1
	 * @param pos
	 * @return
	 */
	public static float mapCoordinateX(float pos) {
		return (float)((pos - Constants.X_AREA_MIN)/(Constants.X_AREA_MAX - Constants.X_AREA_MIN));
	}

	/**
	 * 转换LeapMotion Y轴坐标至0-1
	 * @param pos
	 * @return
	 */
	public static float mapCoordinateY(float pos) {
		return (float)(1-(pos - Constants.Y_AREA_MIN)/(Constants.Y_AREA_MAX - Constants.Y_AREA_MIN));
	}

	/**
	 * 转换LeapMotion Z轴坐标至0-1
	 * @param pos
	 * @return
	 */
	public static float mapCoordinateZ(float pos) {
		return (float)((pos - Constants.Z_AREA_MIN)/(Constants.Z_AREA_MAX - Constants.Z_AREA_MIN));
	}
}
