package com.mockknights.petshelter.data.remote.response

import com.mockknights.petshelter.domain.ShelterType
import com.squareup.moshi.Json

/**
 * A pet shelter remote model.
 * @param id The id of the shelter.
 * @param name The name of the shelter.
 * @param password The password of the shelter account.
 * @param phoneNumber The phone number of the shelter.
 * @param address The address of the shelter in latitude and longitude.
 * @param shelterType The type of the shelter.
 * @param photoURL The url of the shelter photo.
 */
data class PetShelterRemote (
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String?,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "address") val address: Address,
    @Json(name = "shelterType") val shelterType: ShelterType,
    @Json(name = "photoURL") val photoURL: String?
    )

/**
 * A shelter address remote model.
 * @param latitude The latitude of the shelter.
 * @param longitude The longitude of the shelter.
 */
data class Address (
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
        )

