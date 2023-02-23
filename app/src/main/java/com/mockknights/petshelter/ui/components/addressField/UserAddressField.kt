package com.mockknights.petshelter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.places.api.Places
import com.mockknights.petshelter.BuildConfig
import com.mockknights.petshelter.ui.components.addressField.UserAddressFieldViewModel
import com.mockknights.petshelter.ui.detail.UserDataFieldLabel
import com.mockknights.petshelter.ui.detail.toDp
import com.mockknights.petshelter.ui.theme.GrayKiwoko
import com.mockknights.petshelter.ui.theme.moderatTextField

@Composable
fun UserAddressField(viewModel: UserAddressFieldViewModel = hiltViewModel(), onUpdateData: (String) -> Unit) {

    val localContext = LocalContext.current

    LaunchedEffect(key1 = "initializePlacesClient"){
        Places.initialize(localContext, BuildConfig.MAPS_API_KEY)
        viewModel.placesClient = Places.createClient(localContext)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.toDp().dp),
        ) {
            UserDataFieldLabel("Dirección")
            OutlinedTextField(
                value = text,
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
                    imeAction = ImeAction.Next,
                ),
                onValueChange = {
                    text = it
                    onUpdateData(it)
                    viewModel.searchPlaces(it)
                }
            )
        }
        // Suggestions column, only 3 results
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.locationAutofill.take(3)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            text = it.address
                            viewModel.locationAutofill.clear()
                            viewModel.getCoordinates(it)
                        }
                ) {
                    Text(it.address)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}