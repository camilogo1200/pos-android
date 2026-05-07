package com.hawk.transactions.domain.usecases.impl

import com.hawk.transactions.domain.entities.TransactionsFeed
import com.hawk.transactions.domain.repository.interfaces.TransactionsRepository
import com.hawk.transactions.domain.usecases.interfaces.GetTransactionsUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCaseImpl @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) : GetTransactionsUseCase {

    override fun invoke(): Flow<Result<TransactionsFeed>> = transactionsRepository.getTransactions()
}
