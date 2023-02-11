package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mockknights.petshelter.ui.theme.RedKiwoko
import com.mockknights.petshelter.ui.theme.moderatButtonBold

@Composable
fun createButton(name: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.None,
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun KiwokoIconButton(name: String, icon: Int, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = RedKiwoko),
        modifier = Modifier
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