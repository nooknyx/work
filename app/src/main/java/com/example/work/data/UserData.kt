package com.example.work.data

class UserData {
    var username: String? = null
    var email: String? = null
    var password: String? = null

    constructor(username: String?, email: String?, password: String?) {
        this.username = username
        this.email = email
        this.password = password
    }
}
