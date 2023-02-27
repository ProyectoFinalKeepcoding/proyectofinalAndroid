package com.mockknights.petshelter.data.remote

import com.mockknights.petshelter.data.remote.request.RegisterRequest
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PetShelterAPI {

    @GET("auth/signin")
    suspend fun getToken(): List<String>

    @GET("auth/signup")
    suspend fun register(@Body registerRequest: RegisterRequest)

    @GET("shelters")
    suspend fun getAllPetShelter(): List<PetShelterRemote>

    @GET("shelters/{id}")
    suspend fun getShelter(@Path("id") id: String): PetShelterRemote

    @Multipart
    @POST("upload/{id}")
    suspend fun uploadPhoto(
        @Path("id") id: String,
        @Part body: MultipartBody.Part
    )

    @POST("update/{id}")
    suspend fun updateShelter(
        @Path("id") id: String,
        @Body shelter: PetShelterRemote
    )

}
