package com.example.work.Model

import java.sql.Timestamp

class ModelComment {

    //variables


    var bookId = ""
    var comment = ""
    var id = ""
    var timestamp = ""
    var uid = ""
    var userRating : Double? = 0.0

    //constructor for firebase
    constructor()

    constructor(bookId: String, id: String, comment: String, timestamp: String,  uid: String, userRating: Double) {

        this.bookId = bookId
        this.comment = comment
        this.id = id
        this.timestamp = timestamp
        this.uid = uid
        this.userRating = userRating
    }

}