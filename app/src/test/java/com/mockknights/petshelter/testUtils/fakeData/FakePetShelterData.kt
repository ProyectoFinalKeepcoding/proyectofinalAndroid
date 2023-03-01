package com.mockknights.petshelter.testUtils.fakeData

import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.ShelterType

object FakePetShelterData {

    fun getRemotePetShelter(): PetShelterRemote {
        return PetShelterRemote(
            id = "id",
            name = "name",
            password = "password",
            phoneNumber = "630838080",
            address = Address(40.4167047, -3.7035825),
            photoURL = "photoURL",
            shelterType = ShelterType.SHELTER_POINT
        )
    }

    fun getPetShelter(): PetShelter {
        return PetShelter(
            id = "id",
            name = "name",
            password = "password",
            phoneNumber = "630838080",
            address = Address(40.4167047, -3.7035825),
            photoURL = "photoURL",
            shelterType = ShelterType.SHELTER_POINT
        )
    }
}