package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerBirthDateUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import javax.inject.Inject

class ValidateCustomerBirthDateUseCaseImpl @Inject constructor() : ValidateCustomerBirthDateUseCase {

    private val formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    override fun invoke(birthDate: String): CustomerFieldValidationResult {
        val trimmedValue = birthDate.trim()
        if (trimmedValue.isEmpty()) {
            return CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        }

        return try {
            val parsedDate = LocalDate.parse(trimmedValue, formatter)
            if (parsedDate.isAfter(LocalDate.now())) {
                CustomerFieldValidationResult(CustomerFieldValidationError.INVALID_DATE)
            } else {
                CustomerFieldValidationResult()
            }
        } catch (_: Exception) {
            CustomerFieldValidationResult(CustomerFieldValidationError.INVALID_DATE)
        }
    }
}
