package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerPhoneUseCase {
    operator fun invoke(phoneNumber: String): CustomerFieldValidationResult
}
