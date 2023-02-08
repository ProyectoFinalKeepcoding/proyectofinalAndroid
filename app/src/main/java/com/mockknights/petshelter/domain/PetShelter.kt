package com.mockknights.petshelter.domain

import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.data.remote.response.ShelterType
import java.util.*

data class PetShelter (
    val id: String,
    val name: String,
    val password: String,
    val phoneNumber: String,
    //val address: Address,
    //val shelterType: ShelterType,
    val photoURL: String
        )
