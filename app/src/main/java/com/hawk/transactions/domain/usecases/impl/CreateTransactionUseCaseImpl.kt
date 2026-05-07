package com.hawk.transactions.domain.usecases.impl

import com.hawk.transactions.domain.entities.CreateTransactionDraft
import com.hawk.transactions.domain.repository.interfaces.TransactionsRepository
import com.hawk.transactions.domain.usecases.interfaces.CreateTransactionUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CreateTransactionUseCaseImpl @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) : CreateTransactionUseCase {

    override fun invoke(transaction: CreateTransactionDraft): Flow<Result<Unit>> =
        transactionsRepository.createTransaction(transaction)
}
