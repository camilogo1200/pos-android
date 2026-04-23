package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerCityUseCase {
    operator fun invoke(city: String): CustomerFieldValidationResult
}
