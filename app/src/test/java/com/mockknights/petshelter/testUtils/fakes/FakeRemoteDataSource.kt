package com.mockknights.petshelter.testUtils.fakes

import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakeData.FakePetShelterData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class FakeRemoteDataSource: RemoteDataSource {

    var updatedShelter = FakePetShelterData.getEmptiedRemotePetShelter()
    override suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>> {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun register(registerRequest: RegisterRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getShelter(id: String): Flow<PetShelterRemote> {
        return FakeDetailData.getDetail(id)
    }

    override suspend fun uploadPhoto(id: String, body: MultipartBody.Part) {
        TODO("Not yet implemented")
    }

    override suspend fun updateShelter(id: String, shelter: PetShelterRemote) {
        updatedShelter = shelter
    }

}