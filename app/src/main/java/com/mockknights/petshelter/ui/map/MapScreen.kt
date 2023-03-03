package com.mockknights.petshelter.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.google.accompanist.permissions.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.ui.components.KiwokoIconButton
import com.mockknights.petshelter.ui.components.LogoBox
import com.mockknights.petshelter.ui.detail.toDp
import com.mockknights.petshelter.ui.theme.moderatMediumTitle
import kotlinx.coroutines.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("RestrictedApi")
@Preview(showSystemUi = true)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

    // Coroutine scope to launch coroutines from composable functions
    val coroutineScope = rememberCoroutineScope()
    // Local context to get resources
    val mapScreenContext = LocalContext.current
    // PetShelter list
    val petShelters = ((viewModel.mapShelterListState.collectAsState().value) as? MapShelterListState.Success)?.petShelters
    // Get camera position
    val cameraPositionState = viewModel.cameraPositionState.collectAsState().value
    // This launcher is used to request permissions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionRequestCompleted(isGranted, mapScreenContext)
    }
    // Before composing the view, check and request (or not) location permissions
    OnMapLaunched { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
    // This composable contains the map and manages the modal of selected shelter
    BottomSheetScaffold(
        scaffoldState = viewModel.bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            val modalShelterList = viewModel.modalShelterList.collectAsState().value
            // When clicking on a shelter, the viewModel gets the shelter and sets it in the modalShelterList
            // This modal shelter is then shown in the modal
            if(modalShelterList.isNotEmpty()) {
                val shelter = modalShelterList[0]
                ModalBox(
                    title = shelter.name,
                    phoneNumber = shelter.phoneNumber,
                    photoUrl = shelter.photoURL,
                    onCall = {
                        viewModel.onCall(shelter.phoneNumber, mapScreenContext)
                    },
                    onGo = {
                        viewModel.onGo(shelter.address, mapScreenContext)
                    })
            } else {
                ModalBox(null, null, null)
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column() {
                // The logo box is always shown, whether or not the list of shelters is empty
                LogoBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.3f))

                if (petShelters != null) {

                    MyGoogleMaps(petShelters,
                        locationGranted = viewModel.locationPermissionGranted.value,
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(7.7f),
                        onPlacingPoint = {
                            viewModel.getShelterIconByShelterType(it) // Defines the icon depending on shelterType
                        },
                        onPointClicked = {clickedShelterName ->
                            viewModel.toggleModal(coroutineScope)
                            viewModel.setModalShelter(clickedShelterName)
                            true // This way, the default behaviour of google maps is disabled (shows info window with title and snippet)
                        }, onMapClicked = {
                            viewModel.collapseModal(coroutineScope)
                        }, onMapLoaded = {
                            if(viewModel.locationPermissionGranted.value) viewModel.moveCameraToUserLocation(coroutineScope)
                        })


                }
            }
            KiwokoIconButton(
                name = "Refugio más cercano",
                icon = 0,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp / 7.19).dp)
                    .padding(16.dp),
                onClick = {
                    viewModel.onClosestShelterClicked(coroutineScope)
                }
            )
        }
    }
}

@Composable
fun MyGoogleMaps(petShelter: List<PetShelter>,
                 locationGranted: Boolean,
                 cameraPositionState: CameraPositionState,
                 modifier: Modifier,
                 onPlacingPoint: (String) -> Int,
                 onPointClicked: (String) -> Boolean,
                 onMapClicked: () -> Unit,
                 onMapLoaded: () -> Unit) {

    // Map properties
    val properties = MapProperties( // Important: not remembered, as it has to be recomposed entirely for purposes of repainting user location
        mapType = MapType.NORMAL,
        isMyLocationEnabled = locationGranted,
    )

    // Settings of the map
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                tiltGesturesEnabled = true,
                myLocationButtonEnabled = false,
            ))}

    GoogleMap(modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = { onMapClicked() },
        onMapLoaded = { onMapLoaded() },
        onMyLocationButtonClick = { true },
        onMyLocationClick = { },
        onPOIClick = {  }
    ) {
        for (i in petShelter) {
            Marker(
                MarkerState(LatLng(i.address.latitude, i.address.longitude)),
                icon = bitmapDescriptorFromVector(LocalContext.current, onPlacingPoint(i.shelterType.stringValue)),
                title = i.name,
                snippet = i.shelterType.toString(),
                onClick = { onPointClicked(i.name) }
            )
        }
    }
}

// ModalScreen shown on the map. It has 3 thirds of the vertical height. Image scales to allow at
// least 8 dp of padding.
@Preview
@Composable
fun ModalBox(
    title: String? = "Title",
    phoneNumber: String? = "918158899",
    photoUrl: String? = "",
    onCall: () -> Unit = {},
    onGo: () -> Unit = {}
) {
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
                title = title ?: "",
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.6f))
            // Row with the remaining elements
            ImageAndButtonsRow(
                photoUrl = photoUrl ?:"",
                modifier = Modifier
                    .fillMaxSize()
                    .weight(7.4f),
                onCall = onCall,
                onGo = onGo,
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
fun ImageAndButtonsRow(
    photoUrl: String,
    modifier: Modifier,
    onCall: () -> Unit = {},
    onGo: () -> Unit = {})
{
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
                .padding(horizontal = 8.dp, vertical = 8.dp),
            onCall = onCall,
            onGo = onGo
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
fun ButtonsColumn(
    modifier: Modifier,
    onCall: () -> Unit = {},
    onGo: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KiwokoIconButton(
            name = "Llamar",
            icon = R.drawable.phone,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            onCall()
        }
        Spacer(modifier = Modifier
            .fillMaxSize()
            .weight(0.5f))

        KiwokoIconButton(
            name = "Ir",
            icon = R.drawable.directions,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            onGo()
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

@Composable
fun OnMapLaunched(execute: () -> Unit) {
    // Before composing the view, request check and request location permissions
    val lifecycleOwner = LocalLifecycleOwner.current // Compose views don't have onStart, onCreate... but a LifecycleOwner
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val eventObserver = LifecycleEventObserver { _, event ->
            // When starting to compose the view, ask for location permission if not allowed
            if(event == Lifecycle.Event.ON_START) execute()
        }
        // Add the event observer to check the location permission on starting the composition
        lifecycleOwner.lifecycle.addObserver(eventObserver)
        // When leaving the map screen, remove the observer
        onDispose { lifecycleOwner.lifecycle.removeObserver(eventObserver) }
    })
}