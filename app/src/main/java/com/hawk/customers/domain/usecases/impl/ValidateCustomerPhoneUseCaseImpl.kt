package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPhoneUseCase
import javax.inject.Inject

class ValidateCustomerPhoneUseCaseImpl @Inject constructor() : ValidateCustomerPhoneUseCase {

    private val phoneRegex = Regex("^[+0-9()\\- ]{7,20}$")

    override fun invoke(phoneNumber: String): CustomerFieldValidationResult {
        val trimmedValue = phoneNumber.trim()
        if (trimmedValue.isEmpty()) {
            return CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        }

        val digitsCount = trimmedValue.count(Char::isDigit)
        return if (phoneRegex.matches(trimmedValue) && digitsCount in 7..15) {
            CustomerFieldValidationResult()
        } else {
            CustomerFieldValidationResult(CustomerFieldValidationError.INVALID_PHONE)
        }
    }
}
