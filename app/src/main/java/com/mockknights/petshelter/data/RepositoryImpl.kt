package com.mockknights.petshelter.data

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.mappers.PetShelterMapper
import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private var sharedPreferences: SharedPreferences,
    private val mapper: PetShelterMapper
    ) : Repository {


    override suspend fun getAllPetShelter(): Flow<List<PetShelter>> {
        val remoteResult = remoteDataSource.getAllPetShelter()
        val result = remoteResult.map { petShelterList -> mapper.mapPetShelterRemoteToPresentation(petShelterList) }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun getToken(): Flow<List<String>> {
        val response = remoteDataSource.getToken()
        val token = response.firstOrNull()?.get(0)
        if(!token.isNullOrEmpty())
        {
            sharedPreferences.edit().putString("TOKEN", token).apply()
            //LOG
            sharedPreferences.getString("TOKEN", token)?.let { Log.d("TOKEN EN REPOSITORY", it) }
        }
        return response
    }

    override suspend fun register(registerRequest: RegisterRequest) {
        remoteDataSource.register(registerRequest)
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