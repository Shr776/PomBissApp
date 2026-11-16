package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PendingScreen(
    onApproved: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(true) }

    // حلقه چک کردن وضعیت هر ۵ ثانیه
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L) // هر ۵ ثانیه
            val isActive = LicenseManager.checkUserStatus(context)
            if (isActive) {
                onApproved() // اگر تایید شد، برو به برنامه اصلی
                break
            }
        }
    }

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
            text = "Pending Approval",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NeonOrange,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your registration is pending.\nPlease contact admin to activate your device.",
            fontSize = 16.sp,
            color = TextWhite,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Telegram Support:\nID: @Shr776\nChannel: https://t.me/VuSolo",
            fontSize = 14.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        CircularProgressIndicator(color = NeonBlue)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Checking status automatically...",
            fontSize = 12.sp,
            color = TextGray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Device ID: ${LicenseManager.getDeviceId(context)}",
            fontSize = 12.sp,
            color = TextGray
        )
    }
}
