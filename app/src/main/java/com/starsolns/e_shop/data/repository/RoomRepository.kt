package com.starsolns.e_shop.data.repository

import androidx.lifecycle.LiveData
import com.starsolns.e_shop.data.room.ShopDao
import com.starsolns.e_shop.model.UserEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class RoomRepository @Inject constructor(
    private val shopDao: ShopDao
) {

    suspend fun insert(userEntity: UserEntity){
        shopDao.insertUser(userEntity)
    }

    suspend fun updateProfile(userEntity: UserEntity){
        shopDao.updateProfile(userEntity)
    }

    fun getUserProfile(userId: String): Flow<List<UserEntity>>{
        return shopDao.userProfile(userId)
    }

}