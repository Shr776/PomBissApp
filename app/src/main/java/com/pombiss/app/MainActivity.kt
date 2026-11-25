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
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                var selectedFeedList by remember { mutableStateOf<List<Feed>>(emptyList()) }
                var selectedFeedIndex by remember { mutableIntStateOf(-1) }
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
                        if (selectedFeed == null) {
                            FeedListScreen(
                                repository = repository,
                                onFeedClick = { feedList, index ->
                                    selectedFeedList = feedList
                                    selectedFeedIndex = index
                                    selectedFeed = feedList.getOrNull(index)
                                },
                                onSettingsClick = { showSettings = true }
                            )
                        } else {
                            FeedDetailScreen(
                                feed = selectedFeed!!,
                                onBack = { selectedFeed = null },
                                canGoPrevious = selectedFeedIndex > 0,
                                canGoNext = selectedFeedIndex in 0 until selectedFeedList.lastIndex,
                                onPrevious = {
                                    if (selectedFeedIndex > 0) {
                                        selectedFeedIndex -= 1
                                        selectedFeed = selectedFeedList[selectedFeedIndex]
                                    }
                                },
                                onNext = {
                                    if (selectedFeedIndex < selectedFeedList.lastIndex) {
                                        selectedFeedIndex += 1
                                        selectedFeed = selectedFeedList[selectedFeedIndex]
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
