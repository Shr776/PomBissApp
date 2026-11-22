package com.pombiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
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

                var appState by remember { mutableStateOf(3) }
                var selectedFeedList by remember { mutableStateOf<List<Feed>>(emptyList()) }
                var currentIndex by remember { mutableStateOf(-1) }
                val repository = remember { FeedRepository(applicationContext) }

                LaunchedEffect(Unit) {
                    if (LicenseManager.isActivated(context)) {
                        appState = 2
                    } else {
                        appState = 0
                    }
                }

                when (appState) {
                    3 -> { /* لودینگ */ }
                    0 -> {
                        LoginScreen(
                            onLoginSuccess = {
                                LicenseManager.saveActivation(applicationContext, true)
                                appState = 2
                            },
                            onGoToRegister = { appState = 4 }
                        )
                    }
                    4 -> {
                        RegistrationScreen(
                            onRegistrationSuccess = { appState = 1 }
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
                        if (currentIndex == -1) {
                            FeedListScreen(
                                repository = repository,
                                onFeedClickWithIndex = { feed, index, fullList ->
                                    selectedFeedList = fullList
                                    currentIndex = index
                                },
                                onSettingsClick = { showSettings = true }
                            )
                        }
                    }
                    5 -> {
                        if (currentIndex in selectedFeedList.indices) {
                            FeedDetailScreen(
                                feed = selectedFeedList[currentIndex],
                                onBack = { currentIndex = -1 },
                                onNext = if (currentIndex < selectedFeedList.size - 1) {
                                    { currentIndex++ }
                                } else null,
                                onPrev = if (currentIndex > 0) {
                                    { currentIndex-- }
                                } else null
                            )
                        }
                    }
                }
                
                // مدیریت وضعیت برای رفتن به صفحه جزئیات
                LaunchedEffect(currentIndex) {
                    if (currentIndex != -1) {
                        appState = 5
                    } else {
                        appState = 2
                    }
                }
            }
        }
    }
}
