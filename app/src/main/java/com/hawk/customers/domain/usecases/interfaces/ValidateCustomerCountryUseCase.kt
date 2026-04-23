package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerCountryUseCase {
    operator fun invoke(countryCode: String): CustomerFieldValidationResult
}
