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
        
        // --- منطق قوی برای کوتاه کردن عنوان ---
        var rawTitle = parts.getOrNull(2) ?: ""
        
        // ۱. حذف هشتگ از اول
        if (rawTitle.startsWith("#")) {
            rawTitle = rawTitle.substring(1).trim()
        }
        
        // ۲. اگه علامت @ یا پرانتز داشت، از همون جا به بعد رو کات کن
        if (rawTitle.contains("@")) rawTitle = rawTitle.substringBefore("@").trim()
        if (rawTitle.contains("(")) rawTitle = rawTitle.substringBefore("(").trim()
        
        // ۳. لیست کلمات کلیدی ورزشی (دقیقاً همونایی که توی فایل هستن)
        val sportKeywords = listOf(
            "Football", "Tennis", "Basketball", "Handball", 
            "Water Sport", "Motorsport", "Boxing", "Cricket", "Rugby", "NFL", "UFC", "Darts",
            "Figure Skating", "UEFA Champions League", "UEFA", "NHL"
        )
        
        var foundKeyword = false
        for (keyword in sportKeywords) {
            // چک می‌کنیم که کلمه کلیدی توی عنوان باشه
            if (rawTitle.contains(keyword, ignoreCase = true)) {
                // اگه بود، فقط تا آخر همون کلمه رو نگه می‌داریم و بقیه رو کات می‌کنیم
                val index = rawTitle.indexOf(keyword, ignoreCase = true)
                rawTitle = rawTitle.substring(0, index + keyword.length).trim()
                foundKeyword = true
                break // دیگه بقیه کلمات رو چک نکن
            }
        }
        
        // ۴. اگه هیچ کلمه کلیدی پیدا نشد، عنوان رو دست نمی‌زنیم (یا حداکثر ۲۵ کاراکتر کوتاهش می‌کنیم)
        if (!foundKeyword && rawTitle.length > 25) {
            rawTitle = rawTitle.take(25) + "..."
        }
        
        val title = if (rawTitle.isBlank()) "Live Feed" else rawTitle
        // -------------------------------------------------

        // --- کوتاه کردن اسم ماهواره ---
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
