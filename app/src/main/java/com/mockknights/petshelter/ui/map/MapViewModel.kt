package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import android.content.Context
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
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _petShelter = MutableStateFlow(emptyList<PetShelter>())
    val petShelter: MutableStateFlow<List<PetShelter>> get() = _petShelter

    private val _modalShelterList = MutableStateFlow(emptyList<PetShelter>())
    val modalShelterList: MutableStateFlow<List<PetShelter>> get() = _modalShelterList

    private val _sheetState = MutableStateFlow(BottomSheetState(initialValue = BottomSheetValue.Collapsed))
    val bottomSheetScaffoldState = BottomSheetScaffoldState(
        bottomSheetState = _sheetState.value,
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )

    private val _locationPermissionGranted = mutableStateOf(false)
    val locationPermissionGranted: MutableState<Boolean> get() = _locationPermissionGranted

    private val _currentUserLocation = mutableStateOf(LatLng(40.4167047, -3.7035825)) // Madrid by default
    private val currentUserLocation: MutableState<LatLng> get() = _currentUserLocation

    private val _cameraPositionState = MutableStateFlow(CameraPositionState(CameraPosition.fromLatLngZoom(currentUserLocation.value, 6f)))
    val cameraPositionState: MutableStateFlow<CameraPositionState> get() = _cameraPositionState

    private fun setValueOnMainThreadShelter(value: List<PetShelter>) {
        viewModelScope.launch(Dispatchers.Main) {
            _petShelter.value = value
        }
    }

    init {
        getAllPetShelter()
    }

    fun getAllPetShelter() {
        viewModelScope.launch {
            try {
                repository.getAllPetShelter().flowOn(Dispatchers.IO).collect() {
                    setValueOnMainThreadShelter(it)
                }
            } catch (e: Exception) {
                Log.d("MapViewModel", e.message.toString())
            }
        }
    }

    fun setModalShelter(shelterName: String) {
        val modalPetShelter = petShelter.value.filter { it.name == shelterName }
        viewModelScope.launch(Dispatchers.Main) {
            _modalShelterList.value = modalPetShelter
        }
    }

    fun toggleModal(coroutineScope: CoroutineScope) {
        // Needed to use composable functions
        coroutineScope.launch {
            when (_sheetState.value.isCollapsed) {
                true -> _sheetState.value.expand()
                false -> _sheetState.value.collapse()
            }
        }
    }

    fun collapseModal(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            _sheetState.value.collapse()
        }
    }

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

    fun onPermissionRequestCompleted(isGranted: Boolean, context: Context) {
        // Change the status of the permission
        _locationPermissionGranted.value = isGranted
        // If the permission is granted, get the user location
        if (isGranted) { setCurrentUserLocation(context) }
    }

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

    fun moveCameraToUserLocation(coroutineScope: CoroutineScope) {
        val update = CameraUpdateFactory.newCameraPosition(CameraPosition(currentUserLocation.value, 9f, 0f, 0f))
        Log.d("CURRENTLOCATION", currentUserLocation.value.toString())
        coroutineScope.launch {
            _cameraPositionState.value.animate(update)
        }
    }
}