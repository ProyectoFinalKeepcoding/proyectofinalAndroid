package com.mockknights.petshelter.ui.map

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.createButton

@SuppressLint("RestrictedApi")
@Preview(showSystemUi = true)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

    val petShelter = viewModel.petShelter.collectAsState()

    if(!petShelter.value.isEmpty()) {
        MyGoogleMaps(petShelter.value)
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom) {
            createButton(name = "ME", color = Color.Red) {
                //TODO: Llamar a mi Localizacion
            }
        }
    }

}


@Composable
fun MyGoogleMaps(petShelter: List<PetShelter>) {

    val madrid = LatLng(petShelter[1].address.latitude, petShelter[1].address.longitude)

    //PORPIEDAS DE LOS MAPAS
    //1- MODIFICADOR
    val modifier by remember {
        mutableStateOf(Modifier.fillMaxSize())
    }
    //2- POSICION DE LA CAMARA
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(madrid, 5f)
    }
   //3- PROPIEDADES DEL MAPA
    val properties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            //isMyLocationEnabled = true,
            //latLngBoundsForCameraTarget = LatLngBounds(marker2, marker3)
        )) }

    //4- UI SETTINGS
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true, tiltGesturesEnabled = true))}

    GoogleMap(modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = { },
        onMapLoaded = { },
        onMyLocationButtonClick = { true },
        onMyLocationClick = { },
        onPOIClick = { }
    ) {

        for (i in petShelter) {
            Marker(
                MarkerState(LatLng(i.address.latitude, i.address.longitude)),
                title = i.name,
                snippet = i.shelterType
            )
        }
    }
}

