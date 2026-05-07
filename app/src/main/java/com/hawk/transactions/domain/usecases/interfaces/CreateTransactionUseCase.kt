package com.hawk.transactions.domain.usecases.interfaces

import com.hawk.transactions.domain.entities.CreateTransactionDraft
import kotlinx.coroutines.flow.Flow

interface CreateTransactionUseCase {
    operator fun invoke(transaction: CreateTransactionDraft): Flow<Result<Unit>>
}
