package com.starsolns.e_shop.model

import java.io.Serializable

data class Product(
    val user_id: String = "",
    var product_id: String = "",
    val user_name: String = "",
    val name: String = "",
    val price: String = "",
    val category: String = "",
    val quantity: String = "",
    val description: String = "",
    val richDescription: String = "",
    val productImage: String = ""

): Serializable {}