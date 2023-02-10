package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.createButton

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("RestrictedApi")
//@Preview(showSystemUi = true)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

    val petShelter = viewModel.petShelter.collectAsState()

    val sheetState =
        if (viewModel.sheetstate.collectAsState().value == BottomSheetState.COLLAPSED) BottomSheetState(initialValue = BottomSheetValue.Collapsed)
        else BottomSheetState(initialValue = BottomSheetValue.Expanded)

    val bottomSheetScaffoldState = BottomSheetScaffoldState(
        bottomSheetState = sheetState,
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            val modalShelterList = viewModel.modalShelterList.collectAsState().value
            var modalShelterName = ""
            var modalShelterNumber = ""
            if(modalShelterList.isNotEmpty()) {
                modalShelterName = modalShelterList[0].name
                modalShelterNumber = modalShelterList[0].phoneNumber
            }
            ModalBox(modalShelterName, modalShelterNumber)
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(!petShelter.value.isEmpty()) {
                MyGoogleMaps(petShelter.value,
                    onPointClicked = {clickedShelterName ->
                    viewModel.toggleModal()
                        viewModel.setModalShelter(clickedShelterName)
                    true // This way, the default behaviour of google maps is disabled (shows info window with title and snippet)
                }, onMapClicked = {
                    viewModel.collapse()
                })
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom) {
                    createButton(name = "ME", color = Color.Red) {
                        //TODO: Llamar a mi Localizacion
                    }
                }
            }
        }
    }
}


@Composable
fun MyGoogleMaps(petShelter: List<PetShelter>, onPointClicked: (String) -> Boolean, onMapClicked: () -> Unit) {

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
        onMapClick = { onMapClicked() },
        onMapLoaded = { },
        onMyLocationButtonClick = { true },
        onMyLocationClick = { },
        onPOIClick = {  }
    ) {

        for (i in petShelter) {
            Marker(
                MarkerState(LatLng(i.address.latitude, i.address.longitude)),
                title = i.name,
                snippet = i.shelterType,
                onClick = { onPointClicked(i.name) }
            )

        }
    }
}

@Preview
@Composable
fun ModalBox(title: String = "Title", number: String = "NUMBER") {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title box
            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(BorderStroke(1.dp, Color.Gray))
            ) {
                Text(
                    title,
                    Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Normal
                    )
            }
            Row(Modifier
                .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
            ) {
                val image: Painter = painterResource(id = R.drawable.ic_launcher_background)
                Image(
                    painter = image,
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    createButton(name = number, color = Color.Red, onClick = { /*TODO*/ })
                    createButton(name = "GO", color = Color.Red, onClick = { /*TODO*/ })
                }
            }
        }
    }
}

