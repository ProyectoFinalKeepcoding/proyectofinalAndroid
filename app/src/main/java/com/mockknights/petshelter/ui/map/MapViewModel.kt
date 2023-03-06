package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.mockknights.petshelter.R
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.lang.StrictMath.pow
import java.lang.StrictMath.toRadians
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * ViewModel for the Map screen.
 * @param repository The repository that is used to manage the data.
 * @param coroutineDispatcher The IO dispatcher that is used to run the coroutines.
 */
@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel() {
    /**
     * The state of the pet shelters shown in the map screen. When working with the map state inside
     * the map view model, use this value. The default value is [MapShelterListState.Loading].
     */
    private val _mapShelterListState = MutableStateFlow<MapShelterListState>(MapShelterListState.Loading)
    /**
     * The state of the pet shelters shown in the map screen visible outside the map view model.
     * The default value is [MapShelterListState.Loading].
     */
    val mapShelterListState: MutableStateFlow<MapShelterListState> get() = _mapShelterListState

    /**
     * The state of the pet shelters shown in the modal bottom sheet. When working with the modal
     * bottom sheet state inside the map view model, use this value. The default value is an empty
     * list.
     * */
    private val _modalShelterList = MutableStateFlow(emptyList<PetShelter>())
    /**
     * The state of the pet shelters shown in the modal bottom sheet visible outside the map view
     * model. The default value is an empty list.
     */
    val modalShelterList: MutableStateFlow<List<PetShelter>> get() = _modalShelterList

    /**
     * The state of the bottom sheet. When working with the bottom sheet state inside the map view
     * model use this value. The default value is [BottomSheetValue.Collapsed].
     */
    private val _sheetState = MutableStateFlow(BottomSheetState(initialValue = BottomSheetValue.Collapsed))
    /**
     * The state of the bottom scaffold that is visible outside the map view model. The default value of
     * its bottom sheet is [BottomSheetValue.Collapsed].
     */
    val bottomSheetScaffoldState = BottomSheetScaffoldState(
        bottomSheetState = _sheetState.value,
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )

    /**
     * The state of the location permission. When working with the location permission state inside
     * the map view model use this value. The default value is false.
     */
    private val _locationPermissionGranted = mutableStateOf(false)
    /**
     * The state of the location permission visible outside the map view model. The default value is
     * false.
     */
    val locationPermissionGranted: MutableState<Boolean> get() = _locationPermissionGranted

    /**
     * The state of the current user location. When working with the current user location state inside
     * the map view model use this value. The default value is the latitude and longitude of Madrid.
     */
    private val _currentUserLocation = mutableStateOf(LatLng(40.4167047, -3.7035825)) // Madrid by default
    /**
     * The state of the current user location visible outside the map view model. The default value is
     * the latitude and longitude of Madrid.
     */
    val currentUserLocation: MutableState<LatLng> get() = _currentUserLocation

    /**
     * The state of the camera position. When working with the camera position state inside the map
     * view model use this value. The default value is the latitude and longitude of Madrid and a
     * zoom of 6.
     */
    private val _cameraPositionState = MutableStateFlow(CameraPositionState(CameraPosition.fromLatLngZoom(currentUserLocation.value, 6f)))
    /**
     * The state of the camera position visible outside the map view model. The default value is the
     * latitude and longitude of Madrid and a zoom of 6.
     */
    val cameraPositionState: MutableStateFlow<CameraPositionState> get() = _cameraPositionState

    /**
     * Sets the value of the [mapShelterListState] on the main thread.
     */
    private fun setValueOnMainThreadShelter(value: MapShelterListState) {
        viewModelScope.launch(Dispatchers.Main) {
            _mapShelterListState.value = value
        }
    }

    /**
     * Initializes the map view model fetching the pet shelters from the repository.
     */
    init {
        getPetShelters()
    }

    /**
     * Gets the pet shelters from the repository and sets the value of the [mapShelterListState] on the
     * main thread. If the pet shelter list is empty, it sets the value of the [mapShelterListState]
     * to [MapShelterListState.Error] with the message "Empty pet shelter list".
     * @throws Exception if the pet shelter list is empty. The message of the exception is "Empty pet
     * shelter list".
     */
    private fun getPetShelters() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val petShelters = repository.getAllPetShelter().flowOn(coroutineDispatcher)
                if(petShelters.first().isEmpty()) throw Exception("Empty pet shelter list")
                setValueOnMainThreadShelter(MapShelterListState.Success(petShelters.first()))
            } catch (e: Exception) {
                setValueOnMainThreadShelter(MapShelterListState.Error(e.message.toString()))
            }
        }
    }

    /**
     * Sets the value of the [_mapShelterListState] to the pet shelter with the name [shelterName].
     * @param shelterName the name of the pet shelter to set.
     */
    fun setModalShelter(shelterName: String) {
        val petShelters = (_mapShelterListState.value as? MapShelterListState.Success)?.petShelters ?: listOf()
        val modalPetShelter = petShelters.filter { it.name == shelterName }
        viewModelScope.launch(Dispatchers.Main) {
            _modalShelterList.value = modalPetShelter
        }
    }

    /**
     * Toggles the modal bottom sheet state between [BottomSheetValue.Collapsed] and
     * [BottomSheetValue.Expanded].
     */
    fun toggleModal(coroutineScope: CoroutineScope) {
        // Needed to use composable functions
        coroutineScope.launch {
            when (_sheetState.value.isCollapsed) {
                true -> _sheetState.value.expand()
                false -> _sheetState.value.collapse()
            }
        }
    }

    /**
     * Expands the modal bottom sheet state of the bottom scaffold.
     */
    fun collapseModal(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            _sheetState.value.collapse()
        }
    }

    /**
     * Gets the icon of the pet shelter by its type.
     * @param shelterType the type of the pet shelter.
     */
    fun getShelterIconByShelterType(shelterType: String): Int {
        return when (shelterType) {
            ShelterType.PARTICULAR.stringValue -> R.drawable.particular
            ShelterType.LOCAL_GOVERNMENT.stringValue -> R.drawable.towncouncil
            ShelterType.VETERINARY.stringValue -> R.drawable.veterinary
            ShelterType.SHELTER_POINT.stringValue -> R.drawable.animalshelter
            ShelterType.KIWOKO_STORE.stringValue -> R.drawable.kiwoko
            else -> R.drawable.questionmark
        }
    }

    /**
     * Sets the value of the [locationPermissionGranted] to [isGranted] and if the permission is
     * granted, it gets the user location.
     * @param isGranted the value of the permission.
     * @param context the context of the application.
     */
    fun onPermissionRequestCompleted(isGranted: Boolean, context: Context) {
        // Change the status of the permission
        _locationPermissionGranted.value = isGranted
        // If the permission is granted, get the user location
        if (isGranted) { setCurrentUserLocation(context) }
    }

    /**
     * Sets the value of the [currentUserLocation] to the current user location and sets the value of
     * the [cameraPositionState] to the current user location and a zoom of 6.
     * @context the context of the application.
     */
    @SuppressLint("MissingPermission") // Permission is checked with locationPermissionState
    private fun setCurrentUserLocation(context: Context) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (locationPermissionGranted.value) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {nullCheckedLocation ->
                    _currentUserLocation.value = LatLng(nullCheckedLocation.latitude, nullCheckedLocation.longitude)
                    _cameraPositionState.value = CameraPositionState(CameraPosition.fromLatLngZoom(currentUserLocation.value, 6f))
                }
            }
        }
    }

    /**
     * Moves the camera to the user location.
     * @param coroutineScope the coroutine scope of the application.
     */
    fun moveCameraToUserLocation(coroutineScope: CoroutineScope) {
        val update = CameraUpdateFactory.newCameraPosition(CameraPosition(currentUserLocation.value, 9f, 0f, 0f))
        coroutineScope.launch {
            _cameraPositionState.value.animate(update)
        }
    }

    /**
     * Moves the camera to the location.
     * @param coroutineScope the coroutine scope of the application.
     * @param location the location to move the camera to.
     */
    private fun moveCameraToLocation(coroutineScope: CoroutineScope, location: LatLng) {
        val update = CameraUpdateFactory.newCameraPosition(CameraPosition(location, 9f, 0f, 0f))
        coroutineScope.launch {
            _cameraPositionState.value.animate(update)
        }
    }

    /**
     * Moves the camera to the closest shelter.
     * @param coroutineScope the coroutine scope of the application.
     */
    fun onClosestShelterClicked(coroutineScope: CoroutineScope) {
        // If there is no shelter, do nothing
        if ((_mapShelterListState.value as? MapShelterListState.Success)?.petShelters.isNullOrEmpty()) return
        // Get closest shelter and move camera to it
        val closestShelter = getClosestShelter()
        closestShelter?.let { unwrappedClosestShelter ->
            modalShelterList.value = listOf(unwrappedClosestShelter)
            toggleModal(coroutineScope)
            moveCameraToLocation(
                coroutineScope = coroutineScope,
                location = LatLng(unwrappedClosestShelter.address.latitude, unwrappedClosestShelter.address.longitude)
            )
        }
    }

    /**
     * Gets the closest shelter to the user location.
     * @return the closest shelter to the user location.
     */
    fun getClosestShelter(): PetShelter? {
        // If there is no shelter, return an empty shelter
        if ((_mapShelterListState.value as? MapShelterListState.Success)?.petShelters.isNullOrEmpty()) return null
        val petShelters = (_mapShelterListState.value as MapShelterListState.Success).petShelters // Checked cast
        // When permission is granted, the user location is the origin
        val origin: LatLng? = if (locationPermissionGranted.value) currentUserLocation.value else null
        // If there is an origin, get the closest shelter
        var closestShelter: PetShelter? = null
        origin?.let {unwrappedOrigin ->
            closestShelter = petShelters
                .filter { petShelter ->
                    petShelter.address.latitude - unwrappedOrigin.latitude <= 1 &&
                            petShelter.address.longitude - unwrappedOrigin.longitude <= 1
                }
                .sortedWith { shelter1, shelter2 ->
                    val coordinatesShelter1 =
                        LatLng(shelter1.address.latitude, shelter1.address.longitude)
                    val coordinatesShelter2 =
                        LatLng(shelter2.address.latitude, shelter2.address.longitude)
                    when {
                        distanceBetween(unwrappedOrigin, coordinatesShelter1) == distanceBetween(
                            unwrappedOrigin,
                            coordinatesShelter2
                        ) -> 0
                        distanceBetween(unwrappedOrigin, coordinatesShelter1) < distanceBetween(
                            unwrappedOrigin,
                            coordinatesShelter2
                        ) -> -1
                        else -> 1
                    }
                }
                .firstOrNull()
        }
        return closestShelter
    }

    /**
     * Gets the distance between two points using the Haversine formula.
     * @param origin the origin point.
     * @param destination the destination point.
     */
    private fun distanceBetween(origin: LatLng, destination: LatLng): Double {
        val earthRadius = 6371 // Radius of the earth in km
        val dLat = Math.toRadians(destination.latitude - origin.latitude)  // Increment of latitude
        val dLon = Math.toRadians(destination.longitude - origin.longitude) // Increment of longitude
        // Haversine formula
        val a = pow(sin(dLat / 2), 2.0) +
                cos(toRadians(origin.latitude)) * cos(Math.toRadians(destination.latitude)) *
                pow(sin(dLon / 2), 2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    /**
     * Starts a new calling intent with the phone number of the shelter.
     * @param phone the phone number of the shelter.
     * @param localContext the context of the application.
     */
    fun onCall(phone: String, localContext: Context) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        localContext.startActivity(intent)
    }

    /**
     * Starts a new navigation intent with the address of the shelter in Google Maps.
     * @param address the address of the shelter.
     * @param localContext the context of the application.
     */
    fun onGo(address: Address, localContext: Context) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${address.latitude},${address.longitude}&mode=w") // w = walking
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(localContext.packageManager)?.let {
            localContext.startActivity(mapIntent)
        }
    }
}