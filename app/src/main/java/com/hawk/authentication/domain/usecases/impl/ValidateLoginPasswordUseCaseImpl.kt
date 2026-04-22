package com.hawk.authentication.domain.usecases.impl

import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginPasswordUseCase
import javax.inject.Inject

class ValidateLoginPasswordUseCaseImpl @Inject constructor() : ValidateLoginPasswordUseCase {
    override fun invoke(password: String): Boolean = password.length >= 8
}
