package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerBirthDateUseCase {
    operator fun invoke(birthDate: String): CustomerFieldValidationResult
}
