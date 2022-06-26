package com.example.work.Model

data class Comments(
    var bookId: String = "",
    var comment: String = "",
    var id: String = "",
    var timestamp: String = "",
    var uid: String = "",
    var userRating: Double? = 0.0,
    var isBookmark: Boolean? = false
)
