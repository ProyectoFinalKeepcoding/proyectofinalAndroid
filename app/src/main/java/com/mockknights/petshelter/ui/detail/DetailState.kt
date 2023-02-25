package com.mockknights.petshelter.ui.detail

import com.mockknights.petshelter.domain.PetShelter

sealed class DetailState {
    data class Success(val petShelter: PetShelter) : DetailState()
    data class Error(val message: String) : DetailState()
    object Loading : DetailState()
}
