package com.mockknights.petshelter.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.KiwokoIconButton
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
            Column() {
                LogoBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.3f))
                if(petShelter.value.isNotEmpty()) {
                    MyGoogleMaps(petShelter.value,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(7.7f),
                        onPlacingPoint = {
                            viewModel.getShelterIconByShelterType(it) // Defines the icon depending on shelterType
                        },
                        onPointClicked = {clickedShelterName ->
                            viewModel.toggleModal()
                            viewModel.setModalShelter(clickedShelterName)
                            true // This way, the default behaviour of google maps is disabled (shows info window with title and snippet)
                        }, onMapClicked = {
                            viewModel.collapse()
                        })
                }
            }
        }
    }
}
@Composable
fun LogoBox(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {
        val image: Painter = painterResource(id = R.drawable.logo)
        Image(painter = image, contentDescription = "Logo")
    }
}

@Composable
fun MyGoogleMaps(petShelter: List<PetShelter>, modifier: Modifier, onPlacingPoint: (String) -> Int, onPointClicked: (String) -> Boolean, onMapClicked: () -> Unit) {

    val madrid = LatLng(petShelter[1].address.latitude, petShelter[1].address.longitude)

    //PORPIEDAS DE LOS MAPAS
    //1- MODIFICADOR
    val modifier by remember {
        mutableStateOf(modifier)
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
                icon = bitmapDescriptorFromVector(LocalContext.current, onPlacingPoint(i.shelterType)),
                title = i.name,
                snippet = i.shelterType,
                onClick = { onPointClicked(i.name) }
            )

        }
    }
}

// ModalScreen shown on the map. It has 3 thirds of the vertical height. Image scales to allow at
// least 8 dp of padding.
@Preview
@Composable
fun ModalBox(title: String = "Title", phoneNumber: String = "918158899", photoUrl: String = "") {
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
                modifier = Modifier
                    .fillMaxSize()
                    .weight(7.4f)
            )
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
fun ImageAndButtonsRow(photoUrl: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
    ) {
        ImageColumn(
            photoUrl = photoUrl,
            modifier = Modifier
                .fillMaxSize()
                .weight(5.3f)
                .padding(horizontal = 8.dp, vertical = 8.dp) // Depending on the device, the image will adjust to 8dp
        )
        ButtonsColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(4.7f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
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
                .clip(RoundedCornerShape(25.dp))
        )
    }
}

@Composable
fun ButtonsColumn(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KiwokoIconButton(
            name = "Llamar",
            icon = R.drawable.phone,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // TODO: Call!!
        }
        KiwokoIconButton(
            name = "Ir",
            icon = R.drawable.directions,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // TODO: Go!!
        }
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

// Get bitmapDescriptor from svg icon
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}