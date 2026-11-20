package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.FavoritesManager
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
    val context = LocalContext.current
    var feeds by remember { mutableStateOf<List<Feed>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    var showFavoritesOnly by remember { mutableStateOf(false) }
    
    // استفاده از getFavorites برای ذخیره آیدی‌های یکتا
    var favoriteIds by remember { mutableStateOf(FavoritesManager.getFavorites(context)) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun loadFeeds(force: Boolean) {
        scope.launch {
            isLoading = true
            errorMsg = null
            try {
                val newFeeds = repository.getFeeds(forceRefresh = force)
                feeds = newFeeds
                
                // حذف فیدهایی که دیگه توی لیست نیستن از فیووریت
                val currentFeedIds = newFeeds.map { "${it.freq}_${it.pol}_${it.sr}_${it.datetime}".replace(" ", "") }.toSet()
                val currentFavorites = FavoritesManager.getFavorites(context).toMutableSet()
                currentFavorites.retainAll(currentFeedIds)
                
                context.getSharedPreferences("PomBissFavorites", android.content.Context.MODE_PRIVATE)
                    .edit().putStringSet("favorite_feeds", currentFavorites).apply()
                
                favoriteIds = currentFavorites
                
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

    val searchedFeeds = if (searchQuery.isBlank()) {
        feeds
    } else {
        feeds.filter {
            it.sat.contains(searchQuery, ignoreCase = true) ||
            it.feedId.contains(searchQuery, ignoreCase = true) ||
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.freq.contains(searchQuery, ignoreCase = true)
        }
    }

    // فیلتر کردن برای فیووریت
    val displayedFeeds = if (showFavoritesOnly) {
        searchedFeeds.filter { feed ->
            val uniqueId = "${feed.freq}_${feed.pol}_${feed.sr}_${feed.datetime}".replace(" ", "")
            favoriteIds.contains(uniqueId)
        }
    } else {
        searchedFeeds
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        HeaderBar(
            feedCount = displayedFeeds.size,
            onRefreshClick = { loadFeeds(true) },
            onSearchClick = { 
                showSearchBar = !showSearchBar 
                if (!showSearchBar) searchQuery = ""
            },
            onSettingsClick = onSettingsClick,
            isFavoriteView = showFavoritesOnly,
            onToggleFavoriteView = { 
                showFavoritesOnly = !showFavoritesOnly 
                // برای اینکه بعد از تغییر تب، لیست بره بالای صفحه
                scope.launch { listState.scrollToItem(0) }
            }
        )

        if (showSearchBar) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search...", color = TextGray) },
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
            if (displayedFeeds.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (showFavoritesOnly) "No favorite feeds found." else "No feeds found.",
                        color = TextGray
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(displayedFeeds) { index, feed ->
                        val uniqueId = "${feed.freq}_${feed.pol}_${feed.sr}_${feed.datetime}".replace(" ", "")
                        FeedCard(
                            feed = feed, 
                            index = index,
                            isFavorite = favoriteIds.contains(uniqueId),
                            onFavoriteClick = {
                                FavoritesManager.toggleFavorite(context, feed)
                                favoriteIds = FavoritesManager.getFavorites(context)
                            },
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
