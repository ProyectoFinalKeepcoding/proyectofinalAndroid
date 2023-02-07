package com.mockknights.petshelter.ui.map

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository): ViewModel(){


    fun getMyLocation() {
        val locationManager: LocationManager
        //val fusedLocationProviderClient: LocationManagerCompat

    }
}