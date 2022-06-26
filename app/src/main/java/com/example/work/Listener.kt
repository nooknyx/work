package com.example.work

import com.example.work.Model.Comments

interface Listener {
    fun onClickBookmark(comments: Comments, position: Int) {}
    fun onLoading(isLoading:Boolean) {}
}