package com.mockknights.petshelter.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mockknights.petshelter.ui.theme.RedKiwoko
import com.mockknights.petshelter.ui.theme.moderatButtonBold
import com.mockknights.petshelter.ui.theme.moderatLightTitle
import com.mockknights.petshelter.ui.theme.moderatMediumTitle

@Composable
fun CreateWelcomeButton(name: String, modifier: Modifier, colorButton: Color, colorText: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(
                color = Color.Black,
                blurRadius = 4.dp,
                offsetY = 4.dp,
                offsetX = 0.dp,
                spread = 0.dp
            ),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorButton)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.None,
            color = colorText,
            style = MaterialTheme.typography.moderatMediumTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 38.sp,
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 14.dp)
        )
    }
}


@Composable
fun KiwokoIconButton(name: String, icon: Int, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = RedKiwoko),
        modifier = modifier
            .shadow(
                color = Color.Black,
                blurRadius = 4.dp,
                offsetY = 4.dp,
                offsetX = 0.dp,
                spread = 0.dp
            )
            .clip(RoundedCornerShape(4))
    ) {
        Row(
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = name,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.moderatButtonBold,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6.6f)
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Call", // decorative element
                tint = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3.3f)
                    .size(45.dp)
            )
        }
    }
}


fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)