package com.mockknights.petshelter.ui.detail

import android.content.res.Resources
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mockknights.petshelter.R
import com.mockknights.petshelter.ui.components.ButtonRow
import com.mockknights.petshelter.ui.components.RadioButtonsRow
import com.mockknights.petshelter.ui.components.UserAddressField
import com.mockknights.petshelter.ui.components.UserDataField
import com.mockknights.petshelter.ui.components.detailImage.DetailImage
import com.mockknights.petshelter.ui.theme.*

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density.toInt())
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density.toInt())


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(id: String, detailViewModel: DetailViewModel = hiltViewModel()) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val detailState by detailViewModel.detailState.collectAsState()

    LaunchedEffect(key1 = id) {
        detailViewModel.getShelterDetail(id)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.toDp().dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
            ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.toDp().dp)
        ) {
            if (detailState is DetailState.Success) { // If the data is not loaded yet, don't show anything
                val shelter = (detailState as DetailState.Success).petShelter
                UserNameRow(
                    userName = shelter.name,
                    onNameEdited = { newName ->
                        detailViewModel.onEditName(newName)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
                ImageRow(
                    photoUrl = shelter.photoURL,
                    shelterId = shelter.id,
                    onImageClicked = {
                        detailViewModel.onImageClicked()
                    }
                )
                UserAddressField(
                    currentAddress = shelter.address,
                    onUpdateData = { latitude, longitude ->
                        detailViewModel.onUpdatedAddress(latitude, longitude)
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
                UserDataField(
                    fieldLabel = "Tel??fono",
                    userData = shelter.phoneNumber,
                    keyboardType = KeyboardType.Phone,
                    onUpdateValue = { phone ->
                        detailViewModel.onUpdatedPhone(phone)
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
                RadioButtonsRow(
                    currentSelection = shelter.shelterType,
                    onItemClick = { shelterType ->
                        detailViewModel.onUpdatedShelterType(shelterType)
                    }
                )
                ButtonRow(
                    onClick = {
                        detailViewModel.onSaveClicked()
                    })
            }
        }
    }
}

@Preview
@Composable
fun UserNameRow(
    userName: String = "Long username to check how it looks",
    onNameEdited: (String) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.6f)
        )
        UserNameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8.4f),
            userName = userName,
            onDone = { onNameEdited(it) }
        )
    }
}

@Preview
@Composable
fun UserNameTextField(modifier: Modifier = Modifier, userName: String = "username", onDone: (String) -> Unit = {}) {

    val enabled = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf(
        TextFieldValue(
            text = userName,
            selection = TextRange(0, 0)
        )
    )
    }
    val focusRequester =  remember { FocusRequester() }

    LaunchedEffect(key1 = enabled.value) {
          if(enabled.value) {
              textFieldValue.value = TextFieldValue(text = userName, selection = TextRange(0, userName.length))
              focusRequester.requestFocus()
          }
    }

    TextField(
        enabled = enabled.value,
        textStyle = MaterialTheme.typography.moderatUsername,
        singleLine = true,
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            disabledTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = RedKiwoko,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                    enabled.value = !enabled.value
                    onDone(textFieldValue.value.text)
            }
        ),
        modifier = modifier
            .focusRequester(focusRequester),
        trailingIcon = {
            IconButton (
                onClick = {
                        enabled.value = !enabled.value
                        onDone(textFieldValue.value.text)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = "Edit username",
                    tint = RedKiwoko
                )
            }

        }
    )
}


@Preview
@Composable
fun ImageRow(
    photoUrl: String = "0F484421-1D54-4A2D-806D-8BAAD2CA1158.png",
    shelterId: String = "0F484421-1D54-4A2D-806D-8BAAD2CA1158",
    onImageClicked: () -> Unit = {}
) {

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
        DetailImage(
            photoUrl = photoUrl,
            shelterId = shelterId,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(25.dp))
                .fillMaxSize()
                .weight(6.8f),
            onImageSelected = { onImageClicked() }
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
fun UserDataFieldLabel(fieldLabel: String = "Direcci??n") {
    Text(
        text = fieldLabel,
        style = MaterialTheme.typography.moderatDataFieldLabel
    )
}




