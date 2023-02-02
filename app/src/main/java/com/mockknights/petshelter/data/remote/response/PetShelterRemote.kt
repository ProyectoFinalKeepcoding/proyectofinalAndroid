package com.mockknights.petshelter.data.remote.response

import android.location.Address
import com.squareup.moshi.Json
import java.util.UUID

data class PetShelterRemote (
    @Json(name = "id") val id: UUID,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String,
    @Json(name = "phoneNumber") val phoneNumber: String,
    //@Json(name = "address") val address: Address,
    //@Json(name = "shelterType") val shelterType: ShelterType,
    @Json(name = "photoURL") val photoURL: String
    )


data class ShelterType (
    @Json(name = "name") val name: String
        )