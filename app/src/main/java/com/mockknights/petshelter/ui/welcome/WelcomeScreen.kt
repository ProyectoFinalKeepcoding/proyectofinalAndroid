package com.mockknights.petshelter.ui.welcome

import android.widget.Toolbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.ui.components.createButton

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreen (viewModel: WelcomeViewModel = hiltViewModel(), navigateToMap: () -> (Unit) = {} , navigateToLogin: () -> (Unit) = {}) {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            createButton(name = "USER", color = Color.Black) {
                navigateToMap()
            }

            createButton(name = "PETSHELTER", color = Color.Green ) {
                navigateToLogin()
            }
        }
    }
}