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
        val title = parts.getOrNull(2) ?: ""
        val sat = parts.getOrNull(3) ?: ""
        
        val type = parts.getOrNull(4) ?: ""
        val feedId = parts.getOrNull(5) ?: ""
        val event = parts.getOrNull(6) ?: ""
        
        // تاریخ معمولاً آخرین بخش است که شبیه تاریخ است
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
            datetime = datetime
        )
    }
}
