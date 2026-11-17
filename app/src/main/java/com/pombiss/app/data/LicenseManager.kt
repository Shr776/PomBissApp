package com.pombiss.app.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import java.io.File

object LicenseManager {

    private const val SCRIPT_URL = "https://script.google.com/macros/s/AKfycbwlckeyeshqWrLzjUTderETd2iDMfN77ca1LeXXsFz9JI5ZijPiT0o9FiruyBhXb2o/exec"

    fun getDeviceId(context: Context): String {
        val brand = android.os.Build.BRAND ?: "Unknown"
        val model = android.os.Build.MODEL ?: "Unknown"
        
        val uuidFile = File(context.filesDir, "pombiss_device_uuid.txt")
        var uniqueId = ""

        if (uuidFile.exists()) {
            uniqueId = uuidFile.readText()
        } else {
            uniqueId = UUID.randomUUID().toString().take(8).uppercase()
            uuidFile.writeText(uniqueId)
        }

        return "$brand-$model-$uniqueId".replace(" ", "").uppercase()
    }

    fun getDeviceModel(): String {
        return android.os.Build.MODEL ?: "Unknown"
    }

    fun getDeviceBrand(): String {
        return android.os.Build.BRAND ?: "Unknown"
    }

    // تابع لاگین
    suspend fun loginUser(context: Context, username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val conn = URL(SCRIPT_URL).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val jsonParam = JSONObject()
            jsonParam.put("action", "login")
            jsonParam.put("username", username)
            jsonParam.put("password", password)

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
                val status = jsonResponse.optString("status", "error")
                val userStatus = jsonResponse.optString("userStatus", "not_found")
                
                conn.disconnect()
                
                // اگه لاگین موفق بود و وضعیت active بود
                if (status == "success" && userStatus.equals("active", ignoreCase = true)) {
                    return@withContext true
                }
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    suspend fun registerDeviceWithDetails(
        context: Context,
        username: String,
        password: String, // پسورد جدید
        country: String,
        phoneModel: String,
        receiverBrand: String,
        receiverModel: String,
        imageName: String,
        age: String,
        gender: String
    ) = withContext(Dispatchers.IO) {
        val deviceId = getDeviceId(context)
        val brand = getDeviceBrand()

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
            jsonParam.put("brand", brand)
            jsonParam.put("username", username)
            jsonParam.put("password", password) // ارسال پسورد
            jsonParam.put("phoneModel", phoneModel)
            jsonParam.put("country", country)
            jsonParam.put("receiverBrand", receiverBrand)
            jsonParam.put("receiverModel", receiverModel)
            jsonParam.put("imageName", imageName)
            jsonParam.put("age", age)
            jsonParam.put("gender", gender)

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
                
                if (userStatus.equals("active", ignoreCase = true)) {
                    return@withContext true
                }
                return@withContext false
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
