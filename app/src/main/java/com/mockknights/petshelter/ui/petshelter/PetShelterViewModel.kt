package com.mockknights.petshelter.ui.petshelter

import androidx.lifecycle.ViewModel
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PetShelterViewModel @Inject constructor(private val repository: Repository): ViewModel() {
}