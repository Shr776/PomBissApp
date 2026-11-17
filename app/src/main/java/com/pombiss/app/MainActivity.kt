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
import com.pombiss.app.ui.screens.*
import com.pombiss.app.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            
            var currentTheme by remember { mutableStateOf(ThemeManager.getSelectedTheme(context)) }
            var showSettings by remember { mutableStateOf(false) }

            PomBissTheme(
                primaryColor = currentTheme.primary,
                accentColor = currentTheme.accent
            ) {
                if (showSettings) {
                    SettingsScreen(
                        currentThemeId = currentTheme.id,
                        onThemeSelected = { themeId ->
                            ThemeManager.saveTheme(context, themeId)
                            currentTheme = ThemeManager.getSelectedTheme(context)
                        },
                        onBack = { showSettings = false }
                    )
                    return@PomBissTheme
                }

                // وضعیت‌ها: 3=لودینگ، 0=لاگین/ثبت‌نام، 1=پندینگ، 2=برنامه اصلی
                var appState by remember { mutableStateOf(3) }
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }
                
                // آیا کاربر از قبل لاگین کرده؟ (برای اینکه هر بار صفحه لاگین نیاد)
                var isLoggedIn by remember { mutableStateOf(LicenseManager.isActivated(context)) }

                LaunchedEffect(Unit) {
                    if (LicenseManager.isActivated(context)) {
                        // اگه قبلاً لاگین کرده بود، برو تو
                        appState = 2
                    } else {
                        // اگه نکرده بود، برو صفحه لاگین
                        appState = 0
                    }
                }

                when (appState) {
                    3 -> {
                        Box(
                            modifier = Modifier.fillMaxSize().background(DarkBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = currentTheme.primary)
                        }
                    }
                    0 -> {
                        // صفحه لاگین و ثبت‌نام
                        if (isLoggedIn) {
                            appState = 2 // (دوباره چک میکنیم)
                        } else {
                            LoginScreen(
                                onLoginSuccess = {
                                    LicenseManager.saveActivation(applicationContext, true)
                                    isLoggedIn = true
                                    appState = 2
                                },
                                onGoToRegister = {
                                    // وقتی روی دکمه ثبت‌نام کلیک کرد، میره به فرم ثبت‌نام
                                    appState = 4 
                                }
                            )
                        }
                    }
                    4 -> {
                        // صفحه ثبت‌نام
                        RegistrationScreen(
                            onRegistrationSuccess = {
                                appState = 1 // برو به پندینگ
                            }
                        )
                    }
                    1 -> {
                        // صفحه انتظار
                        PendingScreen(
                            onApproved = {
                                LicenseManager.saveActivation(applicationContext, true)
                                appState = 2
                            }
                        )
                    }
                    2 -> {
                        // برنامه اصلی
                        if (selectedFeed == null) {
                            FeedListScreen(
                                repository = repository,
                                onFeedClick = { feed -> selectedFeed = feed },
                                onSettingsClick = { showSettings = true }
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
