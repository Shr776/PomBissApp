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
        
        var rawTitle = parts.getOrNull(2) ?: ""
        
        if (rawTitle.startsWith("#")) rawTitle = rawTitle.substring(1).trim()
        if (rawTitle.contains("@")) rawTitle = rawTitle.substringBefore("@").trim()
        if (rawTitle.contains("(")) rawTitle = rawTitle.substringBefore("(").trim()
        
        val sportKeywords = listOf(
            "Football", "Tennis", "Basketball", "Handbal", "Vollyball", "Volleyball", 
            "Water Sport", "Motorsport", "Boxing", "Cricket", "Rugby", "NFL", "UFC", "Darts",
            "Figure Skating", "UEFA Champions League", "UEFA", "NHL", "MotoGP", "Formula"
        )
        
        var foundKeyword = false
        for (keyword in sportKeywords) {
            if (rawTitle.contains(keyword, ignoreCase = true)) {
                val index = rawTitle.indexOf(keyword, ignoreCase = true)
                rawTitle = rawTitle.substring(0, index + keyword.length).trim()
                foundKeyword = true
                break 
            }
        }
        
        if (!foundKeyword && rawTitle.length > 25) {
            rawTitle = rawTitle.take(25) + "..."
        }
        
        val title = if (rawTitle.isBlank()) "Live Feed" else rawTitle

        var sat = parts.getOrNull(3) ?: ""
        if (sat.contains("(")) {
            sat = sat.substringAfter("(").substringBefore(")").trim()
        }
        if (sat.length > 7) {
            sat = sat.take(7) + "..."
        }

        val type = parts.getOrNull(4) ?: ""
        val feedId = parts.getOrNull(5) ?: ""
        
        // --- منطق جدید برای پیدا کردن اسم تیم‌ها ---
        val rawEvent = parts.getOrNull(6) ?: ""
        val event = if (rawEvent.contains("vs", ignoreCase = true) || rawEvent.contains(" - ")) {
            rawEvent // اگه اسم تیم‌ها بود، همون رو بذار
        } else {
            "" // اگه فقط اسم لیگ بود، خالی بذار تا Match Info نمایش داده نشه
        }
        // -------------------------------------------

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
            event = event, // اینجا از event اصلاح شده استفاده میکنیم
            category = "",
            datetime = datetime
        )
    }
}
