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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(private val repository: Repository): ViewModel() {

//    private val _detailState = MutableStateFlow(PetShelter("", "", "","", Address(0.0, 0.0), ShelterType.PARTICULAR, ""))
//    val detailState: MutableStateFlow<PetShelter> get() = _detailState

    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)
    val detailState: MutableStateFlow<DetailState> get() = _detailState


    fun getShelterDetail(id: String) {
        viewModelScope.launch {
            val result = repository.getShelter(id).flowOn(Dispatchers.IO)
            _detailState.value = DetailState.Success(result.first())
        }
    }

    fun onUpdatedShelterType(shelterType: ShelterType) {
        updateShelterType(shelterType)
    }
    private fun updateShelterType(shelterType: ShelterType) {
        val updatedShelter = (_detailState.value as DetailState.Success).petShelter.copy(shelterType = shelterType)
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    fun onUpdatedPhone(phone: String) {
        updatePhone(phone)
    }
    private fun updatePhone(phone: String) {
        val updatedShelter = (_detailState.value as DetailState.Success).petShelter.copy(phoneNumber = phone)
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    fun onUpdatedAddress(latitude: String, longitude: String) {
        updateAddress(latitude, longitude)
    }
    private fun updateAddress(latitude: String, longitude: String) {
        val updatedShelter = (_detailState.value as DetailState.Success).petShelter.copy(address = Address(latitude.toDouble(), longitude.toDouble()))
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    fun onEditName(name: String) {
        updateUserName(name)
    }
    private fun updateUserName(name: String) {
        val updatedShelter = (_detailState.value as DetailState.Success).petShelter.copy(name = name)
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    private fun setValueOnIOThread(value: DetailState) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailState.value = value
        }
    }

    fun onImageClicked() {
        updateImage()
    }
    private fun updateImage() {
        val petShelter = (_detailState.value as DetailState.Success).petShelter
        if (petShelter.photoURL.isNotEmpty()) return // already has an image url, not needed to set one
        val updatedShelter = (_detailState.value as DetailState.Success).petShelter.copy(photoURL = "${petShelter.id}.png")
        setValueOnIOThread(DetailState.Success(updatedShelter))
        Log.d("DetailViewModel", "Image url is now: ${(detailState.value as DetailState.Success).petShelter.photoURL}")
    }

}


