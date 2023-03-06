package com.mockknights.petshelter.data.remote.mappers

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.domain.PetShelter
import javax.inject.Inject

/**
 * A mapper class to map PetShelterRemote to PetShelter and vice versa.
 */
class PetShelterMapper @Inject constructor() {
    /**
     * Maps a list of PetShelterRemote to a list of PetShelter.
     * @param petShelterListRemote The list of PetShelterRemote to be mapped.
     * @return The list of PetShelter.
     */
    fun mapPetShelterRemoteToPresentation(petShelterListRemote: List<PetShelterRemote>): List<PetShelter> {
        return petShelterListRemote.map { mapOnePetShelterRemoteToPresentation(it) }
    }

    /**
     * Maps one PetShelterRemote to a PetShelter.
     * @param petShelterRemote The list of PetShelter to be mapped.
     * @return The petShelter presentation.
     */
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

    /**
     * Maps a petShelter presentation to a petShelter remote.
     * @param petShelter The petShelter presentation to be mapped.
     * @return The petShelter remote.
     */
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