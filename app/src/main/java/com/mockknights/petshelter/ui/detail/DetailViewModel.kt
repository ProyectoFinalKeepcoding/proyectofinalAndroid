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

    private val _detailState = MutableStateFlow(PetShelter("", "", "","", Address(0.0, 0.0), ShelterType.PARTICULAR, ""))
    val detailState: MutableStateFlow<PetShelter> get() = _detailState

    fun getShelterDetail(id: String) {
        viewModelScope.launch {
            val result = repository.getShelter(id).flowOn(Dispatchers.IO)
            _detailState.value = result.first()
        }
    }

    fun onUpdatedShelterType(shelterType: ShelterType) {
        updateShelterType(shelterType)
    }
    private fun updateShelterType(shelterType: ShelterType) {
        val newDetailState = _detailState.value.copy(shelterType = shelterType)
        viewModelScope.launch (Dispatchers.IO) {
            _detailState.value = newDetailState
        }
    }

    fun onUpdatedDataField(text: String, fieldType: DetailFieldType) {
        updateDataField(text, fieldType)
    }
    private fun updateDataField(text: String, fieldType: DetailFieldType) {
        val newDetailState = getDetailStateByFieldType(text, fieldType)
        viewModelScope.launch (Dispatchers.IO) {
            _detailState.value = newDetailState
        }
    }
    private fun getDetailStateByFieldType(text: String, fieldType: DetailFieldType): PetShelter {
        return when (fieldType) {
            DetailFieldType.ADDRESS -> _detailState.value // TODO: Implement after managing addresses with google api
            DetailFieldType.PHONE -> _detailState.value.copy(phoneNumber = text)
        }
    }
}

enum class DetailFieldType {
    ADDRESS, PHONE
}