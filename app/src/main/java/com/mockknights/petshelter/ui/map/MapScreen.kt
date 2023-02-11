package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.createButton
import com.mockknights.petshelter.ui.theme.moderatMediumTitle


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
            var modalShelterPhotoUrl = ""
            if(modalShelterList.isNotEmpty()) {
                modalShelterName = modalShelterList[0].name
                modalShelterNumber = modalShelterList[0].phoneNumber
                modalShelterPhotoUrl = modalShelterList[0].photoURL
            }
            ModalBox(modalShelterName, modalShelterNumber, modalShelterPhotoUrl)
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
fun ModalBox(title: String = "Title", phoneNumber: String = "NUMBER", photoUrl: String = "") {
    val configuration = LocalConfiguration.current
    val modalHeight = configuration.screenHeightDp.dp / 3
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(modalHeight),
        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title box: modifier passed as parameter to use columnScope
            TitleBox(
                title = title,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.6f))
            // Row with the remaining elements
            ImageAndButtonsRow(
                photoUrl = photoUrl,
                phoneNumber = phoneNumber,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(7.4f))
        }
    }
}

@Composable
fun TitleBox(title: String, modifier: Modifier) {
        Column(
            modifier = modifier
                .background(Color.LightGray)
                .padding(top = 2.dp, bottom = 2.dp)
                .drawBehind {
                    drawTopLine(this, 5f)
                    drawBottomLine(this, 5f)
                }
        ) {
            Text(
                title,
                Modifier
                    .fillMaxSize() // First expand it to max size
                    .wrapContentHeight(), // Then, wrap content and align vertically the text
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                style = MaterialTheme.typography.moderatMediumTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
}

@Composable
fun ImageAndButtonsRow(photoUrl: String, phoneNumber: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
    ) {
        ImageColumn(
            photoUrl = photoUrl,
            modifier = Modifier
                .fillMaxSize()
                .weight(5.3f)
                .padding(10.dp)
        )
        ButtonsColumn(
            phoneNumber = phoneNumber,
            modifier = Modifier
                .fillMaxSize()
                .weight(4.7f)
                .padding(10.dp))
    }
}

@Composable
fun ImageColumn(photoUrl: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if (photoUrl.isNotEmpty()) "http://10.0.2.2:8080/$photoUrl" else R.drawable.ic_launcher_background,
            contentDescription = "image",
            placeholder = painterResource(R.drawable.ic_launcher_background),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}

@Composable
fun ButtonsColumn(phoneNumber: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        createButton(name = phoneNumber, color = Color.Red, onClick = { /*TODO*/ })
        createButton(name = "GO", color = Color.Red, onClick = { /*TODO*/ })
    }
}

// Extension functions
fun drawTopLine(drawScope: DrawScope, width: Float) {
    drawScope.drawLine(
        Color.Black,
        Offset(0f, 0f),
        Offset(drawScope.size.width, 0f),
        width
    )
}

fun drawBottomLine(drawScope: DrawScope, width: Float) {
    val y = drawScope.size.height
    drawScope.drawLine(
        Color.Black,
        Offset(0f, y),
        Offset(drawScope.size.width, y),
        width
    )
}

