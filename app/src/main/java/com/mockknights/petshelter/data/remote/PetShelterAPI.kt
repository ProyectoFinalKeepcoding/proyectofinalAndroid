package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import retrofit2.http.GET

interface PetShelterAPI {

    @GET("auth/signin")
    suspend fun getToken(): String

    @GET("auth/signup")
    suspend fun register()

    @GET("shelters")
    suspend fun getAllPetShelter(): List<PetShelterRemote>




}