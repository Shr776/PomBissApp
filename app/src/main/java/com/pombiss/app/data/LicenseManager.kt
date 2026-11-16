package com.pombiss.app.data

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object LicenseManager {

    private const val SCRIPT_URL = "https://script.google.com/macros/s/AKfycbwlckeyeshqWrLzjUTderETd2iDMfN77ca1LeXXsFz9JI5ZijPiT0o9FiruyBhXb2o/exec"

    fun getDeviceId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return androidId.take(8).uppercase()
    }

    fun getDeviceModel(): String {
        return android.os.Build.MODEL ?: "Unknown"
    }

    // گرفتن برند گوشی
    fun getDeviceBrand(): String {
        return android.os.Build.BRAND ?: "Unknown"
    }

    suspend fun registerDeviceOnServer(context: Context) = withContext(Dispatchers.IO) {
        val deviceId = getDeviceId(context)
        val model = getDeviceModel()
        val brand = getDeviceBrand() // گرفتن برند

        try {
            val conn = URL(SCRIPT_URL).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val jsonParam = JSONObject()
            jsonParam.put("action", "register")
            jsonParam.put("deviceId", deviceId)
            jsonParam.put("model", model)
            jsonParam.put("brand", brand) // ارسال برند به سرور

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

    suspend fun checkUserStatus(context: Context): Boolean = withContext(Dispatchers.IO) {
        val deviceId = getDeviceId(context)
        try {
            val conn = URL(SCRIPT_URL).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val jsonParam = JSONObject()
            jsonParam.put("action", "checkStatus")
            jsonParam.put("deviceId", deviceId)

            val os = conn.outputStream
            os.write(jsonParam.toString().toByteArray(Charsets.UTF_8))
            os.flush()
            os.close()

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                
                val jsonResponse = JSONObject(response.toString())
                val userStatus = jsonResponse.optString("userStatus", "not_found")
                
                conn.disconnect()
                return@withContext userStatus.equals("active", ignoreCase = true)
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext isActivated(context)
        }
        return@withContext false
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
