package com.pombiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pombiss.app.data.Feed
import com.pombiss.app.data.FeedRepository
import com.pombiss.app.ui.screens.FeedDetailScreen
import com.pombiss.app.ui.screens.FeedListScreen
import com.pombiss.app.ui.theme.DarkBackground
import com.pombiss.app.ui.theme.PomBissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomBissTheme {
                val navController = rememberNavController()
                val repository = remember { FeedRepository(applicationContext) }

                NavHost(
                    navController = navController,
                    startDestination = "list",
                    modifier = Modifier.fillMaxSize().background(DarkBackground)
                ) {
                    composable("list") {
                        FeedListScreen(
                            repository = repository,
                            onFeedClick = { feed ->
                                // برای سادگی، اطلاعات فید رو به صورت موقت نگه می‌داریم
                                // در نسخه‌های بعدی می‌شه از State بهتری استفاده کرد
                                navController.currentBackStackEntry?.savedStateHandle?.set("feed", feed)
                                navController.navigate("detail")
                            }
                        )
                    }
                    composable("detail") {
                        val feed = navController.previousBackStackEntry?.savedStateHandle?.get<Feed>("feed")
                        if (feed != null) {
                            FeedDetailScreen(feed = feed, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
