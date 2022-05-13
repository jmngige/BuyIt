package com.starsolns.e_shop.model

data class Users (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val dob: String = "",
    var gender: String = "",
    val profilePicture: String = "",
    val profileComplete: Int = 0
){}