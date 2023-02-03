package com.mockknights.petshelter.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.GoogleMap
import com.mockknights.petshelter.ui.login.LoginViewModel

@Preview(showSystemUi = true)
@Composable
fun MapScreen () {
    GoogleMap(modifier = Modifier.fillMaxSize())
}