package com.mockknights.petshelter.testUtils.fakes

import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakeData.FakeLoginData
import com.mockknights.petshelter.testUtils.fakeData.FakeMapData
import com.mockknights.petshelter.testUtils.fakeData.FakePetShelterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class FakeRemoteDataSource: RemoteDataSource {

    var updatedShelter = FakePetShelterData.getEmptiedRemotePetShelter()
    var returnValidToken = true
    var returnValidPetShelterList = true

    override suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>> {
        return if (returnValidPetShelterList)
            flow { emit(FakeMapData.getPetShelterList()) }
        else
            flow { emit(emptyList()) }
    }

    override suspend fun getToken(): Flow<List<String>> {
        return if(returnValidToken)
            FakeLoginData.getToken(FakeLoginData.validUser, FakeLoginData.validPassword)
        else
            FakeLoginData.getToken("invalid", "invalid")
    }

    override suspend fun register(registerRequest: RegisterRequest) {
        if(registerRequest.name == FakePetShelterData.getEmptiedPetShelter().name)
            throw Exception("Invalid data")
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