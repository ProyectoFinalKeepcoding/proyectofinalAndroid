package com.mockknights.petshelter.di

import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.data.local.LocalDataSource
import com.mockknights.petshelter.data.local.LocalDataSourceImpl
import com.mockknights.petshelter.data.remote.RemoteDataSource
import com.mockknights.petshelter.data.remote.RemoteDataSourceImpl
import com.mockknights.petshelter.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    //@Binds
    //abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

}