package com.hawk.transactions.data.repository

import com.hawk.transactions.data.datasources.remote.interfaces.TransactionsRemoteDataSource
import com.hawk.transactions.data.mappers.toDomain
import com.hawk.transactions.data.mappers.toRequestDto
import com.hawk.transactions.domain.entities.CreateTransactionDraft
import com.hawk.transactions.domain.entities.TransactionsFeed
import com.hawk.transactions.domain.errors.TransactionReadException
import com.hawk.transactions.domain.errors.TransactionWriteException
import com.hawk.transactions.domain.repository.interfaces.TransactionsRepository
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.network.NetworkManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class TransactionsRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val transactionsRemoteDataSource: TransactionsRemoteDataSource,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : TransactionsRepository {

    override fun getTransactions(): Flow<Result<TransactionsFeed>> = flow {
        if (!networkManager.isNetworkAvailable()) {
            emit(Result.failure(TransactionReadException.NoConnection))
        } else {
            emitAll(
                transactionsRemoteDataSource.getTransactions()
                    .map { result -> result.map { response -> response.toDomain() } }
            )
        }
    }.flowOn(coroutineDispatcher)

    override fun createTransaction(transaction: CreateTransactionDraft): Flow<Result<Unit>> = flow {
        if (!networkManager.isNetworkAvailable()) {
            emit(Result.failure(TransactionWriteException.NoConnection))
        } else {
            emitAll(transactionsRemoteDataSource.createTransaction(transaction.toRequestDto()))
        }
    }.flowOn(coroutineDispatcher)
}
