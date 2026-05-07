package com.hawk.transactions.data.datasources.remote

import com.hawk.common.environment.AppEnvironment
import com.hawk.transactions.data.datasources.remote.interfaces.TransactionsRemoteDataSource
import com.hawk.transactions.data.dto.CreateTransactionRequestDto
import com.hawk.transactions.data.dto.TransactionsListResponseDto
import com.hawk.transactions.domain.errors.TransactionReadException
import com.hawk.transactions.domain.errors.TransactionWriteException
import com.hawk.utils.coroutines.IoDispatcher
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TransactionsRemoteDataSourceImpl @Inject constructor(
    private val transactionsApi: TransactionsApi,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : TransactionsRemoteDataSource {

    override fun getTransactions(): Flow<Result<TransactionsListResponseDto>> = flow {
        try {
            val response = transactionsApi.getTransactions(url = AppEnvironment.transactionsListPath)
            if (!response.isSuccessful) {
                emit(Result.failure(TransactionReadException.RequestFailed(response.code())))
                return@flow
            }

            val body = response.body()
            if (body == null) {
                emit(Result.failure(TransactionReadException.RequestFailed(response.code())))
                return@flow
            }

            emit(Result.success(body))
        } catch (exception: IOException) {
            emit(Result.failure(TransactionReadException.RequestFailed()))
        } catch (exception: Exception) {
            emit(Result.failure(TransactionReadException.RequestFailed()))
        }
    }.flowOn(coroutineDispatcher)

    override fun createTransaction(request: CreateTransactionRequestDto): Flow<Result<Unit>> = flow {
        try {
            val response = transactionsApi.createTransaction(
                url = AppEnvironment.transactionsCreatePath,
                body = request
            )

            if (!response.isSuccessful) {
                emit(Result.failure(TransactionWriteException.RequestFailed(response.code())))
                return@flow
            }

            emit(Result.success(Unit))
        } catch (exception: IOException) {
            emit(Result.failure(TransactionWriteException.RequestFailed()))
        } catch (exception: Exception) {
            emit(Result.failure(TransactionWriteException.RequestFailed()))
        }
    }.flowOn(coroutineDispatcher)
}
