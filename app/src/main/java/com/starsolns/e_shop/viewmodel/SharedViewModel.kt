package com.starsolns.e_shop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.starsolns.e_shop.data.datastore.DatastoreRepository
import com.starsolns.e_shop.data.repository.RoomRepository
import com.starsolns.e_shop.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val roomRepository: RoomRepository,
    application: Application
): AndroidViewModel(application) {

    val getSellerUserName = datastoreRepository.readSellerUserName.asLiveData()

    fun saveSellerUserName(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepository.saveUserName(username)
        }
    }


    fun insertUserProfile(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.insert(userEntity)
        }
    }

    fun getUserProfile(userId: String): LiveData<List<UserEntity>>{
           return  roomRepository.getUserProfile(userId).asLiveData()
    }

    fun updateUserProfile(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.updateProfile(userEntity)
        }
    }

}