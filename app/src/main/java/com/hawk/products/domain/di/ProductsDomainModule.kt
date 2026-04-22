package com.hawk.products.domain.di

import com.hawk.products.domain.usecases.impl.GetProductsUseCaseImpl
import com.hawk.products.domain.usecases.interfaces.GetProductsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductsDomainModule {
    @Binds
    abstract fun bindGetProductsUseCase(
        impl: GetProductsUseCaseImpl
    ): GetProductsUseCase
}
