package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mockknights.petshelter.ui.theme.GrayKiwoko
import com.mockknights.petshelter.ui.theme.RedKiwoko
import com.mockknights.petshelter.ui.theme.moderatTextField
import com.mockknights.petshelter.ui.theme.moderatUsername

@Preview
@Composable
fun BoldTitle(modifier: Modifier = Modifier, title: String = "title", color: Color = Color.Black) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.moderatUsername,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color
    )
}

@Preview
@Composable
fun TextPlaceholder(text: String = "Placeholder") {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        style = MaterialTheme.typography.moderatTextField,
        color = GrayKiwoko,
        textAlign = TextAlign.Left
    )
}