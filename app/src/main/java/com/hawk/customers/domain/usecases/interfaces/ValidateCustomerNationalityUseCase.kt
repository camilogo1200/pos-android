package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerNationalityUseCase {
    operator fun invoke(countryCode: String): CustomerFieldValidationResult
}
