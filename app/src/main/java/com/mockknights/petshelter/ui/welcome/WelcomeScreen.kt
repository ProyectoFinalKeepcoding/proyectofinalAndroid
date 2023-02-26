package com.mockknights.petshelter.ui.welcome

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.mockknights.petshelter.ui.components.CreateWelcomeButton
import com.mockknights.petshelter.ui.theme.RedKiwoko
import com.mockknights.petshelter.R


fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density.toInt())
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density.toInt())

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreen( navigateToMap: () -> (Unit) = {}, navigateToLogin: () -> (Unit) = {}) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RedKiwoko), Alignment.Center
        ) {
            BackgroundImage()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.toDp().dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2.8f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    CreateWelcomeButton(
                        name = "Puedo acoger a una mascota",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 16.toDp().dp),
                        colorButton = Color.White,
                        colorText = RedKiwoko
                    ) {
                        navigateToLogin()
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2.4f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4.8f),
                    verticalArrangement = Arrangement.Top
                ) {
                    // Lottie file dog walking (not showing)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        WalkingDog(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    CreateWelcomeButton(
                        name = "He encontrado una mascota",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                            .wrapContentHeight()
                            .padding(horizontal = 16.toDp().dp),
                        colorButton = RedKiwoko,
                        colorText = Color.White
                    ) {
                        navigateToMap()
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(4f)
                    )
                }

            }
        }

}


@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.welcome),
        contentDescription = "Fondo pantalla",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize())
}

@Preview // No preview for lottie files yet
@Composable
fun WalkingDog(modifier: Modifier = Modifier) {
    val compositeResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.dog)
    )
    val progress by animateLottieCompositionAsState(
        compositeResult.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = compositeResult.value,
        progress = { progress },
        alignment = Alignment.TopStart,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
    )
}