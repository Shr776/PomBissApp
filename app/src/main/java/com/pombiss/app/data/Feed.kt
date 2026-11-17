package com.pombiss.app.data

data class Feed(
    val position: String,
    val freq: String,
    val pol: String,
    val sr: String,
    val cw: String,
    val title: String,
    val sat: String,
    val type: String,
    val feedId: String,
    val event: String,
    val category: String, 
    val datetime: String
) {
    fun toShareString(): String {
        // لینک کانال اصلی اینجاست
        return "Sat: $sat\nFreq: $freq $pol $sr\nFeed: ${if(feedId.isNotBlank()) feedId else title}\nType: $type\nCW: $cw\nTime: $datetime\n\n📡 Join: https://t.me/PomFeed"
    }
}
