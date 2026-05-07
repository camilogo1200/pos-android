package com.hawk.transactions.data.di

import com.hawk.common.environment.AppEnvironment
import com.hawk.transactions.data.datasources.remote.TransactionsApi
import com.hawk.transactions.data.datasources.remote.TransactionsRemoteDataSourceImpl
import com.hawk.transactions.data.datasources.remote.interfaces.TransactionsRemoteDataSource
import com.hawk.transactions.data.repository.TransactionsRepositoryImpl
import com.hawk.transactions.domain.repository.interfaces.TransactionsRepository
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
abstract class TransactionsDataModule {

    @Binds
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository

    @Binds
    abstract fun bindTransactionsRemoteDataSource(
        impl: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideTransactionsApi(
            retrofitBuilder: Retrofit.Builder,
            converterFactory: Converter.Factory
        ): TransactionsApi = retrofitBuilder
            .baseUrl(AppEnvironment.transactionsBaseUrl)
            .addConverterFactory(converterFactory)
            .build()
            .create(TransactionsApi::class.java)
    }
}
