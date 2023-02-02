package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>>
}