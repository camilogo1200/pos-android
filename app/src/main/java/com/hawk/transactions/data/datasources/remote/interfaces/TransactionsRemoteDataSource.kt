package com.hawk.transactions.data.datasources.remote.interfaces

import com.hawk.transactions.data.dto.CreateTransactionRequestDto
import com.hawk.transactions.data.dto.TransactionsListResponseDto
import kotlinx.coroutines.flow.Flow

interface TransactionsRemoteDataSource {
    fun getTransactions(): Flow<Result<TransactionsListResponseDto>>

    fun createTransaction(request: CreateTransactionRequestDto): Flow<Result<Unit>>
}
