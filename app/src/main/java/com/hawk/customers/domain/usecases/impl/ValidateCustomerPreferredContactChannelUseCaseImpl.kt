package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerFieldValidationResult
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPreferredContactChannelUseCase
import javax.inject.Inject

class ValidateCustomerPreferredContactChannelUseCaseImpl @Inject constructor() :
    ValidateCustomerPreferredContactChannelUseCase {

    override fun invoke(channel: String): CustomerFieldValidationResult =
        if (channel.isBlank()) {
            CustomerFieldValidationResult(CustomerFieldValidationError.REQUIRED)
        } else {
            CustomerFieldValidationResult()
        }
}
