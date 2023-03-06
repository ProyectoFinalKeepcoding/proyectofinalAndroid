package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Remote data source to manage data from the remote server.
 */
class RemoteDataSourceImpl @Inject constructor(private val api: PetShelterAPI): RemoteDataSource {

    /**
     * Get all pet shelters from the remote server.
     * @return A flow of list of pet shelters.
     */
    override suspend fun getAllPetShelter(): Flow<List<PetShelterRemote>> {
        val result = api.getAllPetShelter()
        return flow { emit(result) }
    }

    /**
     * Get token from the remote server.
     * @return A flow of list of strings with token as the first element and an id as second element.
     */
    override suspend fun getToken(): Flow<List<String>> {
        val result = api.getToken()
        return flow { emit(result) }
    }

    /**
     * Register a new user to the remote server.
     * @param registerRequest The request body to register a new user.
     */
    override suspend fun register(registerRequest: RegisterRequest) {
        api.register(registerRequest)
    }

    /**
     * Get a pet shelter from the remote server by [id].
     * @param id The id of the pet shelter.
     * @return A flow of pet shelter containing only one element.
     */
    override suspend fun getShelter(id: String): Flow<PetShelterRemote> {
        val result = api.getShelter(id)
        return flow { emit(result) }
    }

    /**
     * Upload a photo to the remote server. The photo is stored in the [body] as a [MultipartBody.Part].
     * @param id The id of the pet shelter.
     * @param body The photo to upload as a [MultipartBody.Part].
     */
    override suspend fun uploadPhoto(id: String, body: MultipartBody.Part) {
        api.uploadPhoto(id, body)
    }

    /**
     * Update a pet shelter in the remote server. The pet shelter is stored in the [shelter] as a [PetShelterRemote].
     * @param id The id of the pet shelter.
     * @param shelter The pet shelter to update.
     */
    override suspend fun updateShelter(id: String, shelter: PetShelterRemote) {
        api.updateShelter(id, shelter)
    }
}