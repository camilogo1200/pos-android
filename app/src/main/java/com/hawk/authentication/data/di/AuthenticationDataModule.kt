package com.hawk.authentication.data.di

import com.hawk.authentication.data.datasources.local.RoomAuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.local.interfaces.AuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.remote.AuthenticationApi
import com.hawk.authentication.data.datasources.remote.KeycloakAuthenticationRemoteDataSource
import com.hawk.authentication.data.datasources.remote.interfaces.AuthenticationRemoteDataSource
import com.hawk.authentication.data.repository.AuthenticationRepositoryImpl
import com.hawk.authentication.domain.repository.interfaces.AuthenticationRepository
import com.hawk.common.environment.AppEnvironment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationDataModule {

    @Binds
    abstract fun bindAuthenticationRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindAuthenticationRemoteDataSource(
        impl: KeycloakAuthenticationRemoteDataSource
    ): AuthenticationRemoteDataSource

    @Binds
    abstract fun bindAuthenticationLocalDataSource(
        impl: RoomAuthenticationLocalDataSource
    ): AuthenticationLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAuthenticationApi(
            retrofitBuilder: Retrofit.Builder,
            converterFactory: Converter.Factory
        ): AuthenticationApi = retrofitBuilder
            .baseUrl(AppEnvironment.keycloakBaseUrl)
            .addConverterFactory(converterFactory)
            .build()
            .create(AuthenticationApi::class.java)
    }
}
