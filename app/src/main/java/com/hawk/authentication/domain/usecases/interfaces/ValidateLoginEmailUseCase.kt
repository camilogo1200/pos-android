package com.hawk.authentication.domain.usecases.interfaces

fun interface ValidateLoginEmailUseCase {
    operator fun invoke(email: String): Boolean
}
