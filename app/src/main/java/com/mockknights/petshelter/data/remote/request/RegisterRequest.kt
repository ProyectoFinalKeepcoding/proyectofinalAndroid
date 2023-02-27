package com.mockknights.petshelter.data.remote.request

import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.ShelterType
import com.squareup.moshi.Json


data class RegisterRequest(
    val name: String = "",
    val password: String = "",
    val address: Address = Address(0.0, 0.0),
    val phoneNumber: String = "",
    val shelterType: ShelterType = ShelterType.PARTICULAR,
)