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

// Create new family for font moderat
val ModeratFamily = FontFamily(
    Font(R.font.moderat_bold, FontWeight.Bold),
    Font(R.font.moderat_medium, FontWeight.Medium),
    Font(R.font.moderat_light, FontWeight.Light)
)

// Create extension styles that can be used as Material styles
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

// Create extension styles that can be used as Material styles
val Typography.moderatButtonBold: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
    }

val Typography.moderatLightTitle: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Light,
            fontSize = 24.sp,
            letterSpacing = 0.14.em
        )
    }

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

val Typography.moderatRadioButtonLabel: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = ModeratFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            letterSpacing = 0.03.em
        )
    }

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
