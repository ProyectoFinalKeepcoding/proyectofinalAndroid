package com.mockknights.petshelter.ui.components.detailImage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mockknights.petshelter.R

@Preview
@Composable
fun DetailImage(
    modifier: Modifier = Modifier,
    shelterId: String = "5B4A63DF-F5B8-4306-B264-2C56CF05C613",
    detailImageViewModel: DetailImageViewModel = hiltViewModel(),
    photoUrl: String = "",
    onImageSelected: () -> Unit = {},
) {
    val localContext = LocalContext.current
    val originalImageUrl by remember { mutableStateOf("http://10.0.2.2:8080/$photoUrl") }
    var selectedImage by remember { mutableStateOf(Uri.parse("http://10.0.2.2:8080/$photoUrl")) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImage = uri
        onImageSelected()
        if(uri != null) detailImageViewModel.onSelectedImage(uri, shelterId, localContext)
        // If clicked outside the gallery, a person placeholder image will be uploaded and shown
        else detailImageViewModel.onSelectedImage(Uri.parse("android.resource://com.mockknights.petshelter/${R.drawable.person_image}"), shelterId, localContext)
    }

    AsyncImage(
        model = if(photoUrl.isEmpty()) R.drawable.person_image
                else if(selectedImage != null) selectedImage.toString()
                else R.drawable.person_image,
        contentDescription = "image",
        placeholder = painterResource(R.drawable.person_image),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickable {
                galleryLauncher.launch("image/*")
            }
    )
}


