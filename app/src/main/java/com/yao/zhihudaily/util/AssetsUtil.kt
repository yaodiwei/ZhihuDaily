package com.yao.zhihudaily.util

import com.yao.zhihudaily.util.ResUtil.getResources
import java.io.IOException
import java.nio.charset.StandardCharsets

class AssetsUtil {

    fun getString(fileName: String): String? {
        try {
            val inputStream = getResources().assets.open(fileName)
            val length = inputStream.available()
            val buffer = ByteArray(length)
            inputStream.read(buffer)
            return String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}