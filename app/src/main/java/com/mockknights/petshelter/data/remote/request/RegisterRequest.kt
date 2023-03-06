package com.mockknights.petshelter.data.remote.request

import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.ShelterType

/**
 * A register request model.
 * @param name The name of the shelter.
 * @param password The password of the shelter account.
 * @param phoneNumber The phone number of the shelter.
 * @param address The address of the shelter in latitude and longitude.
 * @param shelterType The type of the shelter.
 */
data class RegisterRequest(
    val name: String = "",
    val password: String = "",
    val address: Address = Address(0.0, 0.0),
    val phoneNumber: String = "",
    val shelterType: ShelterType = ShelterType.PARTICULAR,
)