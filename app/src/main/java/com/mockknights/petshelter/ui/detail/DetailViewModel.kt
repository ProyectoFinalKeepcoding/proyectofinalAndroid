package com.mockknights.petshelter.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Detail screen.
 * The ViewModel works with the Repository to manage the data.
 * @param repository The repository that manages the data
 * @param coroutineDispatcher The coroutine dispatcher working in the IO thread. It is injected to avoid multiple threads when hardcoding it.
 */
@HiltViewModel
class DetailViewModel@Inject constructor(private val repository: Repository, private val coroutineDispatcher: CoroutineDispatcher): ViewModel() {

    /**
     * The state of the detail screen. When working with the detail state inside the detail view model, use this value.
     */
    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)
    /**
     * The state of the detail screen visible outside the detail view model.
     */
    val detailState: MutableStateFlow<DetailState> get() = _detailState

    /**
     * Get the shelter detail by [id] from the repository and set the value of [_detailState] to
     * [DetailState.Success] with the gotten shelter.
     * @throws NoSuchElementException If the shelter is not found, the value of [_detailState] is set
     * to [DetailState.Error] with the following error message: "No shelter found with id: [id]"
     */
    fun getShelterDetail(id: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val result = repository.getShelter(id).flowOn(coroutineDispatcher)
            try {
                _detailState.value = DetailState.Success(result.first())
            } catch (e: NoSuchElementException) {
                _detailState.value = DetailState.Error("No shelter found with id: $id")
            }
        }
    }

    /**
     * Update the shelter value of [_detailState] with a new [shelterType] value.
     */
    fun onUpdatedShelterType(shelterType: ShelterType) {
        updateShelterType(shelterType)
    }
    /**
     * Update the shelter value of [_detailState] with a new [shelterType] value in the [coroutineDispatcher].
     * It only updates the value if the [_detailState] is [DetailState.Success].
     */
    private fun updateShelterType(shelterType: ShelterType) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(shelterType = shelterType)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    /**
     * Update the phone value of [_detailState] with a new [phone] value.
     */
    fun onUpdatedPhone(phone: String) {
        updatePhone(phone)
    }
    /**
     * Update the phone value of [_detailState] with a new [phone] value in the [coroutineDispatcher].
     * It only updates the value if the [_detailState] is [DetailState.Success].
     */
    private fun updatePhone(phone: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(phoneNumber = phone)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    /**
     * Update the address value of [_detailState] with a new address value containing the [latitude]
     * and [longitude] values.
     */
    fun onUpdatedAddress(latitude: String, longitude: String) {
        updateAddress(latitude, longitude)
    }
    /**
     * Update the address value of [_detailState] with a new address value containing the [latitude]
     * and [longitude] values in the [coroutineDispatcher]. It only updates the value if the [_detailState]
     * is [DetailState.Success].
     */
    private fun updateAddress(latitude: String, longitude: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(address = Address(latitude.toDouble(), longitude.toDouble()))
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }

    /**
     * Update the name value of [_detailState] with a new [name] value.
     */
    fun onEditName(name: String) {
        updateUserName(name)
    }
    /**
     * Update the name value of [_detailState] with a new [name] value in the [coroutineDispatcher].
     * It only updates the value if the [_detailState] is [DetailState.Success].
     */
    private fun updateUserName(name: String) {
        val updatedShelter = (_detailState.value as? DetailState.Success)?.petShelter?.copy(name = name)
        updatedShelter?.let { setValueOnIOThread(DetailState.Success(it)) }
    }
    /**
     * Update the image value of [_detailState].
     */

    fun onImageClicked() {
        updateImage()
    }
    /**
     * Update the photoURL value of [_detailState] in the [coroutineDispatcher] and sets it to
     * "id.png". It only updates the value if the [_detailState] is [DetailState.Success].
     */
    private fun updateImage() {
        val detailStateSuccess = _detailState.value as? DetailState.Success
        detailStateSuccess ?: return
        val petShelter = detailStateSuccess.petShelter
        if (petShelter.photoURL.isNotEmpty()) return // already has an image url, not needed to set one
        val updatedShelter = petShelter.copy(photoURL = "${petShelter.id}.png")
        setValueOnIOThread(DetailState.Success(updatedShelter))
    }

    /**
     * Update the shelter in the server with the value of [_detailState] using the [coroutineDispatcher].
     * It only updates the value if the [_detailState] is [DetailState.Success].
     */
    fun onSaveClicked(context: Context) {
        val petShelter = (_detailState.value as? DetailState.Success)?.petShelter
        petShelter?.let {
            if(checkEmptyProperties(it, context)) {
                val id = it.id
                viewModelScope.launch(coroutineDispatcher) {
                    repository.updateShelter(id, it)
                    mToast(context, "DATOS ACTUALIZADOS")
                }
            }
        }
    }

    /**
     * Check if the [petShelter] has empty properties and show a toast message if it has.
     * @return True if the [petShelter] has no empty properties, false otherwise.
     */
    private fun checkEmptyProperties(petShelter : PetShelter, context: Context): Boolean {
        if(petShelter.name.isEmpty()){
            mToast(context, "NOMBRE VACÍO")
            return false
        }
        if(petShelter.phoneNumber.isEmpty()){
            mToast(context, "TELÉFONO VACÍO")
            return false
        }
        return true
    }

    /**
     * Set the value of [_detailState] in the [coroutineDispatcher].
     */
    private fun setValueOnIOThread(value: DetailState) {
        viewModelScope.launch(coroutineDispatcher) {
            _detailState.value = value
        }
    }

    /**
     * Show a toast message on the UI thread.
     * @param context The context of the application.
     * @param message The message to show.
     */
    fun mToast(context: Context, message: String){
        viewModelScope.launch {
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}


