package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface RemoteDataSource {
    suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>>
    suspend fun getToken(): Flow<List<String>>
    suspend fun register(registerRequest: RegisterRequest)
    suspend fun getShelter(id: String): Flow<PetShelterRemote>
    suspend fun uploadPhoto(id: String, body: MultipartBody.Part)
    suspend fun updateShelter(id: String, shelter: PetShelterRemote)
}

