package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mockknights.petshelter.ui.theme.*

/**
 * This is the title that indicates main information in the app.
 * @param title The text that will be shown in the title.
 * @param color The color of the text. By default is black.
 */
@Preview
@Composable
fun BoldTitle(
    modifier: Modifier = Modifier,
    title: String = "title",
    color: Color = Color.Black
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.moderatUsername,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color
    )
}

/**
 * This is the placeholder shown in the text fields when needed.
 * @param text The text that will be shown in the placeholder.
 */
@Preview
@Composable
fun TextPlaceholder(
    text: String = "Placeholder"
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(),
        style = MaterialTheme.typography.moderatTextField,
        color = GrayKiwoko,
        textAlign = TextAlign.Left
    )
}

/**
 * This is the label shown above the text field in the detail and sign up screens.
 * @param fieldLabel The text that will be shown in the label.
 */
@Preview
@Composable
fun UserDataFieldLabel(
    fieldLabel: String = "Dirección"
) {
    Text(
        text = fieldLabel,
        style = MaterialTheme.typography.moderatDataFieldLabel
    )
}