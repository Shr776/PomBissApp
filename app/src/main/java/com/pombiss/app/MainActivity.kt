package com.pombiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.pombiss.app.data.Feed
import com.pombiss.app.data.FeedRepository
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.screens.ActivationScreen
import com.pombiss.app.ui.screens.FeedDetailScreen
import com.pombiss.app.ui.screens.FeedListScreen
import com.pombiss.app.ui.screens.PendingScreen
import com.pombiss.app.ui.theme.PomBissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomBissTheme {
                // وضعیت‌ها: 0 = صفحه فعال‌سازی، 1 = صفحه انتظار، 2 = برنامه اصلی
                var appState by remember { 
                    mutableStateOf(if (LicenseManager.isActivated(applicationContext)) 2 else 0) 
                }
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }

                when (appState) {
                    0 -> {
                        // صفحه فعال‌سازی
                        ActivationScreen(
                            onActivationSuccess = {
                                appState = 1 // بعد از زدن دکمه، برو به صفحه انتظار
                            }
                        )
                    }
                    1 -> {
                        // صفحه انتظار برای تایید
                        PendingScreen(
                            onApproved = {
                                // اگر ادمین تایید کرد، وضعیت رو ذخیره کن و برو به برنامه اصلی
                                LicenseManager.saveActivation(applicationContext, true)
                                appState = 2 
                            }
                        )
                    }
                    2 -> {
                        // برنامه اصلی (لیست فیدها)
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
