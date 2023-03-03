package com.mockknights.petshelter.testUtils.fakeData

import com.mockknights.petshelter.data.remote.request.RegisterRequest

object FakeRegisterData {

    fun getRegisterRequest(valid: Boolean): RegisterRequest {
        val shelter = if(valid) FakePetShelterData.getPetShelter() else FakePetShelterData.getEmptiedPetShelter()
        return RegisterRequest(
            name = shelter.name,
            password = shelter.password,
            address = shelter.address,
            phoneNumber =  shelter.phoneNumber,
            shelterType = shelter.shelterType
        )
    }
}