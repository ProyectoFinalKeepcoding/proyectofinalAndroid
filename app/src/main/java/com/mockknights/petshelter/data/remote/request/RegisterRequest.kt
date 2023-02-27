package com.mockknights.petshelter.data.remote.request

import com.mockknights.petshelter.data.remote.response.Address
import com.squareup.moshi.Json


data class RegisterRequest(
    val name: String = "",
    val password: String = "",
    val phone: String = "",
    val address: Address = Address(0.0, 0.0),
    val shelterType: String = "",
)