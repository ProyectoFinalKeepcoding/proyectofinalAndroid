package com.mockknights.petshelter.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mockknights.petshelter.ui.components.CreateWelcomeButton
import com.mockknights.petshelter.ui.theme.RedKiwoko

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreen(/*viewModel: WelcomeViewModel = hiltViewModel(),*/ navigateToMap: () -> (Unit) = {},
                                                                     navigateToLogin: () -> (Unit) = {}
) {

    val buttonModifier by remember {
        mutableStateOf(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )
    }
    val spacerModifier by remember { mutableStateOf(
        Modifier
            .fillMaxWidth()
            .height(100.dp)) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .background(RedKiwoko), Alignment.Center
        )

        {
            CreateWelcomeButton(
                name = "Puedo acoger a una mascota",
                modifier = buttonModifier,
                colorButton = Color.White,
                colorText = RedKiwoko
            )
            {
                navigateToMap()
            }
        }
        Spacer(modifier = spacerModifier.background(RedKiwoko))
        Spacer(modifier = spacerModifier.background(Color.White))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .background(Color.White), Alignment.Center
        ) {
            CreateWelcomeButton(
                name = "He encontrado una mascota",
                modifier = buttonModifier,
                colorButton = RedKiwoko,
                colorText = Color.White
            ) {
                navigateToLogin()
            }
        }
    }
}