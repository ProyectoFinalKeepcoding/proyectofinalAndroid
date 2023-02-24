package com.mockknights.petshelter.domain

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllPetShelter(): Flow<List<PetShelter>>
    suspend fun getToken(): Flow<String>
    suspend fun register(registerRequest: RegisterRequest)
}