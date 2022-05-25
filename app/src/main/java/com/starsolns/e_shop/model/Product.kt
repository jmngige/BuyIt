package com.starsolns.e_shop.model

import java.io.Serializable

data class Product(
    val user_id: String,
    val user_name: String,
    val name: String,
    val price: Int,
    val category: String,
    val quantity: Int,
    val description: String,
    val richDescription: String,
    val productImage: String

): Serializable