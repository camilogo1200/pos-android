package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCountryUseCase
import javax.inject.Inject

class ValidateCustomerCountryUseCaseImpl @Inject constructor() : ValidateCustomerCountryUseCase {

    override fun invoke(countryCode: String): CustomerFieldValidationResult =
        if (countryCode.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
