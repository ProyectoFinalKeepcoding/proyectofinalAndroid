package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import retrofit2.http.GET

interface PetShelterAPI {

    @GET("shelters")
    suspend fun getAllPetShelter(): List<PetShelterRemote>


}