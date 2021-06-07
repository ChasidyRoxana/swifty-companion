package com.example.companion.di

import android.content.res.Resources
import com.example.companion.data.mapper.ApiMapper
import com.example.companion.data.network.CompanionApi
import com.example.companion.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRepository(
        api: CompanionApi,
        mapper: ApiMapper,
        resources: Resources
    ): Repository {
        return Repository(api, mapper, resources)
    }
}