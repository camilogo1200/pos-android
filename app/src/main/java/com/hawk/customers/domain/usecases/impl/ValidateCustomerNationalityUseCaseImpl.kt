package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerNationalityUseCase
import javax.inject.Inject

class ValidateCustomerNationalityUseCaseImpl @Inject constructor() :
    ValidateCustomerNationalityUseCase {

    override fun invoke(countryCode: String): CustomerFieldValidationResult =
        if (countryCode.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
