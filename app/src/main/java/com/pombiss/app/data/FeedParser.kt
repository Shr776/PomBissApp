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
        
        // --- شروع منطق کوتاه کردن عنوان ---
        var rawTitle = parts.getOrNull(2) ?: ""
        // اگه پرانتز داشت، فقط تا قبل از پرانتز رو بردار
        if (rawTitle.contains("(")) {
            rawTitle = rawTitle.substringBefore("(").trim()
        }
        // اگه فاصله داشت و کلمه اول با هشتگ بود، فقط کلمه اول رو بردار
        if (rawTitle.startsWith("#") && rawTitle.contains(" ")) {
            rawTitle = rawTitle.split(" ")[0]
        }
        val title = rawTitle.trim()
        // -----------------------------------

        // --- شروع منطق کوتاه کردن اسم ماهواره ---
        var sat = parts.getOrNull(3) ?: ""
        // اگه اسم ماهواره طولانی بود (مثل HotBird 13F/13G ( 13E ))، فقط قسمت داخل پرانتز (13E) رو بردار
        if (sat.contains("(")) {
            sat = sat.substringAfter("(").substringBefore(")").trim()
        }
        // اگه هنوز طولانی بود، کوتاهش کن
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
