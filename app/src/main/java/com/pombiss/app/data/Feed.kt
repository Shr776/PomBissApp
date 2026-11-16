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
    val datetime: String
) {
    // این تابع برای اینه که کل مشخصات رو با یه دکمه بشه کپی کرد
    fun toShareString(): String {
        return "Sat: $sat\nFreq: $freq $pol $sr\nFeed: ${if(feedId.isNotBlank()) feedId else title}\nType: $type\nCW: $cw\nTime: $datetime"
    }
}
