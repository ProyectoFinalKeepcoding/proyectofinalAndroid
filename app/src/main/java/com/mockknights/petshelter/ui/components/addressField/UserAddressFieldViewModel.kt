package com.mockknights.petshelter.ui.components.addressField

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
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddressFieldViewModel@Inject constructor(private val repository: Repository): ViewModel(){

    lateinit var placesClient: PlacesClient
    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    private var job: Job? = null

    private val _currentLatLong = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLatLong: MutableStateFlow<LatLng> get() = _currentLatLong


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
                    viewModelScope.launch(Dispatchers.IO) {
                        currentLatLong.emit(it.place.latLng!!)
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}

data class AutocompleteResult(
    val address: String,
    val placeId: String
)
