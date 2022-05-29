package com.example.work.data

import android.net.Uri

class Bookdata
{
    var BookTitle: String? = ""
    var Author : String? = ""
    var Image: String? = ""
    var bookId : String? = ""
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
        viewCount: Long?
    ) {
        this.BookTitle = BookTitle
        this.Author = Author
        this.Image = Image
        this.bookId = bookId
        this.viewCount = viewCount
    }

}