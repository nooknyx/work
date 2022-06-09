package com.example.work.Model

import java.sql.Timestamp

class ModelComment {

    //variables
    var bookId = ""
    var comment = ""
    var id = ""
    var timestamp = ""
    var uid = ""

    //empty constructor
    constructor()
    //constructor with parameters
    constructor(bookId: String, comment: String, id: String, timestamp: String, uid: String) {
        this.bookId = bookId
        this.comment = comment
        this.id = id
        this.timestamp = timestamp
        this.uid = uid
    }

}