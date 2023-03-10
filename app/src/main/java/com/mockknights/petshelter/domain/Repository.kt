package com.mockknights.petshelter.domain

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface Repository {
    suspend fun getAllPetShelter(): Flow<List<PetShelter>>
    suspend fun register(registerRequest: RegisterRequest)
    suspend fun getToken(): Flow<List<String>>
    suspend fun getShelter(id: String): Flow<PetShelter>
    suspend fun uploadPhoto(id: String, body: MultipartBody.Part)
    suspend fun updateShelter(id: String, petShelter: PetShelter)
}