package com.example.work.data

import android.net.Uri

class Bookdata
{
    var BookTitle: String? = ""
    var Author : String? = ""
    var Image: String? = ""
    var bookId : String? = ""
    var dateAdded : Long? = 0
    var AverageRatings : Double? = 0.0
    var personalRate : Double? = 0.0
    var viewCount: Long? = 0

    /*constructor(name: String?, author: String?, bookid: String?) {
        this.name = name
        this.author = author
        this.bookid = bookid
    }*/
    //empty constructor
    constructor()
    //constructor /w parameter
    constructor(
        BookTitle: String?,
        Author: String?,
        Image: String?,
        bookId: String?,
        dateAdded: Long?,
        AverageRatings : Double?,
        personalRate : Double?,
        viewCount: Long?
    ) {
        this.BookTitle = BookTitle
        this.Author = Author
        this.Image = Image
        this.bookId = bookId
        this.dateAdded = dateAdded
        this.AverageRatings = AverageRatings
        this.personalRate = personalRate
        this.viewCount = viewCount
    }

}