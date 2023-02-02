package com.mockknights.petshelter.data

import com.mockknights.petshelter.data.local.LocalDataSource
import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.mappers.PetShelterMapper
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    //private val localDataSource: LocalDataSource,
    private val mapper: PetShelterMapper
    ) : Repository {


    override suspend fun getAllPetShelter(): Flow<List<PetShelter>> {
        return remoteDataSource.getAllPetShelter().map { petShelterList -> mapper.mapPetShelterRemoteToPresentation(petShelterList) }
    }
}