package com.mockknights.petshelter.ui.map

import com.mockknights.petshelter.domain.PetShelter

/**
 * This class represents the state of the map data.
 */
sealed class MapShelterListState {
    /**
     * This class represents the state of the map data when it is successfully loaded. The
     * [petShelters] property contains the list of pet shelters to be loaded.
     */
    data class Success(val petShelters: List<PetShelter>) : MapShelterListState()

    /**
     * This class represents the state of the map data when it is not successfully loaded. The
     * [message] property contains the error message.
     */
    data class Error(val message: String) : MapShelterListState()

    /**
     * This class represents the state of the map data when it is Loading. It is the default state
     * of the detail data.
     */
    object Loading : MapShelterListState()
}