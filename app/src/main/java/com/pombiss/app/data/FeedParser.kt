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
        val rawTitle = parts.getOrNull(2) ?: ""
        val sat = parts.getOrNull(3) ?: ""
        
        val type = parts.getOrNull(4) ?: ""
        val feedId = parts.getOrNull(5) ?: ""
        val event = parts.getOrNull(6) ?: ""
        
        val datetime = parts.lastOrNull { it.contains("-") && it.contains(":") } ?: ""

        // ۱. اضافه کردن هشتگ به عنوان
        val title = if (rawTitle.isNotBlank()) "#$rawTitle" else ""

        // ۲. استخراج نام مختصر ماهواره (مثلا از Eutelsat 16D (16E) میشه 16E)
        var shortSat = sat
        val match = Regex("""\(([^)]+)\)""").find(sat)
        if (match != null) {
            shortSat = match.groupValues[1].trim()
        }

        return Feed(
            position = position,
            freq = freq,
            pol = pol,
            sr = sr,
            cw = cw,
            title = title,
            sat = shortSat,
            type = type,
            feedId = feedId,
            event = event,
            category = "", // فعلاً خالی، بعداً پر میشه
            datetime = datetime
        )
    }
}
