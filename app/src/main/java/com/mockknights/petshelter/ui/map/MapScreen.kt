package com.mockknights.petshelter.ui.map

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.mockknights.petshelter.data.local.model.PetShelterLocal
import com.mockknights.petshelter.ui.components.createButton
import com.mockknights.petshelter.ui.login.LoginViewModel

@Preview(showSystemUi = true)
@Composable
fun MapScreen() {
    MyGoogleMaps()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom) {
        createButton(name = "ME", color = Color.Red) {
            //TODO: Llamar a mi Localizacion
        }
    }
}


@Composable
fun MyGoogleMaps() {
    val marker1 = LatLng(28.270833, -16.63916)
    val marker2 = LatLng(40.416729, -3.703339)
    val marker3 = LatLng(41.5790182, 1.617346)
    val marker4 = LatLng(39.85738435, -4.02404485700751)

    val properties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = false,
            latLngBoundsForCameraTarget = LatLngBounds(marker2, marker3)
        )) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true, tiltGesturesEnabled = true))}

    GoogleMap(modifier = Modifier.fillMaxSize(),
        properties = properties,
        uiSettings = uiSettings
    ) {
        Marker(
            MarkerState(marker1),
            title = "AMIGO1",
            snippet = "Iep que pasa contigo?",
            rotation = Float.MAX_VALUE

        )
        Marker(
            MarkerState(marker2),
            title = "AMIGO2",
            snippet = "Me lo estas preguntando a mi hermano?",
            rotation = Float.MIN_VALUE
        )
        Marker(
            MarkerState(marker3),
            title = "AMIGO3",
            snippet = "Que coño dices tu ahora?",
            flat = true
        )
        Marker(
            MarkerState(marker4),
            title = "AMIGO4",
            snippet = "No se, tu sabras",
        )
    }
}

