package com.pombiss.app.data

import android.content.Context

object FavoritesManager {

    private const val PREFS_NAME = "PomBissFavorites"
    private const val KEY_FAVORITES = "favorite_feeds"

    // گرفتن لیست فیدهای ذخیره شده
    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    // اضافه یا حذف کردن یه فید از لیست
    fun toggleFavorite(context: Context, feedId: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentFavorites = getFavorites(context).toMutableSet()
        
        val isNowFavorite: Boolean
        if (currentFavorites.contains(feedId)) {
            currentFavorites.remove(feedId)
            isNowFavorite = false
        } else {
            currentFavorites.add(feedId)
            isNowFavorite = true
        }
        
        prefs.edit().putStringSet(KEY_FAVORITES, currentFavorites).apply()
        return isNowFavorite
    }

    // چک کردن اینکه یه فید جزو علاقه‌مندی‌هاست یا نه
    fun isFavorite(context: Context, feedId: String): Boolean {
        return getFavorites(context).contains(feedId)
    }
}
