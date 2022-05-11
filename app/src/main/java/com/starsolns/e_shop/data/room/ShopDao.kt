package com.starsolns.e_shop.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.starsolns.e_shop.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update
    suspend fun updateProfile(userEntity: UserEntity)

    @Query("SELECT * FROM user_profile WHERE id LIKE :userId")
    fun userProfile(userId: String): Flow<List<UserEntity>>

}