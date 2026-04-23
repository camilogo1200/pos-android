package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerAddressLineOneUseCase {
    operator fun invoke(addressLineOne: String): CustomerFieldValidationResult
}
