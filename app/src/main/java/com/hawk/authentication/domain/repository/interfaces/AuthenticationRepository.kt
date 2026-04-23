package com.hawk.authentication.domain.repository.interfaces

import com.hawk.authentication.domain.entities.AuthenticationSession
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun authenticate(
        username: String,
        password: String
    ): Flow<Result<AuthenticationSession>>
}
