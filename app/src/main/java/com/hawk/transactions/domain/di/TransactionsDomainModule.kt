package com.hawk.transactions.domain.di

import com.hawk.transactions.domain.usecases.impl.CreateTransactionUseCaseImpl
import com.hawk.transactions.domain.usecases.impl.GetTransactionsUseCaseImpl
import com.hawk.transactions.domain.usecases.interfaces.CreateTransactionUseCase
import com.hawk.transactions.domain.usecases.interfaces.GetTransactionsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TransactionsDomainModule {

    @Binds
    abstract fun bindGetTransactionsUseCase(
        impl: GetTransactionsUseCaseImpl
    ): GetTransactionsUseCase

    @Binds
    abstract fun bindCreateTransactionUseCase(
        impl: CreateTransactionUseCaseImpl
    ): CreateTransactionUseCase
}
