package com.mockknights.petshelter.testUtils.fakeData

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.domain.ShelterType

object FakeMapData {

    fun getPetShelterList(): List<PetShelterRemote> {
        val shelterVeterinary2 = PetShelterRemote(id = "0", name = "VeterinarySevillaLaNuevaWithLongName", phoneNumber = "666909898", address = Address(latitude = 40.3475422, longitude = -4.0275268), shelterType = ShelterType.VETERINARY, password = "123456", photoURL = "")
        val shelterParticular1 = PetShelterRemote(id = "1", name = "Fran", phoneNumber = "44433421", address = Address(latitude = 39.865762, longitude =-4.030329), shelterType = ShelterType.PARTICULAR, password = "123456", photoURL = "")
        val shelterParticular2 = PetShelterRemote(id = "2", name = "Isma", phoneNumber = "44433421", address = Address(latitude = 40.405775, longitude =-3.996504), shelterType = ShelterType.PARTICULAR, password = "123456", photoURL = "")
        val shelterParticular3 = PetShelterRemote(id = "3", name = "Joakin", phoneNumber = "44433421", address = Address(latitude = 40.422989, longitude = -3.637153), shelterType = ShelterType.PARTICULAR, password = "123456", photoURL = "")
        val shelterVeterinary = PetShelterRemote(id = "4", name = "Aitor", phoneNumber = "44433421", address = Address(latitude = 43.262217, longitude =-2.872610), shelterType= ShelterType.VETERINARY, password = "123456", photoURL = "")
        val localGovernment = PetShelterRemote(id = "5", name = "Robert", phoneNumber = "44433421", address = Address(latitude = 43.229454, longitude =-3.205004), shelterType =  ShelterType.LOCAL_GOVERNMENT, password = "123456", photoURL = "")
        val kiwokoStore1 = PetShelterRemote(id = "6", name = "KiwokoStore1", phoneNumber = "23423242112454232", address = Address(latitude = 40.4410353, longitude = -3.9992455), shelterType = ShelterType.KIWOKO_STORE, password = "123456", photoURL = "")
        val kiwokoStore2 = PetShelterRemote(id = "7", name = "KiwokoStore2", phoneNumber = "918167789", address = Address(latitude = 40.45283915, longitude = -3.8682917800496277), shelterType = ShelterType.KIWOKO_STORE, password = "123456", photoURL = "")
        val kiwokoStore3 = PetShelterRemote(id = "8", name = "KiwokoStore3", phoneNumber = "918167787", address = Address(latitude = 40.3446868, longitude = -3.8486131), shelterType = ShelterType.KIWOKO_STORE, password = "123456", photoURL = "")

        return listOf(shelterVeterinary2, shelterParticular1, shelterParticular2, shelterParticular3, shelterVeterinary, localGovernment, kiwokoStore1, kiwokoStore2, kiwokoStore3)
    }

    fun getLocation(): LatLng {
        return LatLng(30.0, 30.0)
    }
}