package com.mockknights.petshelter.ui.map

import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.R
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _petShelter = MutableStateFlow(emptyList<PetShelter>())
    val petShelter: MutableStateFlow<List<PetShelter>> get() = _petShelter

    private val _modalShelterList = MutableStateFlow(emptyList<PetShelter>())
    val modalShelterList: MutableStateFlow<List<PetShelter>> get() = _modalShelterList

    private val _sheetState = MutableStateFlow(BottomSheetState(initialValue = BottomSheetValue.Collapsed))
    val bottomSheetScaffoldState = BottomSheetScaffoldState(
        bottomSheetState = _sheetState.value,
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )

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

    fun toggleModal(coroutineScope: CoroutineScope) {
        // Needed to use composable functions
        coroutineScope.launch {
            when (_sheetState.value.isCollapsed) {
                true -> _sheetState.value.expand()
                false -> _sheetState.value.collapse()
            }
        }
    }

    fun collapseModal(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            _sheetState.value.collapse()
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
}