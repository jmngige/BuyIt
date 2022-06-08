package com.starsolns.e_shop.model

import java.io.Serializable

data class Address (
    val user_id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    var region: String = "",
    var town: String = "",
    var address: String = "",
    var addressInfo: String = "",
    var id: String = ""

){}