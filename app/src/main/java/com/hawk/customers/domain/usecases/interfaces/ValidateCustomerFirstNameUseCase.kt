package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerFirstNameUseCase {
    operator fun invoke(firstName: String): CustomerFieldValidationResult
}
