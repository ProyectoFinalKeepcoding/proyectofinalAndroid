package com.mockknights.petshelter.domain

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllPetShelter(): Flow<List<PetShelter>>
    suspend fun getToken(): Flow<String>

    suspend fun getShelter(id: String): Flow<PetShelter>
}