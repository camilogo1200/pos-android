package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerLastNameUseCase
import javax.inject.Inject

class ValidateCustomerLastNameUseCaseImpl @Inject constructor() :
    ValidateCustomerLastNameUseCase {

    private val nameRegex = Regex("^[A-Za-zÀ-ÿ ]+$")

    override fun invoke(lastName: String): CustomerFieldValidationResult {
        val trimmedValue = lastName.trim()
        if (trimmedValue.isEmpty()) {
            return CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        }

        return if (nameRegex.matches(trimmedValue)) {
            CustomerFieldValidationResult()
        } else {
            CustomerFieldValidationResult(CustomerFieldValidationError.LETTERS_ONLY)
        }
    }
}
