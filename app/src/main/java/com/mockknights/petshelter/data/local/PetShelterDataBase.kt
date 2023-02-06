package com.mockknights.petshelter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mockknights.petshelter.data.local.model.PetShelterLocal

@Database(entities = [PetShelterLocal::class], version = 1)
abstract class PetShelterDataBase: RoomDatabase() {
    abstract fun getDAO(): PetShelterDAO
}