package com.starsolns.e_shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    val firstName: String ,
    val lastName: String,
    val email: String,
    val phone: String,
    val dob: String?,
    val gender: String?,
    @PrimaryKey
    val id: String,
)