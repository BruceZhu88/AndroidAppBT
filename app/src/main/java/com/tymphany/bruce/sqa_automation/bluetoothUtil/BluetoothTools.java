package com.tymphany.bruce.sqa_automation.bluetoothUtil;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * 蓝牙工具类
 */
public class BluetoothTools {
	private static BluetoothAdapter adapter = BluetoothAdapter
			.getDefaultAdapter();

	public static final String TAG = "Bruce debug";


	/**
	 * 本程序所使用的UUID
	 */
	public static final UUID PRIVATE_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");//0000180A-0000-1000-8000-00805F9B34FB

	/**
	 * 字符串常量，存放在Intent中的设备对象
	 */
	public static final String DEVICE = "DEVICE";

	/**
	 * 字符串常量，服务器所在设备列表中的位置
	 */
	public static final String SERVER_INDEX = "SERVER_INDEX";

	/**
	 * 字符串常量，Intent中的数据
	 */
	public static final String DATA = "DATA";

	/**
	 * Action类型标识符，Action类型 为读到数据
	 */
	public static final String ACTION_READ_DATA = "ACTION_READ_DATA";

	/**
	 * Action类型标识符，Action类型为 未发现设备
	 */
	public static final String ACTION_NOT_FOUND_SERVER = "ACTION_NOT_FOUND_DEVICE";

	/**
	 * Action类型标识符，Action类型为 开始搜索设备
	 */
	public static final String ACTION_START_DISCOVERY = "ACTION_START_DISCOVERY";

	/**
	 * Action：设备列表
	 */
	public static final String ACTION_FOUND_DEVICE = "ACTION_FOUND_DEVICE";

	/**
	 * Action：选择的用于连接的设备
	 */
	public static final String ACTION_SELECTED_DEVICE = "ACTION_SELECTED_DEVICE";

	/**
	 * Action：开启服务器
	 */
	public static final String ACTION_START_SERVER = "ACTION_STARRT_SERVER";

	/**
	 * Action：关闭后台Service
	 */
	public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";

	/**
	 * Action：到Service的数据
	 */
	public static final String ACTION_DATA_TO_SERVICE = "ACTION_DATA_TO_SERVICE";

	/**
	 * Action：到游戏业务中的数据
	 */
	public static final String ACTION_DATA_TO_GAME = "ACTION_DATA_TO_GAME";

	/**
	 * Action：连接成功
	 */
	public static final String ACTION_CONNECT_SUCCESS = "ACTION_CONNECT_SUCCESS";

	/**
	 * Action：连接错误
	 */
	public static final String ACTION_CONNECT_ERROR = "ACTION_CONNECT_ERROR";

	/**
	 * Message类型标识符，连接成功
	 */
	public static final int MESSAGE_CONNECT_SUCCESS = 0x00000002;

	/**
	 * Message：连接失败
	 */
	public static final int MESSAGE_CONNECT_ERROR = 0x00000003;

	/**
	 * Message：读取到一个对象
	 */
	public static final int MESSAGE_READ_OBJECT = 0x00000004;

	/**
	 * 打开蓝牙功能
	 */
	public static void openBluetooth() {
		adapter.enable();
	}

	/**
	 * 关闭蓝牙功能
	 */
	public static void closeBluetooth() {
		adapter.disable();
	}

	/**
	 * 设置蓝牙发现功能
	 *
	 * @param duration
	 *            设置蓝牙发现功能打开持续秒数（值为0至300之间的整数）
	 */
	public static void openDiscovery(int duration) {
		if (duration <= 0 || duration > 300) {
			duration = 200;
		}
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
	}

	/**
	 * 停止蓝牙搜索
	 */
	public static void stopDiscovery() {
		adapter.cancelDiscovery();
	}

	//下面的蓝牙地址里面放要连接的两台手机的蓝牙地址，为 了保证安装了该程序的手机都能做服务端使用，所以放两个地址。
	//这两个地址在BluetoothClientService类中的discoveryReceiver中使用
	//地址的作用是在一台手机作为客户端搜索到匹配地址的服务端时，自动配对并连接
	/**
	 * 要连接的蓝牙地址
	 */
	public static final String BluetoothAddress = "50:1E:2D:00:82:0F"; //50:1E:2D:00:82:0E   50:1E:2D:00:10:2F
	/**
	 * 要连接的蓝牙地址2
	 */
	public static final String BluetoothAddress2="28:C2:DD:A1:A1:72";
}
