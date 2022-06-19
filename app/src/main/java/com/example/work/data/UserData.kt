package com.example.work.data

class UserData {
    var email: String? = null
    var password: String? = null
    var userUID: String? = null
    var username: String? = null
    var usertype: String? = null
    //empty constructor
    constructor()
    //constructor with parameters
    constructor(userUID: String?, email: String?, password: String?,username: String?, usertype: String?) {
        this.email = email
        this.password = password
        this.userUID = userUID
        this.username = username
        this.usertype = usertype
    }
}