package com.mockknights.petshelter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mockknights.petshelter.R




// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

/**
 * Custom font family for the app.
 */
val ModeratFamily = FontFamily(
    Font(R.font.moderat_bold, FontWeight.Bold),
    Font(R.font.moderat_medium, FontWeight.Medium),
    Font(R.font.moderat_light, FontWeight.Light)
)

/**
 * Style for the title of the modal and the welcome button.
 */
val Typography.moderatMediumTitle: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            letterSpacing = 0.14.em
        )
    }

/**
 * Style for the text in the kiwoko button.
 */
val Typography.moderatButtonBold: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
    }

/**
 * Style for the text in the labels shown above the text fields in the detail and register screens.
 */
val Typography.moderatDataFieldLabel: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            letterSpacing = 0.03.em
        )
    }

/**
 * Style for the text of the labels shown under the radio buttons in the register and detail screens.
 */
val Typography.moderatRadioButtonLabel: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            letterSpacing = 0.03.em
        )
    }

/**
 * Style used for the name of the shelter in the detail screen.
 */
val Typography.moderatUsername: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            letterSpacing = 0.03.em
        )
    }

/**
 * Style used for the text fields.
 */
val Typography.moderatTextField: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            letterSpacing = 0.03.em
        )
    }
