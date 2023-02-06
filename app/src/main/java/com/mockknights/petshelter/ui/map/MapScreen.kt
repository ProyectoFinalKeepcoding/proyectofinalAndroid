package com.mockknights.petshelter.ui.map

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PatternItem
import com.google.maps.android.compose.*
import com.mockknights.petshelter.ui.components.createButton

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
@GoogleMapComposable
public fun Circle() {
    Circle(
        center = LatLng(40.416729, -3.703339),
        clickable = false,
        fillColor = Color.Blue,
        //radius = 5000000000000000.0,
        strokeColor = Color.Green,
        //strokePattern = List<PatternItem>? = null,
        strokeWidth = 10f,
        //tag = Any? = null,
        visible = true,
        zIndex = 0f,
        onClick = { }
    )
}


@Composable
fun MyGoogleMaps() {
    val teide = LatLng(28.270833, -16.63916)
    val madrid = LatLng(40.416729, -3.703339)
    val igualada = LatLng(41.5790182, 1.617346)
    val otra = LatLng(39.85738435, -4.02404485700751)

    //PORPIEDAS DE LOS MAPAS
    //1- MODIFICADOR
    val modifier by remember {
        mutableStateOf(Modifier.fillMaxSize())
    }
    //2- POSICION DE LA CAMARA
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(igualada, 10f)
    }
   //3- PROPIEDADES DEL MAPA
    val properties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = true,
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
            title = "AMIGO1",
            snippet = "Iep que pasa contigo?",
            rotation = Float.MAX_VALUE

        )
        Marker(
            MarkerState(madrid),
            title = "AMIGO2",
            snippet = "Me lo estas preguntando a mi hermano?",
            rotation = Float.MIN_VALUE
        )
        Marker(
            MarkerState(igualada),
            title = "AMIGO3",
            snippet = "Que coño dices tu ahora?",
            flat = true
        )
        Marker(
            MarkerState(otra),
            title = "AMIGO4",
            snippet = "No se, tu sabras",
        )
    }
}

