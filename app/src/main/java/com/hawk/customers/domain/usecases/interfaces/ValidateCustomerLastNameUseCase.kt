package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerLastNameUseCase {
    operator fun invoke(lastName: String): CustomerFieldValidationResult
}
