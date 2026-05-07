package com.hawk.transactions.domain.usecases.interfaces

import com.hawk.transactions.domain.entities.TransactionsFeed
import kotlinx.coroutines.flow.Flow

interface GetTransactionsUseCase {
    operator fun invoke(): Flow<Result<TransactionsFeed>>
}
