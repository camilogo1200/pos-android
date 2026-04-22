package com.hawk.authentication.domain.usecases.interfaces

import com.hawk.authentication.domain.entities.AuthenticationSession
import kotlinx.coroutines.flow.Flow

interface AuthenticateUserUseCase {
    operator fun invoke(
        username: String,
        password: String
    ): Flow<Result<AuthenticationSession>>
}
