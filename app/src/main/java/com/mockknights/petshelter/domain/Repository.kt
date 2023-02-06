package com.mockknights.petshelter.domain

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllPetShelter(): Flow<List<PetShelter>>
}