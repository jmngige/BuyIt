package com.starsolns.e_shop.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.starsolns.e_shop.util.Constants.DATASTORE_PREF_NAME
import com.starsolns.e_shop.util.Constants.IMAGE_STRING_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DatastoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_PREF_NAME
    )

    suspend fun saveImageString(imageString: String){
        context.datastore.edit {
            it[IMAGE_STRING] = imageString
        }
    }

    val readImageString: Flow<String> = context.datastore.data
        .catch { exception->
            if(exception is IOException){
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }
        .map { Pref->
            Pref[IMAGE_STRING] ?: ""
        }


    companion object {
        val IMAGE_STRING = stringPreferencesKey(IMAGE_STRING_KEY)
    }
}
