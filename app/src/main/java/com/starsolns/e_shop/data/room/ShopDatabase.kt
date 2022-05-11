package com.starsolns.e_shop.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.starsolns.e_shop.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class ShopDatabase: RoomDatabase() {
    abstract fun shopDao(): ShopDao
}