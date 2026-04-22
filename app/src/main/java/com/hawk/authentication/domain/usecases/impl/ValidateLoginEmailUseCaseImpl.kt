package com.hawk.authentication.domain.usecases.impl

import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginEmailUseCase
import javax.inject.Inject

class ValidateLoginEmailUseCaseImpl @Inject constructor() : ValidateLoginEmailUseCase {

    private val emailRegex = Regex(pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override fun invoke(email: String): Boolean = emailRegex.matches(email.trim())
}
