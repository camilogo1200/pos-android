package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerDocumentNumberUseCase {
    operator fun invoke(documentNumber: String): CustomerFieldValidationResult
}
