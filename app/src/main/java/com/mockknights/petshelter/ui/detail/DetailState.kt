package com.mockknights.petshelter.ui.detail

import com.mockknights.petshelter.domain.PetShelter

/**
 * This class represents the state of the detail data.
 */
sealed class DetailState {
    /**
     * This class represents the state of the detail data when it is successfully loaded. The
     * [petShelter] property contains the data.
     */
    data class Success(val petShelter: PetShelter) : DetailState()

    /**
     * This class represents the state of the detail data when it is not successfully loaded. The
     * [message] property contains the error message.
     */
    data class Error(val message: String) : DetailState()

    /**
     * This class represents the state of the detail data when it is Loading. It is the default state
     * of the detail data.
     */
    object Loading : DetailState()
}
