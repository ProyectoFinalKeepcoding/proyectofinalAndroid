package com.mockknights.petshelter.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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

    private val _detailState = MutableStateFlow(PetShelter("", "", "","", Address(0.0, 0.0), ShelterType.PARTICULAR, ""))
    val detailState: MutableStateFlow<PetShelter> get() = _detailState

    lateinit var placesClient: PlacesClient

    val locationAutofill = mutableStateListOf<AutocompleteResult>()

    private var job: Job? = null

    private var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))

    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                // Suggestions are limited to Europe and north of Africa
                .setLocationBias(
                    RectangularBounds.newInstance(
                        LatLng(30.0, 30.0),
                        LatLng(70.0, 70.0)
                    )
                )
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        AutocompleteResult(
                            it.getFullText(null).toString(),
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener {
                if (it != null) {
                    currentLatLong = it.place.latLng!!
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }




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


data class AutocompleteResult(
    val address: String,
    val placeId: String
)
