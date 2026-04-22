package com.hawk.authentication.domain.usecases.interfaces

interface ValidatePasswordConfirmationUseCase {
    operator fun invoke(
        password: String,
        confirmPassword: String
    ): Boolean
}
