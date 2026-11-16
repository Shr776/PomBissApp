package com.pombiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pombiss.app.data.Feed
import com.pombiss.app.data.FeedRepository
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.screens.ActivationScreen
import com.pombiss.app.ui.screens.FeedDetailScreen
import com.pombiss.app.ui.screens.FeedListScreen
import com.pombiss.app.ui.screens.PendingScreen
import com.pombiss.app.ui.theme.DarkBackground
import com.pombiss.app.ui.theme.NeonBlue
import com.pombiss.app.ui.theme.PomBissTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomBissTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                // وضعیت‌ها: 0 = فعال‌سازی، 1 = انتظار، 2 = برنامه اصلی
                // 3 = در حال چک کردن لایسنس موقع ورود
                var appState by remember { mutableStateOf(3) }
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }

                // این بخش فقط یک بار موقع باز شدن برنامه اجرا میشه
                LaunchedEffect(Unit) {
                    if (LicenseManager.isActivated(context)) {
                        // اگر قبلا فعال شده بود، از سرور چک کن ببین هنوزم تاییده یا نه
                        val isStillActive = LicenseManager.checkUserStatus(context)
                        if (isStillActive) {
                            appState = 2 // هنوز تاییده، برو تو
                        } else {
                            appState = 1 // پندینگ شده، بفرستش به صفحه انتظار
                        }
                    } else {
                        appState = 0 // اصلاً قبلاً فعال نشده بود
                    }
                }

                when (appState) {
                    3 -> {
                        // صفحه لودینگ (فقط چند ثانیه موقع چک کردن لایسنس)
                        Box(
                            modifier = Modifier.fillMaxSize().background(DarkBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = NeonBlue)
                        }
                    }
                    0 -> {
                        ActivationScreen(
                            onActivationSuccess = { appState = 1 }
                        )
                    }
                    1 -> {
                        PendingScreen(
                            onApproved = {
                                LicenseManager.saveActivation(applicationContext, true)
                                appState = 2
                            }
                        )
                    }
                    2 -> {
                        if (selectedFeed == null) {
                            FeedListScreen(
                                repository = repository,
                                onFeedClick = { feed -> selectedFeed = feed }
                            )
                        } else {
                            FeedDetailScreen(
                                feed = selectedFeed!!,
                                onBack = { selectedFeed = null }
                            )
                        }
                    }
                }
            }
        }
    }
}
