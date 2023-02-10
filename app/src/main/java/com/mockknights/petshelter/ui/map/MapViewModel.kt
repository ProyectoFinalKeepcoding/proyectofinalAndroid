package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository): ViewModel(){

    private val _petShelter = MutableStateFlow(emptyList<PetShelter>())
    val petShelter: MutableStateFlow<List<PetShelter>> get() = _petShelter

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
            repository.getAllPetShelter().flowOn(Dispatchers.IO).collect() {
                setValueOnMainThreadShelter(it)
            }
        }

    }





    @SuppressLint("MissingPermission") //Por si hay problemas con los permisos
    fun getMyLocation() {

        val locationManager: LocationManager

        //VER SI TIENE LOS PERMISOS ACEPTADOS y SINO PEDIRLOS
        //if(ActivityCompat.getPermissionCompatDelegate(requireContext(), ACCESS_FINE_LOCATION)
        // == PackageManager.PERMISSION_GRANTED
        // && ActivityCompat.getPermissionCompatDelegate(requireContext(), ACCESS_COARSE_LOCATION)
        // == PackageManager.PERMISSION_GRANTED) {
        // LLAMAR A LA FUNCION GETMYLOCATION para obtener mi Localizacion
        //} else {
        //val permissionLauncher = ActivityResultLauncher
        //}



        //LOCATION HECHO EN CLASE
        //val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient (requireContext())
        //fusedLocationProviderClient.lastLocation.addOnSuccessListener {
        //Log.d("Success", "${it.latitude} ${it.longitude}")
        //val myLocation = LatLng(it.latitude, it.longitude)
        //}.addOnCanceledListener {
        //Log.d("Canceled", "Cancelado")
        //}.addOnFailureListener {
        //Log.d("Fallo", "Ha fallado")
        //}

    }
}