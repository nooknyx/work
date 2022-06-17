package com.example.work.data

import android.net.Uri

class Bookdata
{
    var BookTitle: String = ""
    var Author : String = ""
    var Image: String = ""
    var bookId : String = ""
    var dateAdded : Long = 0
    var AverageRatings : Double = 0.0
    var personalRate : Double = 0.0
    var viewCount: Long = 0
    var isFavourite = false

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
        viewCount: Long?,
        isFavourite: Boolean
    ) {
        if (BookTitle != null) {
            this.BookTitle = BookTitle
        }
        if (Author != null) {
            this.Author = Author
        }
        if (Image != null) {
            this.Image = Image
        }
        if (bookId != null) {
            this.bookId = bookId
        }
        if (dateAdded != null) {
            this.dateAdded = dateAdded
        }
        if (AverageRatings != null) {
            this.AverageRatings = AverageRatings
        }
        if (personalRate != null) {
            this.personalRate = personalRate
        }
        if (viewCount != null) {
            this.viewCount = viewCount
        }
        this.isFavourite = isFavourite
    }

}