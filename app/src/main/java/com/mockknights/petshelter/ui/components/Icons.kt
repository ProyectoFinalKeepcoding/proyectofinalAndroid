package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mockknights.petshelter.R

/**
 * This is the logo that is shown in the welcome screen.
 */
@Composable
fun LogoBox(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val logoMarker: Painter = painterResource(id = R.drawable.logomarker)
        val logoTypescript: Painter = painterResource(id = R.drawable.logo)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painter = logoMarker, contentDescription = "Logo marker")
            Spacer(modifier = Modifier.size(1.dp))
            Image(painter = logoTypescript, contentDescription = "Logo typescript")
        }
    }
}