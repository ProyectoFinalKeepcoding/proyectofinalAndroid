package com.mockknights.petshelter.ui.components.detailImage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mockknights.petshelter.R

@Preview
@Composable
fun DetailImage(
    modifier: Modifier = Modifier,
    photoUrl: String = "",
    onImageSelected: () -> Unit = {},
//    detailImageViewModel: DetailImageViewModel = hiltViewModel()
) {
    val originalImageUrl by remember { mutableStateOf("http://10.0.2.2:8080/$photoUrl") }
    var selectedImage by remember { mutableStateOf(Uri.parse("http://10.0.2.2:8080/$photoUrl")) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImage = uri
        onImageSelected()
    }

    AsyncImage(
        model = if(photoUrl.isEmpty()) R.drawable.person
                else if(selectedImage != null) selectedImage.toString()
                else originalImageUrl,
        contentDescription = "image",
        placeholder = painterResource(R.drawable.person),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickable {
                galleryLauncher.launch("image/*")
            }
    )
}


