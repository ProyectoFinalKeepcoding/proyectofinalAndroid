package com.mockknights.petshelter.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _petShelter = MutableStateFlow(emptyList<PetShelter>())
    val petShelter: MutableStateFlow<List<PetShelter>> get() = _petShelter

    private val _modalShelterList = MutableStateFlow(emptyList<PetShelter>())
    val modalShelterList: MutableStateFlow<List<PetShelter>> get() = _modalShelterList

    private val _sheetState = MutableStateFlow(BottomSheetState.COLLAPSED)
    val sheetState: MutableStateFlow<BottomSheetState> get() = _sheetState

    private val _permissionState = MutableStateFlow(false)
    val permissionState: MutableStateFlow<Boolean> get() = _permissionState

    private fun setValueOnMainThreadShelter(value: List<PetShelter>) {
        viewModelScope.launch(Dispatchers.Main) {
            _petShelter.value = value
        }
    }

    init {
        getAllPetShelter()
    }

    fun getAllPetShelter() {
        viewModelScope.launch {
            repository.getAllPetShelter().flowOn(Dispatchers.IO).collect() {
                setValueOnMainThreadShelter(it)
            }
        }
    }

    fun setModalShelter(shelterName: String) {
        val modalPetShelter = petShelter.value.filter { it.name == shelterName }
        viewModelScope.launch(Dispatchers.Main) {
            _modalShelterList.value = modalPetShelter
        }
    }

    fun toggleModal() {
        when (_sheetState.value) {
            BottomSheetState.COLLAPSED -> expand()
            BottomSheetState.EXPANDED -> collapse()
        }
    }

    private fun expand() {
        viewModelScope.launch(Dispatchers.Main) {
            _sheetState.value = BottomSheetState.EXPANDED
        }
    }

    fun collapse() {
        viewModelScope.launch(Dispatchers.Main) {
            _sheetState.value = BottomSheetState.COLLAPSED
        }
    }

    fun getShelterIconByShelterType(shelterType: String): Int {
        return when (shelterType) {
            ShelterType.PARTICULAR.stringValue -> R.drawable.particular
            ShelterType.LOCAL_GOVERNMENT.stringValue -> R.drawable.towncouncil
            ShelterType.VETERINARY.stringValue -> R.drawable.veterinary
            ShelterType.SHELTER_POINT.stringValue -> R.drawable.animalshelter
            ShelterType.KIWOKO_STORE.stringValue -> R.drawable.kiwoko
            else -> R.drawable.questionmark
        }
    }

    fun setPermissionState(value: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _permissionState.value = value
            println(permissionState.value)
        }
    }
}