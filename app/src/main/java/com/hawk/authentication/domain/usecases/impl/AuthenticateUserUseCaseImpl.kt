package com.hawk.authentication.domain.usecases.impl

import com.hawk.authentication.data.repository.interfaces.AuthenticationRepository
import com.hawk.authentication.domain.entities.AuthenticationSession
import com.hawk.authentication.domain.usecases.interfaces.AuthenticateUserUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthenticateUserUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : AuthenticateUserUseCase {

    override fun invoke(
        username: String,
        password: String
    ): Flow<Result<AuthenticationSession>> = authenticationRepository.authenticate(
        username = username,
        password = password
    )
}
