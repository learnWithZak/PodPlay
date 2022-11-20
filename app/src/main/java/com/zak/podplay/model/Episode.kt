package com.zak.podplay.model

import java.util.*

data class Episode(
    var guid: String = "",
    var titre: String = "",
    var description: String = "",
    var medialUrl: String = "",
    var mimeType: String = "",
    var releaseDate: Date = Date(),
    var duration: String = ""
)