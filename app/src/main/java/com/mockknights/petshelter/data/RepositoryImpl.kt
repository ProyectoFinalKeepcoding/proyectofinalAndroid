package com.mockknights.petshelter.data

import android.content.SharedPreferences
import android.util.Log
import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.mappers.PetShelterMapper
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    //private val localDataSource: LocalDataSource,
    private val sharedPreferences: SharedPreferences,
    private val mapper: PetShelterMapper
    ) : Repository {


    override suspend fun getAllPetShelter(): Flow<List<PetShelter>> {
        val remoteResult = remoteDataSource.getAllPetShelter()
        val result = remoteResult.map { petShelterList -> mapper.mapPetShelterRemoteToPresentation(petShelterList) }
        return result
    }

    override suspend fun getToken(): Flow<List<String>> {
        val response = remoteDataSource.getToken()
        val token = response.firstOrNull()?.get(0)
        if(!token.isNullOrEmpty()) {
            sharedPreferences.edit().putString("TOKEN", token).apply()
        }
        return response
    }

    override suspend fun getShelter(id: String): Flow<PetShelter> {
        return remoteDataSource.getShelter(id).map { petShelter -> mapper.mapOnePetShelterRemoteToPresentation(petShelter) }
    }

    override suspend fun uploadPhoto(id: String, body: MultipartBody.Part) {
        remoteDataSource.uploadPhoto(id, body)
    }

    override suspend fun updateShelter(id: String, shelter: PetShelter) {
        remoteDataSource.updateShelter(id, mapper.mapOnePetShelterPresentationToRemote(shelter))
    }
}