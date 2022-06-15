package com.example.work.Model

import java.sql.Timestamp

class ModelComment {

    //variables

    var id = ""
    var bookId = ""
    var timestamp = ""
    var comment = ""
    var uid = ""
    var userRating : Double? = 0.0

    //constructor for firebase
    constructor()

    constructor(id: String, bookId: String, timestamp: String, comment: String, uid: String, userRating: Double) {

        this.id = id
        this.bookId = bookId
        this.timestamp = timestamp
        this.comment = comment
        this.uid = uid
        this.userRating = userRating
    }

}