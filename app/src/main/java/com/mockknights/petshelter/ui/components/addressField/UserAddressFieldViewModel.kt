package com.mockknights.petshelter.ui.components.addressField

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for an user address field.
 */
@HiltViewModel
class UserAddressFieldViewModel@Inject constructor(): ViewModel(){

    /**
     * The places client to manage the places API. It has to be initialized before using it.
     */
    lateinit var placesClient: PlacesClient

    /**
     * The list of places that match the user query.
     */
    val locationAutofill = mutableStateListOf<AutocompleteResult>()

    /**
     * The job that is used to perform the search.
     */
    private var job: Job? = null

    /**
     * The geocoder to perform inverse geocoding.
     */
    lateinit var geoCoder: Geocoder

    /**
     * The current address of the user. When working with this value in this view model, use this value.
     * The default value is LatLng(0.0, 0.0)
     */
    private val _currentLatLong = MutableStateFlow(LatLng(0.0, 0.0))

    /**
     * The current address of the user visible outside this view model. The default value is LatLng(0.0, 0.0)
     */
    val currentLatLong: MutableStateFlow<LatLng> get() = _currentLatLong

    /**
     * The current address of the user as a string. When working with this value in this view model, use this value.
     * The default value is ""
     */
    private val _addressAsString = MutableStateFlow("")
    /**
     * The current address of the user as a string visible outside this view model. The default value is ""
     */
    val addressAsString: MutableStateFlow<String> get() = _addressAsString

    /**
     * Updates the current address of the user as a string.
     */
    fun updateAddressAsString(address: String) {
        _addressAsString.value = address
    }

    /**
     * Searches for places that match the user query and updates the [locationAutofill] list.
     * @param query The user query.
     */
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

    /**
     * Gets the coordinates of the place selected by the user.
     * @param result The result of the place selected by the user.
     */
    fun getCoordinates(result: AutocompleteResult, onSuccess: (LatLng) -> Unit) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener {
                if (it != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        currentLatLong.emit(it.place.latLng!!)
                        _addressAsString.emit(result.address)
                        onSuccess(it.place.latLng!!)
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    /**
     * Gets the address of the user from the coordinates.
     */
    fun onRequestAddressAsString(currentAddress: Address, localContext: Context): String {
        // If already have the address, return it
        if(addressAsString.value.isNotEmpty()) return addressAsString.value
        // If not, get it from the geocoder
        geoCoder = Geocoder(localContext)
        // If the API is higher than TIRAMISU, use the new Geocoder that uses a listener
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModelScope.launch(Dispatchers.IO) {
                geoCoder.getFromLocation(
                    currentAddress.latitude,
                    currentAddress.longitude,
                    1,
                    Geocoder.GeocodeListener {  addresses ->
                    if(addresses.isNotEmpty()) {
                        viewModelScope.launch(Dispatchers.IO) {
                            _addressAsString.emit(addresses[0].getAddressLine(0))
                        }
                    }
                })
            }
        // If the API is lower than TIRAMISU, use the old Geocoder that returns a list
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val address = geoCoder.getFromLocation(currentAddress.latitude, currentAddress.longitude, 1)
                val stringAddress = address?.get(0)?.getAddressLine(0)
                _addressAsString.emit(stringAddress ?: "")
            }
        }
        return addressAsString.value
    }
}

/**
 * The result of an autocomplete search.
 */
data class AutocompleteResult(
    val address: String,
    val placeId: String
)
