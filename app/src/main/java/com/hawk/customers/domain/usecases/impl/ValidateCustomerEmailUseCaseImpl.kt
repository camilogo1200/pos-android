package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerEmailUseCase
import javax.inject.Inject

class ValidateCustomerEmailUseCaseImpl @Inject constructor() : ValidateCustomerEmailUseCase {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override fun invoke(email: String): CustomerFieldValidationResult {
        val trimmedValue = email.trim()
        if (trimmedValue.isEmpty()) {
            return CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        }

        return if (emailRegex.matches(trimmedValue)) {
            CustomerFieldValidationResult()
        } else {
            CustomerFieldValidationResult(CustomerFieldValidationError.INVALID_EMAIL)
        }
    }
}
