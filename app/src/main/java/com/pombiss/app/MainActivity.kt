package com.pombiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pombiss.app.data.Feed
import com.pombiss.app.data.FeedRepository
import com.pombiss.app.data.LicenseManager // این خط اضافه شد
import com.pombiss.app.ui.screens.ActivationScreen
import com.pombiss.app.ui.screens.FeedDetailScreen
import com.pombiss.app.ui.screens.FeedListScreen
import com.pombiss.app.ui.theme.DarkBackground
import com.pombiss.app.ui.theme.PomBissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomBissTheme {
                // وضعیت قفل: آیا کاربر تایید شده یا نه؟
                var isActivated by remember { mutableStateOf(LicenseManager.isActivated(applicationContext)) }
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }

                if (!isActivated) {
                    // اگر فعال نشده بود، صفحه قفل رو نشون بده
                    ActivationScreen(
                        onActivationSuccess = {
                            isActivated = true // بعد از موفقیت، میره به صفحه اصلی
                        }
                    )
                } else {
                    // اگر فعال بود (یا بعد از فعال شدن)، برنامه اصلی رو نشون بده
                    if (selectedFeed == null) {
                        FeedListScreen(
                            repository = repository,
                            onFeedClick = { feed ->
                                selectedFeed = feed
                            }
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
