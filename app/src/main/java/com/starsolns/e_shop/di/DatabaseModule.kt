package com.starsolns.e_shop.di

import android.content.Context
import androidx.room.Room
import com.starsolns.e_shop.data.room.ShopDatabase
import com.starsolns.e_shop.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDbInstance(
        @ApplicationContext context: Context
    ) =
        Room.databaseBuilder(
            context,
            ShopDatabase::class.java,
            DATABASE_NAME
        ).build()


    @Singleton
    @Provides
    fun provideRecipeDao(shopDatabase: ShopDatabase) = shopDatabase.shopDao()

}