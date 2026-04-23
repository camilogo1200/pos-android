package com.hawk.customers.data.di

import com.hawk.common.environment.AppEnvironment
import com.hawk.customers.data.datasources.local.RoomCustomersLocalDataSource
import com.hawk.customers.data.datasources.local.interfaces.CustomersLocalDataSource
import com.hawk.customers.data.datasources.remote.CustomersApi
import com.hawk.customers.data.datasources.remote.CustomersRemoteDataSourceImpl
import com.hawk.customers.data.datasources.remote.interfaces.CustomersRemoteDataSource
import com.hawk.customers.data.repository.CustomersRepositoryImpl
import com.hawk.customers.domain.repository.interfaces.CustomersRepository
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
abstract class CustomersDataModule {

    @Binds
    abstract fun bindCustomersRepository(
        impl: CustomersRepositoryImpl
    ): CustomersRepository

    @Binds
    abstract fun bindCustomersRemoteDataSource(
        impl: CustomersRemoteDataSourceImpl
    ): CustomersRemoteDataSource

    @Binds
    abstract fun bindCustomersLocalDataSource(
        impl: RoomCustomersLocalDataSource
    ): CustomersLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideCustomersApi(
            retrofitBuilder: Retrofit.Builder,
            converterFactory: Converter.Factory
        ): CustomersApi = retrofitBuilder
            .baseUrl(AppEnvironment.customersBaseUrl)
            .addConverterFactory(converterFactory)
            .build()
            .create(CustomersApi::class.java)
    }
}
