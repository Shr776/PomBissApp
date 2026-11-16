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
import com.pombiss.app.ui.screens.FeedDetailScreen
import com.pombiss.app.ui.screens.FeedListScreen
import com.pombiss.app.ui.theme.DarkBackground
import com.pombiss.app.ui.theme.PomBissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomBissTheme {
                // این دو خط جدید جایگزین NavController شدن
                var selectedFeed by remember { mutableStateOf<Feed?>(null) }
                val repository = remember { FeedRepository(applicationContext) }

                if (selectedFeed == null) {
                    FeedListScreen(
                        repository = repository,
                        onFeedClick = { feed ->
                            selectedFeed = feed // با کلیک، فید انتخاب میشه
                        }
                    )
                } else {
                    FeedDetailScreen(
                        feed = selectedFeed!!,
                        onBack = { selectedFeed = null } // با زدن دکمه برگشت، فید صفر میشه
                    )
                }
            }
        }
    }
}
