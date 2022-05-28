package com.example.work.data

import android.net.Uri

class Bookdata
{
    var name: String? = ""
    var author : String? = ""
    var imagebook: Uri? = Uri.EMPTY
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
        author: String?,
        name: String?,
        imagebook: Uri?,
        bookId: String?,
        viewCount: Long?
    ) {
        this.author = author
        this.name = name
        this.imagebook = imagebook
        this.bookId = bookId
        this.viewCount = viewCount
    }

}
