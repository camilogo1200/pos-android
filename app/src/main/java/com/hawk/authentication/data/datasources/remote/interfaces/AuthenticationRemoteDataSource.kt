package com.hawk.authentication.data.datasources.remote.interfaces

import com.hawk.authentication.data.dto.KeycloakTokenDto
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteDataSource {
    fun authenticate(
        username: String,
        password: String
    ): Flow<Result<KeycloakTokenDto>>
}
