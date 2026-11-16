package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.Feed
import com.pombiss.app.data.FeedRepository
import com.pombiss.app.ui.components.FeedCard
import com.pombiss.app.ui.components.HeaderBar
import com.pombiss.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FeedListScreen(
    repository: FeedRepository,
    onFeedClick: (Feed) -> Unit
) {
    var feeds by remember { mutableStateOf<List<Feed>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun loadFeeds(force: Boolean) {
        scope.launch {
            isLoading = true
            errorMsg = null
            try {
                feeds = repository.getFeeds(forceRefresh = force)
            } catch (e: Exception) {
                errorMsg = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadFeeds(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        HeaderBar(
            feedCount = feeds.size,
            onRefreshClick = { loadFeeds(true) },
            onSearchClick = { /* TODO: Search implementation */ }
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonBlue)
            }
        } else if (errorMsg != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMsg!!, color = NeonRed)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(feeds) { feed ->
                    FeedCard(feed = feed, onClick = { onFeedClick(feed) })
                }
                
                // فوتر تبلیغاتی
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Developed by @Shr776 | @VuSolo | @PomSat",
                        color = TextGray,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
