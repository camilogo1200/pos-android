package com.hawk.authentication.domain.usecases.interfaces

fun interface ValidateLoginPasswordUseCase {
    operator fun invoke(password: String): Boolean
}
