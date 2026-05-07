package com.hawk.transactions.domain.repository.interfaces

import com.hawk.transactions.domain.entities.CreateTransactionDraft
import com.hawk.transactions.domain.entities.TransactionsFeed
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    fun getTransactions(): Flow<Result<TransactionsFeed>>

    fun createTransaction(transaction: CreateTransactionDraft): Flow<Result<Unit>>
}
