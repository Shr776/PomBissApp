package com.pombiss.app.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

class FeedRepository(private val context: Context) {

    private val feedsUrl = "https://raw.githubusercontent.com/Shr776/PomBissFeeds/main/feeds.txt"
    private val cacheFile = File(context.cacheDir, "pombiss_feeds.txt")

    suspend fun getFeeds(forceRefresh: Boolean = false): List<Feed> = withContext(Dispatchers.IO) {
        val text = if (forceRefresh || !cacheFile.exists()) {
            try {
                val downloaded = URL(feedsUrl).readText()
                cacheFile.writeText(downloaded)
                downloaded
            } catch (e: Exception) {
                // اگر اینترنت قطع بود، از حافظه می‌خوانیم
                if (cacheFile.exists()) cacheFile.readText() else ""
            }
        } else {
            cacheFile.readText()
        }

        val lines = text.lines()
        val feeds = mutableListOf<Feed>()
        
        for (line in lines) {
            val feed = FeedParser.parseLine(line)
            if (feed != null) {
                feeds.add(feed)
            }
        }
        
        // مرتب‌سازی بر اساس تاریخ (جدیدترین‌ها اول)
        feeds.sortedByDescending { it.datetime }
    }
}
