package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import retrofit2.http.GET
import retrofit2.http.Path

interface PetShelterAPI {

    @GET("auth/signin")
    suspend fun getToken(): List<String>

    @GET("auth/signup")
    suspend fun register()

    @GET("shelters")
    suspend fun getAllPetShelter(): List<PetShelterRemote>

    @GET("shelters/{id}")
    suspend fun getShelter(@Path("id") id: String): PetShelterRemote


}