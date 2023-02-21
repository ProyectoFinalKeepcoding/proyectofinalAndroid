package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: PetShelterAPI): RemoteDataSource {

    override suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>> {
        val result = api.getAllPetShelter()
        return flow { emit(result) }
    }

    override suspend fun getToken(): Flow<String> {
        val result = api.getToken()
        return flow { emit(result) }
    }

    override suspend fun getShelter(id: String): Flow<PetShelterRemote> {
        val result = api.getShelter(id)
        return flow { emit(result) }
    }
}