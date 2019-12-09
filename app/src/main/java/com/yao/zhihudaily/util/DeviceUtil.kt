package com.yao.zhihudaily.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import java.net.NetworkInterface

/**
 * @author Yao
 * @date 2016/10/3
 */
object DeviceUtil {

    private val TAG = "DeviceUtil"

    private var MAC_ADDRESS: String? = null
    private lateinit var DEVICE_ID: String

    var SCREEN_WIDTH = 0
    var SCREEN_HEIGHT = 0
    var DENSITY = 0f
    var STATUS_BAR_HEIGHT = 0

    @SuppressLint("StaticFieldLeak")
    private lateinit var sContext: Context

    val allDeviceInfo: String
        get() {
            val deviceInfo = ("Brand:" + android.os.Build.BRAND
                    + " MODEL:" + android.os.Build.MODEL
                    + " SDK:" + android.os.Build.VERSION.RELEASE
                    + "(" + android.os.Build.VERSION.SDK_INT + ")")
            Log.i(TAG, "DeviceUtil.java - getAllDeviceInfo() ---------- $deviceInfo")
            return deviceInfo
        }

    val macAddress: String?
        @SuppressLint("HardwareIds")
        get() {
            if (!TextUtils.isEmpty(MAC_ADDRESS)) {
                return MAC_ADDRESS
            }
            val wifi = sContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    ?: return MAC_ADDRESS
            val info = wifi.connectionInfo ?: return MAC_ADDRESS
            MAC_ADDRESS = info.macAddress
            if ("02:00:00:00:00:00" == MAC_ADDRESS) {
                try {
                    val interfaces = NetworkInterface.getNetworkInterfaces()
                    while (interfaces.hasMoreElements()) {
                        val iter = interfaces.nextElement()
                        if ("wlan0" == iter.name) {
                            val addr = iter.hardwareAddress
                            if (addr == null || addr.size == 0) {
                                continue
                            }
                            val buf = StringBuilder()
                            for (b in addr) {
                                buf.append(String.format("%02X:", b))
                            }
                            if (buf.length > 0) {
                                buf.deleteCharAt(buf.length - 1)
                            }
                            MAC_ADDRESS = buf.toString()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return MAC_ADDRESS
        }

    val deviceId: String
        get() {
            if (DEVICE_ID == null) {
                DEVICE_ID = macAddress!!.replace(":", "")
            }
            return DEVICE_ID
        }

    val serialNumber: String
        @SuppressLint("HardwareIds")
        get() {
            var serialNumber = android.os.Build.SERIAL
            if (!TextUtils.isEmpty(serialNumber)) {
                return serialNumber
            }
            try {
                @SuppressLint("PrivateApi")
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)
                serialNumber = get.invoke(c, "ro.serialno") as String
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return serialNumber

        }

    fun init(ctx: Context) {
        sContext = ctx
        initScreenSize(ctx)
    }

    fun initScreenSize(context: Context) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)

        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels
        DENSITY = dm.density
        if (screenWidth > 0) {
            if (screenWidth > screenHeight) {
                SCREEN_WIDTH = screenWidth
                SCREEN_HEIGHT = screenHeight
            } else {
                SCREEN_WIDTH = screenHeight
                SCREEN_HEIGHT = screenWidth
            }
        }

        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            STATUS_BAR_HEIGHT = context.resources.getDimensionPixelSize(resourceId)
            //如果取不到就取个保底值25dp
            if (STATUS_BAR_HEIGHT == 0) {
                STATUS_BAR_HEIGHT = (DENSITY * 25).toInt()
            }
        }
    }

}
