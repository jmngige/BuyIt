package com.starsolns.e_shop.model

data class Cart(
    val user_id: String = "",
    val seller_id: String = "",
    val product_id: String = "",
    val name: String = "",
    val price: String = "",
    val image: String = "",
    val quantity: String = "",
    var id: String = ""
) {}