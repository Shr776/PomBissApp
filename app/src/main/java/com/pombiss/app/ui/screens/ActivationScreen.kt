package com.pombiss.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.data.LicenseManager // این خط اضافه شد
import com.pombiss.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ActivationScreen(
    onActivationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // لوگو
        AsyncImage(
            model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to PomBiss Mobile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "To use this app, you need to activate your device first.",
            fontSize = 14.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // دکمه فعال‌سازی
        Button(
            onClick = {
                isLoading = true
                errorMsg = ""
                scope.launch {
                    try {
                        // ارسال اطلاعات به گوگل شیت
                        LicenseManager.registerDeviceOnServer(context)
                        // ذخیره وضعیت (برای اینکه دفعه بعد این صفحه نیاد)
                        LicenseManager.saveActivation(context, true)
                        // رفتن به صفحه اصلی
                        onActivationSuccess()
                    } catch (e: Exception) {
                        errorMsg = "Connection error. Please try again."
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Activate Device", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMsg, color = NeonRed, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Device ID: ${LicenseManager.getDeviceId(context)}",
            fontSize = 12.sp,
            color = TextGray
        )
    }
}
