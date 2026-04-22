package com.hawk.products.data.di

import com.hawk.products.data.datasources.remote.ProductsApi
import com.hawk.products.data.datasources.remote.ProductsRemoteDataSourceImpl
import com.hawk.products.data.datasources.remote.interfaces.ProductsRemoteDataSource
import com.hawk.products.data.repository.ProductsRepositoryImpl
import com.hawk.products.data.repository.interfaces.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsDataModule {

    @Binds
    abstract fun bindProductsRepository(
        impl: ProductsRepositoryImpl
    ): ProductsRepository

    @Binds
    abstract fun bindProductsRemoteDataSource(
        impl: ProductsRemoteDataSourceImpl
    ): ProductsRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideProductsApi(
            retrofit: Retrofit
        ): ProductsApi = retrofit.create(ProductsApi::class.java)
    }
}
