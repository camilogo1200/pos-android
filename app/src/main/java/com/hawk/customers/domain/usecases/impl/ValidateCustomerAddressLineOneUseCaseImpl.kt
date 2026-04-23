package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAddressLineOneUseCase
import javax.inject.Inject

class ValidateCustomerAddressLineOneUseCaseImpl @Inject constructor() :
    ValidateCustomerAddressLineOneUseCase {

    override fun invoke(addressLineOne: String): CustomerFieldValidationResult =
        if (addressLineOne.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
