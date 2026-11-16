package com.pombiss.app.data

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object LicenseManager {

    fun getDeviceId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return androidId.take(8).uppercase()
    }

    fun getDeviceModel(): String {
        return android.os.Build.MODEL ?: "Unknown"
    }

    suspend fun registerDeviceOnServer(context: Context) = withContext(Dispatchers.IO) {
        val url = URL("https://script.google.com/macros/s/AKfycbwlckeyeshqWrLzjUTderETd2iDMfN77ca1LeXXsFz9JI5ZijPiT0o9FiruyBhXb2o/exec")
        val deviceId = getDeviceId(context)
        val model = getDeviceModel()

        try {
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            val jsonParam = JSONObject()
            jsonParam.put("deviceId", deviceId)
            jsonParam.put("model", model)

            val os = conn.outputStream
            os.write(jsonParam.toString().toByteArray(Charsets.UTF_8))
            os.flush()
            os.close()

            conn.responseCode
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveActivation(context: Context, isActivated: Boolean) {
        val prefs = context.getSharedPreferences("PomBissPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_activated", isActivated).apply()
    }

    fun isActivated(context: Context): Boolean {
        val prefs = context.getSharedPreferences("PomBissPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_activated", false)
    }
}
