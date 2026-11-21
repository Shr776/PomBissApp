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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            
            var currentTheme by remember { mutableStateOf(ThemeManager.getSelectedTheme(context)) }
            
            // وضعیت‌های باز شدن صفحات
            var showSettings by remember { mutableStateOf(false) }
            var showProfileEdit by remember { mutableStateOf(false) }

            PomBissTheme(
                primaryColor = currentTheme.primary,
                accentColor = currentTheme.accent
            ) {
                // اگر صفحه ویرایش پروفایل باز بود
                if (showProfileEdit) {
                    ProfileEditScreen(
                        onBack = { showProfileEdit = false }
                    )
                    return@PomBissTheme
                }

                // اگر صفحه تنظیمات باز بود
                if (showSettings) {
                    SettingsScreen(
                        currentThemeId = currentTheme.id,
                        onThemeSelected = { themeId ->
                            ThemeManager.saveTheme(context, themeId)
                            currentTheme = ThemeManager.getSelectedTheme(context)
                        },
                        onEditProfileClick = { 
                            showSettings = false
                            showProfileEdit = true 
                        },
                        onBack = { showSettings = false }
                    )
                    return@PomBissTheme
                }

                var appState by remember { mutableStateOf(3) }
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }

                LaunchedEffect(Unit) {
                    if (LicenseManager.isActivated(context)) {
                        val isStillActive = LicenseManager.checkUserStatus(context)
                        if (isStillActive) {
                            appState = 2
                        } else {
                            appState = 1
                        }
                    } else {
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
                        LoginScreen(
                            onLoginSuccess = {
                                LicenseManager.saveActivation(applicationContext, true)
                                appState = 2
                            },
                            onGoToRegister = {
                                appState = 4 
                            }
                        )
                    }
                    4 -> {
                        RegistrationScreen(
                            onRegistrationSuccess = {
                                appState = 1 
                            }
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
