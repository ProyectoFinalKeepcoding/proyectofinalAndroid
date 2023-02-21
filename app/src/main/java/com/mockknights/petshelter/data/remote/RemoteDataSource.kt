package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>>
    suspend fun getToken(): Flow<String>

    suspend fun getShelter(id: String): Flow<PetShelterRemote>
}

