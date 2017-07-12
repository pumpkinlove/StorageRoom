package com.device;


/**
 * <p>Description:指纹操作采集，指纹比对、RFID读取</p>
 * @version 1.2.6
 */
public class Device {
	static {
		try {
			System.loadLibrary("Device");
		} catch (UnsatisfiedLinkError e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 * <p> Description:获取指纹图片，用于注册指纹</p>
	 * @param  timeout 超时时间
	 * @param  finger 指纹图片数据， 长度[2000+152*200]
	 * @param  message 错误信息，[100]
	 * @return int 返回为0获取指纹图像成功；其他获取指纹图像失败
	 */
	public native static int getImage(int timeout, byte[] finger, byte[] message);

	/**
	 * 
	 * <p>Description: 获取指纹特征，用于比对指纹</p>
	 * @param timeout 超时时间
	 * @param finger 指纹特征 长度， [256]
	 * @param message 错误信息，[100]
	 * @return int 返回为0获取指纹特征成功；其他获取指纹特征失败
	 */
	public native static int getFinger(int timeout, byte[] finger,
			byte[] message);

	/**
	 * 
	 * <p>Description: 将指纹图像转成特征，用于注册指纹时校验指纹</p>
	 * @param image 指纹图片 ，[2000+152*200]
	 * @param feature 指纹特征， 长度[256]
	 * @param message 错误信息[100]
	 * @return int 返回为0成功；其他失败
	 */
	public native static int ImageToFeature(byte[] image, byte[] feature,
			byte[] message);

	/**
	 * 
	 * <p>Description: 特征转成模板，用于注册指纹</p>
	 * @param tz1 第一个指纹特征 ，长度[256]
	 * @param tz2 第二个指纹特征，长度[256]
	 * @param tz3 第三个指纹特征，长度[256]
	 * @param mb 指纹模板，长度[256]
	 * @param message 错误信息，长度[100]
	 * @return int 返回为0成功；其他失败
	 */
	public native static int FeatureToTemp(byte[] tz1, byte[] tz2, byte[] tz3,
			byte[] mb, byte[] message);

	/**
	 * 
	 * <p>Description: 指纹比对</p>
	 * @param mbFinger 指纹模板，长度[256]
	 * @param tzFinger 指纹特征，长度[256]
	 * @param level 默认为3
	 * @return int 返回为0成功；其他失败
	 */
	public native static int verifyFinger(String mbFinger, String tzFinger,
			int level);

	/**
	 * 
	 * <p>Description: 用于校验指纹</p>
	 * @param mbFinger 指纹模板，长度[256]
	 * @param tzFinger 指纹特征，长度[256]
	 * @param level 默认为3
	 * @return int 返回为0成功；其他失败
	 */
	public native static int verifyBinFinger(byte[] mbFinger, byte[] tzFinger,
			int level);
	/**
	 * <p>Description: 获取rfid</p>
	 * @param timeout 超时时间
	 * @param tid 超高频卡tid[200]
	 * @param epcid 超高频卡epcid[200]
	* @param message 错误信息，长度[100]
	 * @return int 返回为0成功；其他失败
	 */
	public native static int getRfid(int timeout,byte[] tid,  byte[] epcid, byte[] message);
	
	/**
	 * <p>Description: 取消</p>
	 * @return int 返回为0成功；其他失败
	 */
	public native static int cancel();
	/**
	 * <p>Description: 打开rfid</p>
	 * @return int 返回为0成功；其他失败
	 */
	public native static int openRfid();
	/**
	 * <p>Description: 关闭rfid</p>
	 * @return int 返回为0成功；其他失败
	 */
	public native static int closeRfid();

	/**
	 * <p>Description: 打开指纹</p>
	 * @return int 返回为0成功；其他失败
	 */
	public native static int openFinger();

	/**
	 * <p>Description: 关闭指纹</p>
	 * @return int 返回为0成功；其他失败
	 */
	public native static int closeFinger();
}
