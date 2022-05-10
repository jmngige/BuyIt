package com.starsolns.e_shop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.starsolns.e_shop.data.datastore.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    application: Application
): AndroidViewModel(application) {

    val readImageString  = datastoreRepository.readImageString.asLiveData()

    fun saveImageString(imageString: String){
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepository.saveImageString(imageString)
        }
    }

}