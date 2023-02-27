package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: PetShelterAPI): RemoteDataSource {

    override suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>> {
        val result = api.getAllPetShelter()
        return flow { emit(result) }
    }

    override suspend fun getToken(): Flow<List<String>> {
        val result = api.getToken()
        return flow { emit(result) }
    }

    override suspend fun register(registerRequest: RegisterRequest) {
        api.register(registerRequest)
    }

    override suspend fun getShelter(id: String): Flow<PetShelterRemote> {
        val result = api.getShelter(id)
        return flow { emit(result) }
    }

    override suspend fun uploadPhoto(id: String, body: MultipartBody.Part) {
        api.uploadPhoto(id, body)
    }

    override suspend fun updateShelter(id: String, shelter: PetShelterRemote) {
        api.updateShelter(id, shelter)
    }
}