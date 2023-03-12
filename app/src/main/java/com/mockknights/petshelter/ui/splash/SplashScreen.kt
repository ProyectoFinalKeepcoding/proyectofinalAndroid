package com.mockknights.petshelter.ui.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mockknights.petshelter.R
import kotlinx.coroutines.delay

@Preview(showSystemUi = true)
@Composable
fun SplashScreen(navigateToWelcome: () -> (Unit) = {}) {

    LaunchedEffect(key1 = true) {
        delay(3000)
        navigateToWelcome()
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logomarker),
            contentDescription = "Logo",
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
        )
    }

}

