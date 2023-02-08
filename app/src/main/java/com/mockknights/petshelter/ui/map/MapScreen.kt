package com.mockknights.petshelter.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.createButton

@Preview(showSystemUi = true)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

    //PEDIR PERMISOS
    //ActivityCompat.checkSelfPermission(requireContext(), )

    val petShelter = viewModel.petShelter.collectAsState()
    LaunchedEffect(key1 = petShelter.value) {

    }

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

    val teide = LatLng(petShelter[0].address.latitude, petShelter[0].address.longitude)
    val madrid = LatLng(petShelter[1].address.latitude, petShelter[1].address.longitude)
    val igualada = LatLng(petShelter[2].address.latitude, petShelter[2].address.longitude)
    val otra = LatLng(petShelter[3].address.latitude, petShelter[3].address.longitude)

    //PORPIEDAS DE LOS MAPAS
    //1- MODIFICADOR
    val modifier by remember {
        mutableStateOf(Modifier.fillMaxSize())
    }
    //2- POSICION DE LA CAMARA
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(madrid, 10f)
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

        Marker(
            MarkerState(teide),
            title = petShelter[0].name,
            snippet = petShelter[0].phoneNumber,
            rotation = Float.MAX_VALUE

        )
        Marker(
            MarkerState(igualada),
            title = petShelter[1].name,
            snippet = petShelter[1].phoneNumber,
            rotation = Float.MIN_VALUE
        )
        Marker(
            MarkerState(madrid),
            title = petShelter[3].name,
            snippet = petShelter[3].phoneNumber,
            flat = true
        )
        Marker(
            MarkerState(otra),
            title = petShelter[4].name,
            snippet = petShelter[4].phoneNumber,
        )
    }
}

