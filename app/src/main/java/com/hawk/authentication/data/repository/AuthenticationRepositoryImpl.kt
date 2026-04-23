package com.hawk.authentication.data.repository

import com.hawk.authentication.data.datasources.local.interfaces.AuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.remote.interfaces.AuthenticationRemoteDataSource
import com.hawk.authentication.data.mappers.toDomain
import com.hawk.authentication.domain.entities.AuthenticationException
import com.hawk.authentication.domain.entities.AuthenticationSession
import com.hawk.authentication.domain.repository.interfaces.AuthenticationRepository
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.network.NetworkManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class AuthenticationRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override fun authenticate(
        username: String,
        password: String
    ): Flow<Result<AuthenticationSession>> = flow {
        authenticateRemotely(username, password).collect { result ->
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

    private fun authenticateRemotely(
        username: String,
        password: String
    ) = if (networkManager.isNetworkAvailable()) {
        authenticationRemoteDataSource.authenticate(username, password)
    } else {
        flowOf(Result.failure(AuthenticationException.ConnectionFailed()))
    }
}
