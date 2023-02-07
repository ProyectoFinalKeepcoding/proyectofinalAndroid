package com.mockknights.petshelter.domain

import android.location.Address
import com.mockknights.petshelter.data.remote.response.ShelterType
import java.util.*

data class PetShelter (
    val id: UUID,
    val name: String,
    val password: String,
    val phoneNumber: String,
    //val address: Address,
    //val shelterType: ShelterType,
    val photoURL: String
        )
