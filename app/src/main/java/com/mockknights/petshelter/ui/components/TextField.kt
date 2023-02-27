package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mockknights.petshelter.ui.detail.UserDataFieldLabel
import com.mockknights.petshelter.ui.detail.toDp
import com.mockknights.petshelter.ui.theme.GrayKiwoko
import com.mockknights.petshelter.ui.theme.moderatTextField

@Preview
@Composable
fun UserDataField(
    fieldLabel: String = "Dirección",
    userData: String = "Avenida Europa, 2",
    placeholderText: String = "Placeholder",
    keyboardType: KeyboardType = KeyboardType.Text,
    doneAction: ImeAction = ImeAction.Done,
    isPassword: Boolean = false,
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
            placeholderText = placeholderText,
            doneAction = doneAction,
            isPassword = isPassword,
            onUpdateValue = { onUpdateValue(it)},
            onDone = onDone
        )
    }
}

@Preview
@Composable
fun UserDataFieldTextField(
    userData: String = "Data in the field",
    keyboardType: KeyboardType = KeyboardType.Text,
    doneAction: ImeAction = ImeAction.Done,
    placeholderText: String = "Placeholder",
    isPassword: Boolean = false,
    onUpdateValue: (String) -> Unit = { },
    onDone : () -> Unit = { }
) {

    var text by remember { mutableStateOf(userData) }

    OutlinedTextField(
        value = text,
        placeholder = { TextPlaceholder(placeholderText) },
        visualTransformation = if (isPassword) PasswordVisualTransformation(mask = '*') else VisualTransformation.None,
        textStyle = MaterialTheme.typography.moderatTextField,
        singleLine = true,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = GrayKiwoko,
            unfocusedBorderColor = GrayKiwoko,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = doneAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        onValueChange = {
            text = it
            onUpdateValue(it)
        },
        )
}

