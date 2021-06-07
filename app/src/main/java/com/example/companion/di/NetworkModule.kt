package com.example.companion.di

import android.content.res.Resources
import com.example.companion.data.mapper.ApiMapper
import com.example.companion.data.network.CompanionApi
import com.example.companion.data.network.NetworkService
import com.example.companion.presentation.mapper.ProfileMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesCompanionApi(resources: Resources): CompanionApi {
        return NetworkService(resources).createCompanionApi()
    }

    @Singleton
    @Provides
    fun provideApiMapper(): ApiMapper {
        return ApiMapper()
    }

    @Singleton
    @Provides
    fun provideProfileMapper(): ProfileMapper {
        return ProfileMapper()
    }
}