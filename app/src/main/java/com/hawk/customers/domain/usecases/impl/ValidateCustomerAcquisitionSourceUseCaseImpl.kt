package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAcquisitionSourceUseCase
import javax.inject.Inject

class ValidateCustomerAcquisitionSourceUseCaseImpl @Inject constructor() :
    ValidateCustomerAcquisitionSourceUseCase {

    override fun invoke(source: String): CustomerFieldValidationResult =
        if (source.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
