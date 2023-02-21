package com.mockknights.petshelter.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(private val repository: Repository): ViewModel() {

    private val _detailState = MutableStateFlow(PetShelter("", "", "","", Address(0.0, 0.0), ShelterType.PARTICULAR.stringValue, ""))
    val detailState: MutableStateFlow<PetShelter> get() = _detailState

    fun getShelterDetail(id: String) {
        viewModelScope.launch {
            val result = repository.getShelter(id).flowOn(Dispatchers.IO)
            _detailState.value = result.first()
        }
    }

    fun updateShelterType(shelterType: ShelterType) {
        val newDetailState = _detailState.value.copy(shelterType = shelterType.stringValue)
        viewModelScope.launch (Dispatchers.IO) {
            _detailState.value = newDetailState
        }
    }
}