package com.pombiss.app.data

import android.content.Context

object FavoritesManager {

    private const val PREFS_NAME = "PomBissFavorites"
    private const val KEY_FAVORITES = "favorite_feeds"

    // ساخت یه آیدی یکتا از ترکیب اطلاعات فید
    private fun getUniqueId(feed: Feed): String {
        return "${feed.freq}_${feed.pol}_${feed.sr}_${feed.datetime}".replace(" ", "")
    }

    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    fun toggleFavorite(context: Context, feed: Feed): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentFavorites = getFavorites(context).toMutableSet()
        val uniqueId = getUniqueId(feed)
        
        val isNowFavorite: Boolean
        if (currentFavorites.contains(uniqueId)) {
            currentFavorites.remove(uniqueId)
            isNowFavorite = false
        } else {
            currentFavorites.add(uniqueId)
            isNowFavorite = true
        }
        
        prefs.edit().putStringSet(KEY_FAVORITES, currentFavorites).apply()
        return isNowFavorite
    }

    fun isFavorite(context: Context, feed: Feed): Boolean {
        return getFavorites(context).contains(getUniqueId(feed))
    }
}
