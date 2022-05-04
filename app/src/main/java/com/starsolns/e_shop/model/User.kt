package com.starsolns.e_shop.model

data class User (
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val gender: String = "",
    val profilePicture: String = "",
    val profileComplete: Int = 0
)