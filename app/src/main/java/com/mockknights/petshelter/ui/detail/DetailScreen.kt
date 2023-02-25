package com.mockknights.petshelter.ui.detail

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.ShelterType
import com.mockknights.petshelter.ui.components.BoldTitle
import com.mockknights.petshelter.ui.components.KiwokoIconButton
import com.mockknights.petshelter.ui.components.UserAddressField
import com.mockknights.petshelter.ui.components.UserDataFieldTextField
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
                    onEditName = { newName ->
                        detailViewModel.onEditName(newName)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
                ImageRow(
                    photoUrl = shelter.photoURL
                )
                UserAddressField(
                    onUpdateData = { latitude, longitude ->
                        detailViewModel.onUpdatedAddress(latitude, longitude)
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
                UserDataField(
                    fieldLabel = "Teléfono",
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
                    onItemClick = { shelterType -> detailViewModel.onUpdatedShelterType(shelterType) }
                )
                ButtonRow(
                    onClick = {
                        // TODO: Viewmodel action
                    })
            }
        }
    }
}

@Preview
@Composable
fun UserNameRow(
    userName: String = "Long username to check how it looks",
    onEditName: (String) -> Unit = {}
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
            onDone = { onEditName(it) }
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
        maxLines = 1,
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
                    contentDescription = "Edit username"
                )
            }

        }
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
fun UserDataField(
    fieldLabel: String = "Dirección",
    userData: String = "Avenida Europa, 2",
    keyboardType: KeyboardType = KeyboardType.Text,
    doneAction: ImeAction = ImeAction.Done,
    onUpdateValue: (String) -> Unit = { },
    onDone: () -> Unit = { }
    ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.toDp().dp),
    ) {
        UserDataFieldLabel(fieldLabel)
        UserDataFieldTextField(
            userData = userData,
            keyboardType = keyboardType,
            doneAction = doneAction,
            onUpdateValue = { onUpdateValue(it)},
            onDone = onDone
        )
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

@Composable
fun RadioButtonsRow(currentSelection: ShelterType, onItemClick: (ShelterType) -> Unit = {}) {

    val currentlySelectedShelterType = remember { mutableStateOf(currentSelection) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.toDp().dp),
    ) {
        UserDataFieldLabel(fieldLabel = "¿Qué soy?")
        RadioButtonsGroup(
            selected = currentlySelectedShelterType.value,
            onItemClick = { shelterType ->
                onItemClick(shelterType)
                currentlySelectedShelterType.value = shelterType
            }
        )
    }
}

@Composable
fun KiwokoRadioButton(selected: Boolean = false, labelText: String = "Particular", modifier: Modifier, onClick: () -> Unit = {}) {
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
            onClick = { onClick() }
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
fun RadioButtonsGroup(selected: ShelterType = ShelterType.PARTICULAR, onItemClick: (ShelterType) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .weight(1f)

        ShelterType.values().forEach { shelterType ->
            KiwokoRadioButton(
                selected = shelterType == selected,
                labelText = shelterType.toString(),
                modifier = modifier,
                onClick = { onItemClick(shelterType) }
            ) }
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
