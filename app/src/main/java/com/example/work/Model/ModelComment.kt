package com.example.work.Model

import java.sql.Timestamp

class ModelComment {

    //variables

    var id = ""
    var bookId = ""
    var timestamp = ""
    var comment = ""
    var uid = ""

    //constructor for firebase

    constructor(id: String, bookId: String, timestamp: String, comment: String, uid: String) {

        this.id = id
        this.bookId = bookId
        this.timestamp = timestamp
        this.comment = comment
        this.uid = uid
    }

}