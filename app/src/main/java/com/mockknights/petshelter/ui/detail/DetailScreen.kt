package com.mockknights.petshelter.ui.detail

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mockknights.petshelter.R
import com.mockknights.petshelter.ui.components.KiwokoIconButton
import com.mockknights.petshelter.ui.theme.*

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density.toInt())
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density.toInt())

@Preview
@Composable
fun DetailScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.toDp().dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
            ){
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.toDp().dp)
        ) {

            UserNameRow()
            ImageRow()
            UserDataField("Dirección", "Avenida Europa 12") // TODO: Change to real data
            UserDataField("Teléfono", "626892362") // TODO: Change to real data
            RadioButtonsRow()
            ButtonRow(
                onClick = {
                    // TODO: Viewmodel action
                })
        }
    }
}


@Preview
@Composable
fun UserNameRow(userName: String = "Long username to check how it looks") {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .weight(1.6f)
        Spacer(
            modifier = modifier
        )
        UserName(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6.8f),
            userName = userName
        )
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.pencil),
            contentDescription = "Edit username"
        )
    }
}
@Preview
@Composable
fun UserName(modifier: Modifier = Modifier, userName: String = "UserName") {
    Text(
        modifier = modifier,
        text = userName,
        style = MaterialTheme.typography.moderatUsername,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
fun ImageRow(photoUrl: String = "0F484421-1D54-4A2D-806D-8BAAD2CA1158.png") {
    Row(
modifier = Modifier
    .fillMaxWidth()
    .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.6f)
        )
        AsyncImage(
            model = if (photoUrl.isNotEmpty()) "http://10.0.2.2:8080/$photoUrl" else R.drawable.ic_launcher_background,
            contentDescription = "image",
            placeholder = painterResource(R.drawable.ic_launcher_background),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(25.dp))
                .fillMaxSize()
                .weight(6.8f)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.6f)
        )
    }
}

@Preview
@Composable
fun UserDataField(fieldLabel: String = "Dirección", userAddress: String = "Avenida Europa, 2") {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.toDp().dp),
    ) {
        UserDataFieldLabel(fieldLabel)
        UserDataFieldTextField(userAddress)
    }
}

@Preview
@Composable
fun UserDataFieldLabel(fieldLabel: String = "Dirección") {
    Text(
        text = fieldLabel,
        style = MaterialTheme.typography.moderatDataFieldLabel
    )
}

@Preview
@Composable
fun UserDataFieldTextField(userAddress: String = "Avenida Europa, 2") {
    OutlinedTextField(
        value = userAddress,
        textStyle = MaterialTheme.typography.moderatTextField,
        singleLine = true,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = GrayKiwoko,
            unfocusedBorderColor = GrayKiwoko,
        ),
        onValueChange = {} )
}

@Preview
@Composable
fun RadioButtonsRow() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.toDp().dp),
    ) {
        UserDataFieldLabel(fieldLabel = "¿Qué soy?")
        RadioButtonsGroup()
    }
}

@Composable
fun KiwokoRadioButton(selected: Boolean = false, labelText: String = "Particular", modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.toDp().dp),
        modifier = modifier
            .wrapContentSize()
    ) {
        RadioButton(
            modifier = Modifier
                .size(40.toDp().dp),
            selected = selected,
            colors = RadioButtonDefaults.colors(
                selectedColor = RedKiwoko,
                unselectedColor = GrayKiwoko,
            ),
            onClick = {}
        )
        Text(
            text = labelText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.moderatRadioButtonLabel,
        )
    }
}

@Preview
@Composable
fun RadioButtonsGroup() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        KiwokoRadioButton(
            labelText = "Particular",
            modifier = modifier
        )
        KiwokoRadioButton(
            selected = true,
            labelText = "Ayuntamiento",
            modifier = modifier
        )
        KiwokoRadioButton(
            labelText = "Veterinario",
            modifier = modifier
        )
        KiwokoRadioButton(
            labelText = "Centro de acogida",
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun ButtonRow(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
        )
        KiwokoIconButton(
            name = "Guardar cambios",
            icon = 0,
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f),
            onClick = onClick
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
        )
    }
}
