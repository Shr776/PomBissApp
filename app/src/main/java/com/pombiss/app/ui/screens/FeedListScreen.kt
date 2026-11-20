package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState // اضافه شد
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedListScreen(
    repository: FeedRepository,
    onFeedClick: (Feed) -> Unit,
    onSettingsClick: () -> Unit
) {
    var feeds by remember { mutableStateOf<List<Feed>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // این خط موقعیت اسکرول رو ذخیره می‌کنه
    val listState = rememberLazyListState()

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

    val filteredFeeds = if (searchQuery.isBlank()) {
        feeds
    } else {
        feeds.filter {
            it.sat.contains(searchQuery, ignoreCase = true) ||
            it.feedId.contains(searchQuery, ignoreCase = true) ||
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.freq.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        HeaderBar(
            feedCount = filteredFeeds.size,
            onRefreshClick = { loadFeeds(true) },
            onSearchClick = { 
                showSearchBar = !showSearchBar 
                if (!showSearchBar) searchQuery = ""
            },
            onSettingsClick = onSettingsClick
        )

        if (showSearchBar) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, satellite, or freq...", color = TextGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextWhite)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = TextGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonBlue)
            }
        } else if (errorMsg != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMsg!!, color = NeonRed)
            }
        } else {
            if (filteredFeeds.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No feeds found matching your search.", color = TextGray)
                }
            } else {
                LazyColumn(
                    state = listState, // وصل کردن استیت به لیست
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(filteredFeeds) { index, feed ->
                        FeedCard(
                            feed = feed, 
                            index = index, 
                            onClick = { onFeedClick(feed) }
                        )
                    }
                    
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
}
