package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerAcquisitionSourceUseCase {
    operator fun invoke(source: String): CustomerFieldValidationResult
}
