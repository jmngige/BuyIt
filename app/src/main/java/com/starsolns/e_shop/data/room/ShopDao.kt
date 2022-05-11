package com.starsolns.e_shop.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.starsolns.e_shop.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    @Insert
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_profile")
    fun userProfile(): Flow<List<UserEntity>>

}