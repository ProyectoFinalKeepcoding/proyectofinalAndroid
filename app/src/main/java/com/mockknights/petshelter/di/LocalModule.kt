package com.mockknights.petshelter.di

import android.content.Context
import androidx.room.Room
import com.mockknights.petshelter.data.local.PetShelterDAO
import com.mockknights.petshelter.data.local.PetShelterDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PetShelterDataBase {
        return Room.databaseBuilder(context, PetShelterDataBase::class.java, "database-name").build()
    }

    @Provides
    fun provideDao(database: PetShelterDataBase): PetShelterDAO {
        return database.getDAO()
    }
}