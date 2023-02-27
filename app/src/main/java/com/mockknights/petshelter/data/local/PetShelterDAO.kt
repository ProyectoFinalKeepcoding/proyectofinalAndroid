package com.mockknights.petshelter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mockknights.petshelter.data.local.model.PetShelterLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface PetShelterDAO {

    @Query("SELECT * FROM petshelter")
    fun getAllPetShelter() : Flow<List<PetShelterLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPetShelter(petShelterList: List<PetShelterLocal>)
}