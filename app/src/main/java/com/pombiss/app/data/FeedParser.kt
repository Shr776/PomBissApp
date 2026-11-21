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
        
        // --- ۱. کوتاه کردن عنوان (مثل خط اول کانال تلگرام) ---
        var rawTitle = parts.getOrNull(2) ?: ""
        
        // اگه علامت @ یا پرانتز داشت، از همون جا به بعد رو کات کن
        if (rawTitle.contains("@")) rawTitle = rawTitle.substringBefore("@").trim()
        if (rawTitle.contains("(")) rawTitle = rawTitle.substringBefore("(").trim()
        
        // اگه کلمه Feed یا Feeds توش بود، از همون جا کات کن
        val lowerTitle = rawTitle.lowercase()
        if (lowerTitle.contains("feed")) {
            val index = lowerTitle.indexOf("feed")
            rawTitle = rawTitle.substring(0, index).trim()
        }
        
        // حذف هشتگ از اول
        if (rawTitle.startsWith("#")) rawTitle = rawTitle.substring(1).trim()
        
        // محدود کردن به ۲۵ کاراکتر
        if (rawTitle.length > 25) rawTitle = rawTitle.take(25) + "..."
        
        val title = if (rawTitle.isBlank()) "Live Feed" else rawTitle

        // --- ۲. استخراج اسم تیم‌ها از بخش Event ---
        val fullEvent = parts.getOrNull(6) ?: ""
        // اگه توی ایونت خط تیره (-) بود، بخش بعد از اون (اسم تیم‌ها) رو بردار
        val eventTeams = if (fullEvent.contains("-")) {
            fullEvent.substringAfter("-").trim()
        } else {
            fullEvent
        }

        // --- ۳. کوتاه کردن اسم ماهواره ---
        var sat = parts.getOrNull(3) ?: ""
        if (sat.contains("(")) {
            sat = sat.substringAfter("(").substringBefore(")").trim()
        }
        if (sat.length > 7) {
            sat = sat.take(7) + "..."
        }

        val type = parts.getOrNull(4) ?: ""
        val feedId = parts.getOrNull(5) ?: ""
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
            event = eventTeams, // حالا این فقط اسم تیم‌هاست
            category = "",
            datetime = datetime
        )
    }
}
