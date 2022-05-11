package com.starsolns.e_shop.model

import androidx.room.Entity

@Entity(tableName = "user_profile")
data class UserEntity(
    val id: String,
    val firstName: String ,
    val lastName: String,
    val email: String,
    val phone: String,
    val dob: String,
    val gender: String,
)