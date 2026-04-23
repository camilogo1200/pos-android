package com.hawk.authentication.data.datasources.remote

import com.hawk.authentication.data.datasources.remote.interfaces.AuthenticationRemoteDataSource
import com.hawk.authentication.data.dto.KeycloakTokenDto
import com.hawk.authentication.domain.entities.AuthenticationException
import com.hawk.common.environment.AppEnvironment
import com.hawk.utils.coroutines.IoDispatcher
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class KeycloakAuthenticationRemoteDataSource @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : AuthenticationRemoteDataSource {

    override fun authenticate(
        username: String,
        password: String
    ): Flow<Result<KeycloakTokenDto>> = flow {
        try {
            val response = authenticationApi.authenticate(
                url = AppEnvironment.keycloakTokenPath,
                clientId = AppEnvironment.keycloakClientId,
                grantType = AppEnvironment.keycloakGrantType,
                username = username,
                password = password
            )

            when {
                response.isSuccessful -> {
                    val dto = response.body()
                    if (dto == null) {
                        emit(Result.failure(AuthenticationException.InvalidPayload()))
                    } else {
                        emit(Result.success(dto))
                    }
                }

                response.code() == 400 || response.code() == 401 -> {
                    emit(Result.failure(AuthenticationException.InvalidCredentials()))
                }

                else -> {
                    emit(Result.failure(AuthenticationException.ConnectionFailed()))
                }
            }
        } catch (exception: IOException) {
            emit(Result.failure(AuthenticationException.ConnectionFailed(exception)))
        } catch (exception: AuthenticationException) {
            emit(Result.failure(exception))
        } catch (exception: Exception) {
            emit(Result.failure(AuthenticationException.ConnectionFailed(exception)))
        }
    }.flowOn(coroutineDispatcher)
}
