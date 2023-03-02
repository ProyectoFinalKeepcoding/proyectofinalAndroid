package com.mockknights.petshelter.testUtils.fakeData

import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.domain.PetShelter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object FakeDetailData {
    const val successId = "id"
    const val errorId = "errorId"

    fun getDetail(id: String): Flow<PetShelterRemote> {
        return when(id) {
            successId -> flow { emit(FakePetShelterData.getRemotePetShelter()) }
            else -> emptyFlow()
        }
    }
}