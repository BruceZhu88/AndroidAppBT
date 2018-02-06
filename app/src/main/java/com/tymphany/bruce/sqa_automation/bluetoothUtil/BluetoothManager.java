/**
 * Bluetooth Manager
 * 
 * @author Bruce.Zhu
 * 
 */
package com.tymphany.bruce.sqa_automation.bluetoothUtil;
import java.lang.reflect.Method;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BluetoothManager {
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	/**
	 * Whether if Current Android device support Bluetooth
	 * 
	 * @return true��Support Bluetooth  false��No support Bluetooth
	 */
	public static boolean isBluetoothSupported()
	{
		return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
	}

	/**
	 * Whether if Android device's bluetooth was opened
	 * 
	 * @return true��Bluetooth opened false��Bluetooth closed
	 */
	public static boolean isBluetoothEnabled()
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.isEnabled();
		}

		return false;
	}

	/**
	 * Force to open currentAndroid device's Bluetooth
	 * 
	 * @return true��Success��false��Fail
	 */
	public static boolean turnOnBluetooth()
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.enable();
		}

		return false;
	}
	
	/**
	 * Force to close current Android device's Bluetooth
	 * 
	 * @return  true��Success��false��Fail
	 */
	public static boolean turnOffBluetooth()
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.disable();
		}

		return false;
	}

	public String scanBTlist(String name) {
		if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
			bluetoothAdapter.enable();
		}
		if (bluetoothAdapter.isDiscovering())
			bluetoothAdapter.cancelDiscovery();
		Object[] lstDevice = bluetoothAdapter.getBondedDevices().toArray();
		for (int i = 0; i < lstDevice.length; i++) {
			BluetoothDevice device = (BluetoothDevice) lstDevice[i];
			Log.d(BluetoothTools.TAG, "bt device name = "+device.getName());
			if (device.getName().equals(name)) {
				String address = device.getAddress();
				return address;
			}
		}
		return "";
	}

	public boolean isBTConnected() {
		//adapter也有getState(), 可获取ON/OFF...其它状态
		int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);              //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
		int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);        //蓝牙头戴式耳机，支持语音输入输出
		int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
		return bluetoothAdapter != null && (a2dp == BluetoothAdapter.STATE_CONNECTED ||
				headset == BluetoothAdapter.STATE_CONNECTED ||
				health == BluetoothAdapter.STATE_CONNECTED);
	}




}