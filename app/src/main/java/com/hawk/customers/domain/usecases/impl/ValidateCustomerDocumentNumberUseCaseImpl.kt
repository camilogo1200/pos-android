package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerDocumentNumberUseCase
import javax.inject.Inject

class ValidateCustomerDocumentNumberUseCaseImpl @Inject constructor() :
    ValidateCustomerDocumentNumberUseCase {

    private val documentRegex = Regex("^[A-Za-z0-9-]{5,40}$")

    override fun invoke(documentNumber: String): CustomerFieldValidationResult {
        val trimmedValue = documentNumber.trim()
        if (trimmedValue.isEmpty()) {
            return CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        }

        return if (documentRegex.matches(trimmedValue)) {
            CustomerFieldValidationResult()
        } else {
            CustomerFieldValidationResult(CustomerFieldValidationError.INVALID_DOCUMENT)
        }
    }
}
