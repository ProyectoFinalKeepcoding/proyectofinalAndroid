package com.mockknights.petshelter.data.remote.mappers

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.domain.PetShelter
import javax.inject.Inject

class PetShelterMapper @Inject constructor() {

    fun mapPetShelterRemoteToPresentation(petShelterListRemote: List<PetShelterRemote>): List<PetShelter> {
        return petShelterListRemote.map { mapOnePetShelterRemoteToPresentation(it) }
    }

    fun mapOnePetShelterRemoteToPresentation(petShelterRemote: PetShelterRemote): PetShelter {
        return PetShelter(
            petShelterRemote.id,
            petShelterRemote.name,
            petShelterRemote.password ?: "",
            petShelterRemote.phoneNumber,
            petShelterRemote.address,
            petShelterRemote.shelterType,
            petShelterRemote.photoURL ?: ""

        )
    }

    fun mapOnePetShelterPresentationToRemote(petShelter: PetShelter): PetShelterRemote {
        return PetShelterRemote(
            petShelter.id,
            petShelter.name,
            petShelter.password ?: "",
            petShelter.phoneNumber,
            petShelter.address,
            petShelter.shelterType,
            petShelter.photoURL ?: ""
        )
    }
}