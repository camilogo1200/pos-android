package com.hawk.authentication.data.repository

import com.hawk.authentication.data.datasources.local.interfaces.AuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.remote.interfaces.AuthenticationRemoteDataSource
import com.hawk.authentication.data.mappers.toDomain
import com.hawk.authentication.data.repository.interfaces.AuthenticationRepository
import com.hawk.authentication.domain.entities.AuthenticationSession
import com.hawk.utils.coroutines.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override fun authenticate(
        username: String,
        password: String
    ): Flow<Result<AuthenticationSession>> = flow {
        authenticationRemoteDataSource.authenticate(username, password).collect { result ->
            result
                .map { dto -> dto.toDomain(username) }
                .onSuccess { session ->
                    authenticationLocalDataSource.saveSession(session)
                    emit(Result.success(session))
                }
                .onFailure { throwable ->
                    authenticationLocalDataSource.clearSession()
                    emit(Result.failure(throwable))
                }
        }
    }.flowOn(coroutineDispatcher)
}
