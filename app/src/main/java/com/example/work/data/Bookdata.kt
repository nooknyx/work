package com.example.work.data

import android.net.Uri

class Bookdata
{
    var BookTitle: String? = ""
    var Author : String? = ""
    var Image: String? = ""
    var bookId : String? = ""
    var bookSummary : String? = ""
    var dateAdded : Long? = 0
    var AverageRatings : Double? = 0.0
    var personalRate : Double? = 0.0
    var viewCount: Long? = 0
    var isFavourite = false
    var Barcode: Long? = 0

    //empty constructor
    constructor()
    //constructor /w parameter
    constructor(
        BookTitle: String?,
        Author: String?,
        Image: String?,
        bookId: String?,
        bookSummary: String?,
        dateAdded: Long?,
        AverageRatings : Double?,
        personalRate : Double?,
        viewCount: Long?,
        isFavourite: Boolean,
        Barcode: Long?
    ) {
        this.BookTitle = BookTitle
        this.Author = Author
        this.Image = Image
        this.bookId = bookId
        this.bookSummary = bookSummary
        this.dateAdded = dateAdded
        this.AverageRatings = AverageRatings
        this.personalRate = personalRate
        this.viewCount = viewCount
        this.isFavourite = isFavourite
        this.Barcode = Barcode
    }

}