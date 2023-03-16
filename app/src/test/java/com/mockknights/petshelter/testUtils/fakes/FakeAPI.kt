package com.mockknights.petshelter.testUtils.fakes

import com.mockknights.petshelter.data.remote.PetShelterAPI
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import okhttp3.MultipartBody

class FakeAPI: PetShelterAPI {

    var registerCompleted = false
    var uploadCompleted = false
    var updateCompleted = false
    override suspend fun getToken(): List<String> {
        TODO("Mocked response in the dispatcher")
    }

    override suspend fun register(registerRequest: RegisterRequest) {
        registerCompleted = true
    }

    override suspend fun getAllPetShelter(): List<PetShelterRemote> {
        TODO("Mocked response in the dispatcher")
    }

    override suspend fun getShelter(id: String): PetShelterRemote {
        TODO("Mocked response in the dispatcher")

    }

    override suspend fun uploadPhoto(id: String, body: MultipartBody.Part) {
        uploadCompleted = true
    }

    override suspend fun updateShelter(id: String, shelter: PetShelterRemote) {
        updateCompleted = true
    }
}