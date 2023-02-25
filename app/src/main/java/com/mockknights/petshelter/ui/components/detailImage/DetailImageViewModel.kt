package com.mockknights.petshelter.ui.components.detailImage

import androidx.lifecycle.ViewModel
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailImageViewModel@Inject constructor(private val repository: Repository): ViewModel() {

    fun uploadImage() {
        TODO("Not yet implemented")
    }

}
