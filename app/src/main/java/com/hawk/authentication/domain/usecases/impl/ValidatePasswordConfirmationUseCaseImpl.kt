package com.hawk.authentication.domain.usecases.impl

import com.hawk.authentication.domain.usecases.interfaces.ValidatePasswordConfirmationUseCase
import javax.inject.Inject

class ValidatePasswordConfirmationUseCaseImpl @Inject constructor() :
    ValidatePasswordConfirmationUseCase {

    override fun invoke(
        password: String,
        confirmPassword: String
    ): Boolean = password.isNotBlank() && password == confirmPassword
}
