package com.starsolns.e_shop.util

object Constants {


        const val USERS = "Users"
        const val CAMERA_OPTION_CODE = 101
        const val GALLERY_OPTION_CODE = 102

        /** datastore */
        const val DATASTORE_PREF_NAME = "profile_data"
        const val IMAGE_STRING_KEY = "load_image_string"

        /** Room Database */
        const val DATABASE_NAME = "user_database"


    fun getAllCategories(): ArrayList<String>{
        val categories = ArrayList<String>()
        categories.add("Smartphones")
        categories.add("TV's")
        categories.add("Laptops")
        categories.add("Shoes")
        categories.add("Clothes")
        categories.add("Watches")
        categories.add("Beauty")
        categories.add("Kitchen")
        categories.add("Bedroom")

        return categories
    }

}