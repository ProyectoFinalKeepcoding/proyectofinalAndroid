package com.mockknights.petshelter.data.remote.response

import com.squareup.moshi.Json

data class PetShelterRemote (
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String?,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "address") val address: Address,
    @Json(name = "shelterType") val shelterType: String,
    @Json(name = "photoURL") val photoURL: String?
    )


data class Address (
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
        )

