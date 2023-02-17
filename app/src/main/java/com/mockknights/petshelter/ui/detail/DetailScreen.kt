package com.mockknights.petshelter.ui.detail

import android.content.res.Resources
import android.service.autofill.UserData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mockknights.petshelter.R
import com.mockknights.petshelter.ui.theme.GrayKiwoko
import com.mockknights.petshelter.ui.theme.moderatDataFieldLabel
import com.mockknights.petshelter.ui.theme.moderatTextField
import com.mockknights.petshelter.ui.theme.moderatUsername

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
            ) {

        UserNameRow()
        ImageRow()

    }
}


@Preview
@Composable
fun UserNameRow(userName: String = "UserName") {

    Row() {
        UserName(userName)
        Icon(
            painter = painterResource(id = R.drawable.pencil),
            contentDescription = "Edit username")
    }
}
@Preview
@Composable
fun UserName(userName: String = "UserName") {
    Text(
        text = userName,
        style = MaterialTheme.typography.moderatUsername
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
    Column() {
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
