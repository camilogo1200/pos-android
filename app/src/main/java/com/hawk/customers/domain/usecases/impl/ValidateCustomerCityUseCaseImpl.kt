package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCityUseCase
import javax.inject.Inject

class ValidateCustomerCityUseCaseImpl @Inject constructor() : ValidateCustomerCityUseCase {

    override fun invoke(city: String): CustomerFieldValidationResult =
        if (city.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
