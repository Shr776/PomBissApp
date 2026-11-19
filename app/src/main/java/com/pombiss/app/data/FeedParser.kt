package com.pombiss.app.data

object FeedParser {

    fun parseLine(line: String): Feed? {
        if (line.isBlank()) return null
        val parts = line.split("=").map { it.trim() }
        
        if (parts.size < 4) return null

        val freqInfo = parts[0].split(" ").filter { it.isNotBlank() }
        if (freqInfo.size < 4) return null

        val position = freqInfo[0]
        val freq = freqInfo[1]
        val pol = freqInfo[2]
        val sr = freqInfo[3]

        val cw = parts.getOrNull(1) ?: ""
        
        // --- منطق جدید و هوشمند برای کوتاه کردن عنوان ---
        var rawTitle = parts.getOrNull(2) ?: ""
        
        // ۱. اگه علامت @ داشت، از همون جا به بعد رو کات کن
        if (rawTitle.contains("@")) {
            rawTitle = rawTitle.substringBefore("@").trim()
        }
        
        // ۲. اگه پرانتز داشت، از همون جا کات کن
        if (rawTitle.contains("(")) {
            rawTitle = rawTitle.substringBefore("(").trim()
        }
        
        // ۳. لیست کلمات کلیدی ورزشی که اگه دیده شدن، بقیه متن حذف بشه
        val sportKeywords = listOf("Football", "Tennis", "Basketball", "Handball", "Water Sport", "Motorsport", "Boxing", "Cricket", "Rugby", "NFL", "UFC")
        for (keyword in sportKeywords) {
            val lowerTitle = rawTitle.lowercase()
            val lowerKeyword = keyword.lowercase()
            if (lowerTitle.contains(lowerKeyword)) {
                val index = lowerTitle.indexOf(lowerKeyword)
                rawTitle = rawTitle.substring(0, index + keyword.length).trim()
                break
            }
        }
        
        // ۴. اگه کلمه Feeds یا incomplete توش بود، کات کن (رفع خطای خط 47)
        val lowerTitleForFeeds = rawTitle.lowercase()
        if (lowerTitleForFeeds.contains("feeds")) {
            val index = lowerTitleForFeeds.indexOf("feeds")
            rawTitle = rawTitle.substring(0, index).trim()
        }
        
        // ۵. اگه هنوز طولانی بود، با سه نقطه کوتاهش کن
        if (rawTitle.length > 25) {
            rawTitle = rawTitle.take(25) + "..."
        }
        
        val title = if (rawTitle.isBlank()) "Live Feed" else rawTitle
        // -------------------------------------------------

        // --- منطق کوتاه کردن اسم ماهواره ---
        var sat = parts.getOrNull(3) ?: ""
        if (sat.contains("(")) {
            sat = sat.substringAfter("(").substringBefore(")").trim()
        }
        if (sat.length > 7) {
            sat = sat.take(7) + "..."
        }
        // -------------------------------------

        val type = parts.getOrNull(4) ?: ""
        val feedId = parts.getOrNull(5) ?: ""
        val event = parts.getOrNull(6) ?: ""
        val datetime = parts.lastOrNull { it.contains("-") && it.contains(":") } ?: ""

        return Feed(
            position = position,
            freq = freq,
            pol = pol,
            sr = sr,
            cw = cw,
            title = title,
            sat = sat,
            type = type,
            feedId = feedId,
            event = event,
            category = "",
            datetime = datetime
        )
    }
}
