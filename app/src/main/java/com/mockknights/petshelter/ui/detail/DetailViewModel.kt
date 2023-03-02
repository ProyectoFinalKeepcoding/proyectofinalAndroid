package com.mockknights.petshelter.ui.detail

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel() {

    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)
    val detailState: MutableStateFlow<DetailState> get() = _detailState

    fun getShelterDetail(id: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val result = repository.getShelter(id).flowOn(coroutineDispatcher)
            try {
                _detailState.value = DetailState.Success(result.first())
            } catch (e: NoSuchElementException) {
                _detailState.value = DetailState.Error("No shelter found with id: $id")
            }
        }
    }

    fun onUpdatedShelterType(shelterType: ShelterType) {
        updateShelterType(shelterType)
    }
    private fun updateShelterType(shelterType: ShelterType) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(shelterType = shelterType)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    fun onUpdatedPhone(phone: String) {
        updatePhone(phone)
    }
    private fun updatePhone(phone: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(phoneNumber = phone)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    fun onUpdatedAddress(latitude: String, longitude: String) {
        updateAddress(latitude, longitude)
    }
    private fun updateAddress(latitude: String, longitude: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(address = Address(latitude.toDouble(), longitude.toDouble()))
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    fun onEditName(name: String) {
        updateUserName(name)
    }
    private fun updateUserName(name: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(name = name)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    fun onImageClicked() {
        updateImage()
    }
    private fun updateImage() {
        val detailStateSuccess = _detailState.value as? DetailState.Success
        detailStateSuccess ?: return
        val petShelter = detailStateSuccess.petShelter
        if (petShelter.photoURL.isNotEmpty()) return // already has an image url, not needed to set one
        val updatedShelter = petShelter.copy(photoURL = "${petShelter.id}.png")
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    fun onSaveClicked() {
        val petShelter = (_detailState.value as? DetailState.Success)?.petShelter
        petShelter?.let {
            val id = it.id
            viewModelScope.launch(coroutineDispatcher) {
                repository.updateShelter(id, it)
            }
        }
    }

    private fun setValueOnIOThread(value: DetailState) {
        viewModelScope.launch(coroutineDispatcher) {
            _detailState.value = value
        }
    }
}


