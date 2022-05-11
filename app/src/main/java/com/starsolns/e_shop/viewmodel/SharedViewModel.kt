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

    val userProfile: LiveData<List<UserEntity>> = roomRepository.getUserProfile().asLiveData()

    fun saveImageString(imageString: String){
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepository.saveImageString(imageString)
        }
    }

    fun insertUserProfile(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.insert(userEntity)
        }
    }

}