package com.mockknights.petshelter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
            letterSpacing = 9.sp
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
            letterSpacing = 15.sp
        )
    }