package com.mockknights.petshelter.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "petshelter")
data class PetShelterLocal (
    @PrimaryKey @ColumnInfo(name = "id") val id: UUID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String,
    //@ColumnInfo(name = "address") val address: Address,
    //@ColumnInfo(name = "shelterType") val shelterType: ShelterType,
    @ColumnInfo(name = "photoURL") val photoURL: String
        )