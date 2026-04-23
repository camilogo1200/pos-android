package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerEmailUseCase {
    operator fun invoke(email: String): CustomerFieldValidationResult
}
